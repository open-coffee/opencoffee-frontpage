(function() {
  const form = document.getElementById('plugin-configuration-form');
  // disable native validation
  form.noValidate = true;
  form.classList.remove('invalid');

  form.addEventListener('submit', event => {
    if (!form.checkValidity()) {
      event.preventDefault();
      form.classList.add('invalid');
    }
  });

  for (const field of form.querySelectorAll('input')) {
    // Add error container
    field.insertAdjacentHTML('afterend', '<div class="error"></div>');
    // Show message on `invalid` event
    field.addEventListener('invalid', event => {
      field.classList.add('invalid');
      field.nextSibling.textContent = field.validationMessage;

      // Reset invalid state and error message on `input` event, trigger validation check
      const handleInput = () => {
        field.removeEventListener('input', handleInput);
        field.classList.remove('invalid');
        field.nextSibling.textContent = '';
        field.checkValidity();
      };
      field.addEventListener('input', handleInput);
    });

    field.addEventListener('blur', () => {
      field.checkValidity();
    });
  }
}());
