(function() {
  const form = document.getElementById('plugin-configuration-form');
  form.noValidate = true;
  form.classList.remove('invalid');

  form.addEventListener('submit', event => {
    if (!form.checkValidity()) {
      event.preventDefault();
      form.classList.add('invalid');
    }
  });

  const ensureField = field => {
    if (field.checkValidity()) {
      field.classList.remove('invalid');
      field.nextElementSibling.textContent = '';
    }
  };

  for (const field of form.querySelectorAll('input')) {
    field.addEventListener('input', () => ensureField(field));
    field.addEventListener('blur', () => ensureField(field));
    field.addEventListener('invalid', () => {
      field.classList.add('invalid');
      field.nextElementSibling.textContent = field.validationMessage;
    });
  }
}());
