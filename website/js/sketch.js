function preload() {
  java = null;
  initWASM().then(response => {
    java = response;
    update();
  }, () => console.log("Failed loading Web Assembly"))
}

function setup() {
  noCanvas();
  editDist = 1
  firstLetterOnly = false;
  initDivs();
  DropDowns.init();
  initAllSliders();
}

function update() {
  const write = document.getElementById("write");
  const read = document.getElementById("read");
  highlight(write.value, read);
}

function initDivs() {
  const write = document.getElementById("write");
  const read = document.getElementById("read");
  
  write.addEventListener('input', update);
  write.addEventListener('scroll', () => {
    read.scrollTop = write.scrollTop;
    read.scrollLeft = write.scrollLeft;
  });
  
  write.addEventListener('contextmenu', async (e) => {
    e.preventDefault(); // This stops the dropdown menu
    
    function startWithCapital(word) {
      if (!word) return false;
      return word[0].toUpperCase() == word[0];
    }
    
    function isAllUpper(word) {
      if (!word) return false;
      return word.toUpperCase() == word;
    }
    
    let pos = getSelectedWord(write);
    let word = write.value.slice(pos[0], pos[1]);
    let startCap = startWithCapital(word);
    let allUpper = isAllUpper(word);
    word = word.toLowerCase();
    let isWord = (await check([word]))[0];
    if (isWord) return;
    let words = (await search(editDist, firstLetterOnly, [word]))[0];
    
    function transform(word) {
      if (allUpper) {
        return word.toUpperCase();
      } else if (startCap) {
        return word[0].toUpperCase() + word.substring(1);
      } else {
        return word;
      }
    }
    
    let replaceWord = (i) => {
      let txt = write.value;
      let corrected = words[i];
      corrected = transform(corrected);
      
      let newStr = txt.slice(0,pos[0]) + corrected + txt.slice(pos[1]);
      write.value = newStr;
      update();
    };
    
    DropDowns.openDropDownAtMouse(words, replaceWord, transform);
  });
}

/*





















*/
