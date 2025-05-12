/**
 * Enhances a standard HTML input slider by adding a draggable knob that displays the current value
 * @param {HTMLInputElement} originalSlider - The original input[type="range"] element
 * @param {Object} options - Optional configuration
 * @param {string} options.knobColor - Background color of the knob (default: "#3498db")
 * @param {string} options.knobTextColor - Text color inside the knob (default: "white")
 * @param {number} options.knobSize - Size of the knob in pixels (default: 40)
 * @returns {HTMLDivElement} - The container element with the enhanced slider
 */
function addNumericDisplay(originalSlider, options = {}) {
  // Default options
  const config = {
    knobColor: options.knobColor || "#FFFFFF",
    knobTextColor: options.knobTextColor || "#888",
    knobSize: options.knobSize || 22
  };

  // Validate input
  if (!originalSlider || originalSlider.type !== "range") {
    console.error("Element must be an input[type='range'] element");
    return null;
  }
  
  // Get original slider properties
  const min = parseFloat(originalSlider.min) || 0;
  const max = parseFloat(originalSlider.max) || 100;
  const step = parseFloat(originalSlider.step) || 1;
  const value = parseFloat(originalSlider.value) || min;
  const originalWidth = originalSlider.offsetWidth;
  
  // Create wrapper and hide original slider
  const wrapper = document.createElement("div");
  wrapper.className = "fancy-slider-wrapper";
  wrapper.style.cssText = `
    position: relative;
    width: ${originalWidth}px;
    height: ${config.knobSize}px;
    margin: 0px ${config.knobSize/2}px;
  `;
  
  // Insert wrapper before the original slider
  originalSlider.parentNode.insertBefore(wrapper, originalSlider);
  
  // Hide original slider but keep it in the DOM for form submission
  originalSlider.style.display = "none";
  
  // Create new slider components
  const track = document.createElement("div");
  track.className = "fancy-slider-track";
  track.style.cssText = `
    position: absolute;
    top: 50%;
    width: 100%;
    height: 6px;
    background: #999;
    border-radius: 3px;
    transform: translateY(-50%);
  `;
  
  const fill = document.createElement("div");
  fill.className = "fancy-slider-fill";
  fill.style.cssText = `
    position: absolute;
    top: 50%;
    left: 0;
    height: 6px;
    background: ${config.knobColor};
    border-radius: 3px;
    transform: translateY(-50%);
    width: 0%;
  `;
  
  const knob = document.createElement("div");
  knob.className = "fancy-slider-knob";
  knob.style.cssText = `
    position: absolute;
    top: 50%;
    width: ${config.knobSize}px;
    height: ${config.knobSize}px;
    background: ${config.knobColor};
    border-radius: 50%;
    transform: translate(-50%, -50%);
    cursor: pointer;
    box-shadow: 0 2px 5px rgba(0,0,0,0.2);
    display: flex;
    justify-content: center;
    align-items: center;
    color: ${config.knobTextColor};
    font-weight: bold;
    user-select: none;
    font-family: sans-serif;
    font-size: ${config.knobSize / 1.5}px;
  `;
  
  // Append components to the wrapper
  wrapper.appendChild(track);
  wrapper.appendChild(fill);
  wrapper.appendChild(knob);
  
  // Handle knob dragging
  let isDragging = false;

  // Function to update slider display
  function updateSliderPosition(value) {
    // Constrain value to range and step
    value = Math.max(min, Math.min(max, value));
    value = Math.round(value / step) * step;
    
    // Handle floating point precision for display
    let displayValue = value;
    if (step < 1) {
      const decimalPlaces = String(step).split('.')[1]?.length || 0;
      displayValue = value.toFixed(decimalPlaces);
    }
    
    // Calculate percentage for positioning
    const percent = (value - min) / (max - min);
    const position = percent * 100;
    
    // Update DOM elements
    knob.style.left = `${position}%`;
    fill.style.width = `${position}%`;
    knob.textContent = displayValue;
    
    // Update original slider value
    originalSlider.value = value;
    
    // Dispatch input and change events to maintain compatibility
    const inputEvent = new Event('input', { bubbles: true });
    const changeEvent = new Event('change', { bubbles: true });
    originalSlider.dispatchEvent(inputEvent);
    
    // Only dispatch change event when done dragging
    if (!isDragging) {
      originalSlider.dispatchEvent(changeEvent);
    }
  }
  
  // Set initial position
  updateSliderPosition(value);
  
  knob.addEventListener('mousedown', function(e) {
    isDragging = true;
    document.addEventListener('mousemove', onMouseMove);
    document.addEventListener('mouseup', onMouseUp);
    e.preventDefault(); // Prevent text selection
  });
  
  track.addEventListener('click', function(e) {
    const rect = track.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const percent = x / rect.width;
    const value = min + percent * (max - min);
    updateSliderPosition(value);
  });
  
  function onMouseMove(e) {
    if (!isDragging) return;
    
    const rect = track.getBoundingClientRect();
    let x = e.clientX - rect.left;
    
    // Constrain x to the track width
    x = Math.max(0, Math.min(x, rect.width));
    
    const percent = x / rect.width;
    const value = min + percent * (max - min);
    updateSliderPosition(value);
  }
  
  function onMouseUp() {
    isDragging = false;
    document.removeEventListener('mousemove', onMouseMove);
    document.removeEventListener('mouseup', onMouseUp);
  }
  
  // Touch support
  knob.addEventListener('touchstart', function(e) {
    isDragging = true;
    document.addEventListener('touchmove', onTouchMove, { passive: false });
    document.addEventListener('touchend', onTouchEnd);
    e.preventDefault(); // Prevent scrolling when touching knob
  });
  
  function onTouchMove(e) {
    if (!isDragging) return;
    
    const touch = e.touches[0];
    const rect = track.getBoundingClientRect();
    let x = touch.clientX - rect.left;
    
    // Constrain x to the track width
    x = Math.max(0, Math.min(x, rect.width));
    
    const percent = x / rect.width;
    const value = min + percent * (max - min);
    updateSliderPosition(value);
    
    // Prevent scrolling while dragging
    e.preventDefault();
  }
  
  function onTouchEnd() {
    isDragging = false;
    document.removeEventListener('touchmove', onTouchMove);
    document.removeEventListener('touchend', onTouchEnd);
  }
  
  // Listen for changes to the original slider
  originalSlider.addEventListener('input', function() {
    if (!isDragging) { // Only update if not currently dragging
      updateSliderPosition(parseFloat(originalSlider.value));
    }
  });
  
  // Expose API for programmatic control
  wrapper.updateValue = function(newValue) {
    updateSliderPosition(parseFloat(newValue));
  };
  
  return wrapper;
}

function initAllSliders() {
  let inps = document.getElementsByTagName("input");
  for (let inp of inps) {
    if (inp.getAttribute("numeric") === "") {
      addNumericDisplay(inp);
    }
  }
}









