function validate() {
    let isValid = true;

    // FullName Validation
    let fullName = document.getElementById("fullName").value;
    let patternFullName =
        /^(?=.{2,40}$)[a-zA-Z][a-zA-Z]*(?: [a-zA-Z]+){0,2}$/;
    let errorFullName = document.getElementById("errorFullName");

    if (fullName === "") {
        errorFullName.innerHTML = contactErrorMessages.fullNameRequired;
        isValid = false;
    } else if (patternFullName.test(fullName) === false) {
        errorFullName.innerHTML = contactErrorMessages.fullNameInvalid;
        isValid = false;
    } else {
        errorFullName.innerHTML = "";
    }

    // Email Validation
    let email = document.getElementById("email").value;
    let patternEmail =
        /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.(com|org|net|info|biz|pro|edu|gov|mil|int|ai|io|tech|store|online|site|xyz|finance|media|design|photography|museum|travel|blog|app|dev|cloud|solutions|company|consulting|academy|family|news|club|fashion|shop|vip|love|wiki|eco|space|dog|beer|fun|ninja|guru|rocks|ac\.in|co\.in|gov\.in|edu\.in|res\.in|net\.in|org\.in|ac\.uk|co\.uk|gov\.uk|org\.uk|ltd\.uk|plc\.uk|net\.uk|sch\.uk|com\.au|net\.au|org\.au|edu\.au|gov\.au|co\.nz|org\.nz|ac\.nz|govt\.nz|co\.za|org\.za|gov\.za|ac\.za|net\.za|com\.br|net\.br|gov\.br|org\.br|edu\.br|mil\.br|com\.cn|gov\.cn|edu\.cn|com\.jp|co\.jp|or\.jp|ac\.jp|go\.jp|com\.hk|org\.hk|edu\.hk|gov\.hk|net\.hk)$/i;
    let errorEmail = document.getElementById("errorEmail");

    if (email === "") {
        errorEmail.innerHTML = contactErrorMessages.emailRequired;
        isValid = false;
    } else if (patternEmail.test(email) === false) {
        errorEmail.innerHTML = contactErrorMessages.emailInvalid;
        isValid = false;
    } else {
        errorEmail.innerHTML = "";
    }

    // Phone Validation
    let phone = document.getElementById("phone").value;
    let patternPhone = /^[6-9][0-9]{9}$/;
    let errorPhone = document.getElementById("errorPhone");

    if (phone === "") {
        errorPhone.innerHTML = contactErrorMessages.phoneRequired;
        isValid = false;
    } else if (patternPhone.test(phone) === false) {
        errorPhone.innerHTML = contactErrorMessages.phoneInvalid;
        isValid = false;
    } else {
        errorPhone.innerHTML = "";
    }

    // Message Validation
    let message = document.getElementById("message").value;
    let patternMessage =
        /^(?=.{2,500}$)[a-zA-Z0-9][a-zA-Z0-9.,:;'"$%&?!()\-–—]*(?: (?! )[a-zA-Z0-9.,:;'"$%&?!()\-–—]+){0,100}$/;
    let errorMessage = document.getElementById("errorMessage");

    if (message === "") {
        errorMessage.innerHTML = contactErrorMessages.messageRequired;
        isValid = false;
    } else if (patternMessage.test(message) === false) {
        errorMessage.innerHTML = contactErrorMessages.messageInvalid;
        isValid = false;
    } else {
        errorMessage.innerHTML = "";
    }

    return isValid;
}