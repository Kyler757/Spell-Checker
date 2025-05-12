lockHighlight = false;

function findAllOccurrences(str, substr) {
  const indices = [];
  let index = str.indexOf(substr);

  while (index !== -1) {
    indices.push(index);
    index = str.indexOf(substr, index + 1);
  }

  return indices;
}

async function waitForAnswer() {
  const elem = document.getElementById("answer");
  
  await new Promise((resolve) => {
    const observer = new MutationObserver(() => {
      resolve();
    });
    
    observer.observe(elem, {
      subtree: true,
      childList: true,
    });
    
    if (elem.innerHTML != "")
      resolve();
  });

  return elem.innerHTML;
}

async function check(words) {
  if (words.length == 0) return [];
  java(["check", ...words]);
  const elem = document.getElementById("answer");
  let wordtxt = await waitForAnswer();
  elem.innerHTML = "";
  return wordtxt.split(" ").map(e => e == "true");
}

async function search(dist, firstLetter, words) {
  if (words.length == 0) return [];
  java(["search", dist.toString(), firstLetter.toString(), ...words]);
  const elem = document.getElementById("answer");
  let wordtxt = await waitForAnswer();
  elem.innerHTML = "";
  return wordtxt.split("\n").map((word) => word.split(" "));
}

async function highlight(text, targetElement, lineHints) {
  if (lockHighlight) return;
  lockHighlight = true;
  
  // WASM not loaded yet
  if (!java) {
    lockHighlight = false;
    return;
  }
  
  // targetElement.innerHTML = "";
  lineHints = lineHints ?? [];
  let highlightedText = text;

  // Helper function to check if a position is already highlighted
  const isAlreadyHighlighted = (start, end) => {
    // Regex search for span tags that encapsulate the highlighted text
    const regex = /<span[^>]*>(.*?)<\/span>/g;

    const spans = highlightedText.matchAll(regex);

    for (const span of spans) {
      const spanStart = span.index;
      const spanEnd = span.index + span[0].length;

      if (spanStart <= start && spanEnd >= end) {
        return true;
      }
    }

    return false;
  };

  let regex = /\b[A-z']+\b/g;
  
  let words = Array.from(highlightedText.matchAll(regex).map(e => e[0]));
  let checkedWords = await check(words);
  
  let wordIdx = 0;
  highlightedText = highlightedText.replace(regex, (match, ...args) => {
    const offset = args[args.length - 2];

    if (isAlreadyHighlighted(offset, offset + match.length)) {
      return match;
    }
    
    let correct = checkedWords[wordIdx++];
    // let correct = true;
    
    if (correct)
      retVal = `<span>${match}</span>`;
    else
      retVal = `<span class="wrong">${match}</span>`;
    
    return retVal;
  });

  const endLines = [
    0,
    ...findAllOccurrences(highlightedText, "\n"),
    highlightedText.length,
  ];

  // Process line hints
  for (let i = lineHints.length - 1; i >= 0; i--) {
    const msg = lineHints[i];
    const { line, style, text, textStyle } = msg;
    const start = endLines[line];
    const end = endLines[line + 1];

    highlightedText =
      highlightedText.slice(0, start) +
      `<span style="${style}">${highlightedText.slice(start, end)}</span>` +
      `<span style="${textStyle}"> ${text}</span>` +
      highlightedText.slice(end);
  }
  
  targetElement.innerHTML = highlightedText + "<br>";
  lockHighlight = false;
}

function getSelectedWord(elem) {
  const text = elem.value;
  let start = elem.selectionStart;
  let end = elem.selectionEnd;

  if (start !== end) {
    throw Error("Should not have selection after typing");
  }

  // Expand to word boundaries
  while (start > 0 && /[\w']/.test(text[start - 1])) {
    start--;
  }
  while (end < text.length && /[\w']/.test(text[end])) {
    end++;
  }

  return [start, end];
}
