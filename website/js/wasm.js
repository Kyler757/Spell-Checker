
async function initWASM() {
  teavm = await TeaVM.wasmGC.load("assets/classes.wasm", {
    stackDeobfuscator: {
      // set to true during development phase, as well as `debugInformationGenerated`
      // option in pom.xml to get clear stack traces. Don't forget
      // to disable for production.
      enabled: false,
    },
  });

  let response = await fetch("assets/dawg.txt");
  let data = await response.text();
  
  teavm.exports.main(["load", data]);
  
  return teavm.exports.main;
}

/*


















*/
