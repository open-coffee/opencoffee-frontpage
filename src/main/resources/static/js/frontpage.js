(function() {
  document.body.addEventListener('click', function handleBodyClick(event) {
    if (getEventPath(event).some(element => isPluginControlElement(element))) {
      return;
    }
    const pluginControls = document.querySelector('.plugin-controls input:checked');
    if (pluginControls) {
      pluginControls.click();
    }
  });

  function getEventPath(event) {
    if (event.path) {
      return event.path;
    }
    if (typeof event.composedPath === 'function') {
      return event.composedPath();
    }
    let element = event.target;
    const path = [element];
    while (element.parentNode) {
      path.push(element.parentNode);
      element = element.parentNode
    }
    return path;
  }

  function isPluginControlElement(element) {
    if (element.getAttribute) {
      const forAttr = element.getAttribute('for') || '';
      if (forAttr.startsWith('plugin-controls-')) {
        return true;
      }
      // firefox triggers 'click' event when checkbox is selected by label click
      if ((element.getAttribute('id') || '').startsWith('plugin-controls-')) {
        return true;
      }
    }
    return false;
  }
}());
