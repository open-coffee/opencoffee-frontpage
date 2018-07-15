(function() {
  document.body.addEventListener('click', function handleBodyClick(event) {
    if (event.path.some(element => isPluginControlElement(element))) {
      return;
    }
    const pluginControls = document.querySelector('.plugin-controls input:checked');
    if (pluginControls) {
      pluginControls.click();
    }
  });

  function isPluginControlElement(element) {
    if (element.getAttribute) {
      const forAttr = element.getAttribute('for') || '';
      return forAttr.startsWith('plugin-controls-')
    }
    return false;
  }
}());
