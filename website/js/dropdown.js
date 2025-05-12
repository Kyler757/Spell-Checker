class DropDowns {
  static init() {
    const dropdowns = document.getElementsByClassName("dropdown");
    DropDowns.hideAllDropDowns();

    let ignoreMouseUp = false;
    
    dropdowns[0].addEventListener("mousedown", (e) => {
      ignoreMouseUp = true;
    });
    
    // Initially hide drop downs on click
    document.addEventListener("mouseup", (e) => {
      if (ignoreMouseUp) {
        ignoreMouseUp = false;
        return;
      }
      if (e.button !== 0) return;
      DropDowns.hideAllDropDowns();
    });

    document.addEventListener("mousemove", (event) => {
      this.mx = event.clientX;
      this.my = event.clientY;
    });

    document.addEventListener("keydown", (e) => {
      if (key == "Escape") {
        DropDowns.hideAllDropDowns();
      }
    });
  }

  static openDropDownAtMouse(words, callback, transform) {
    const dropdowns = document.getElementsByClassName("dropdown");
    let dropdown = dropdowns[0];
    DropDowns.setDropDownPos(dropdown);
    DropDowns.showDropDown(dropdown);
    let list = dropdown.getElementsByClassName("dropdown-content")[0];
    list.innerHTML = "";
    DropDowns.resetScroll();
    transform = transform ?? (w => w);

    // Add words to dropdown
    for (let i = 0; i < words.length; i++) {
      let word = words[i];
      if (!word) continue;
      let option = document.createElement("a");
      option.onclick = () => {
        DropDowns.hideAllDropDowns();
        callback(i);
      }
      let txt = document.createElement("span");
      txt.innerText = transform(word);
      option.appendChild(txt);
      list.appendChild(option);
    }
    
    DropDowns.initStyles(dropdown);
  }

  static resetScroll() {
    const dropdowns = document.getElementsByClassName("dropdown");
    let dropdown = dropdowns[0];
    let list = dropdown.getElementsByClassName("dropdown-content")[0];
    list.scrollTop = 0;
  }

  static hideAllDropDowns() {
    const dropdowns = document.getElementsByClassName("dropdown");
    Array.from(dropdowns).forEach((e) => DropDowns.hideDropDown(e));
  }

  static hideDropDown(dropdown) {
    dropdown.children[0].setAttribute("style", "display: none;");
  }

  static showDropDown(dropdown) {
    dropdown.children[0].setAttribute("style", "display: block;");
  }

  static setDropDownPos(dropdown) {
    dropdown.style.left = `${this.mx}px`;
    dropdown.style.top = `${this.my}px`;
    // dropdown.style.left = `$50px`;
    // dropdown.style.top = `$50px`;
  }

  static initStyles(dropdown) {
    let options = dropdown.getElementsByClassName("dropdown-content")[0]
      .children;
    for (const option of options) {
      let update = (e) => {
        // const x = e.clientX / elem.offsetWidth;
        const bounds = option.getBoundingClientRect();
        const x = (e.clientX - bounds.x) / bounds.width;
        option.style.setProperty("--start", x);
      };
      option.addEventListener("mousemove", update);
      option.addEventListener("mouseup", update);
    }
  }
}

/*

























*/
