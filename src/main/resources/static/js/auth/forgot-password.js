function emailValidate() {
    let isValid = true;

    // Email Validation
    let email = document.getElementById("email").value;
    let patternEmail =
        /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.(com|org|net|info|biz|pro|edu|gov|mil|int|ai|io|tech|store|online|site|xyz|finance|media|design|photography|museum|travel|blog|app|dev|cloud|solutions|company|consulting|academy|family|news|club|fashion|shop|vip|love|wiki|eco|space|dog|beer|fun|ninja|guru|rocks|ac\.in|co\.in|gov\.in|edu\.in|res\.in|net\.in|org\.in|ac\.uk|co\.uk|gov\.uk|org\.uk|ltd\.uk|plc\.uk|net\.uk|sch\.uk|com\.au|net\.au|org\.au|edu\.au|gov\.au|co\.nz|org\.nz|ac\.nz|govt\.nz|co\.za|org\.za|gov\.za|ac\.za|net\.za|com\.br|net\.br|gov\.br|org\.br|edu\.br|mil\.br|com\.cn|gov\.cn|edu\.cn|com\.jp|co\.jp|or\.jp|ac\.jp|go\.jp|com\.hk|org\.hk|edu\.hk|gov\.hk|net\.hk)$/i;
    let errorEmail = document.getElementById("email-invalid");

    if (email === "") {
        errorEmail.innerHTML = forgotPasswordErrorMessages.emailRequired;
        isValid = false;
    } else if (patternEmail.test(email) === false) {
        errorEmail.innerHTML = forgotPasswordErrorMessages.emailInvalid;
        isValid = false;
    } else {
        errorEmail.innerHTML = "";
    }

    return isValid;
}

function otpValidate() {
    let isValid = true;

    // Otp Validation
    let otp = document.getElementById("otp").value;
    let patternOtp = /^[0-9]{6}$/;
    let errorOtp = document.getElementById("otp-invalid");
    console.log(otp);
    if (otp === "") {
        errorOtp.innerHTML = forgotPasswordErrorMessages.otpRequired;
        isValid = false;
    } else if (patternOtp.test(otp) === false) {
        errorOtp.innerHTML = forgotPasswordErrorMessages.otpInvalid;
        isValid = false;
    } else {
        errorOtp.innerHTML = "";
    }

    return isValid;
}

function passwordValidate() {
    let isValid = true;

    // Password Validation
    let password = document.getElementById("new-password").value;
    let patternPassword =
        /^(?=.*[A-Za-z])(?=.*\d+)(?=.*[@$!%*?&]+)[A-Za-z\d@$!%*?&]{8,16}$/;
    let errorPassword = document.getElementById("password-invalid");

    if (password === "") {
        errorPassword.innerHTML = forgotPasswordErrorMessages.passwordRequired;
        isValid = false;
    } else if (patternPassword.test(password) === false) {
        errorPassword.innerHTML = forgotPasswordErrorMessages.passwordInvalid;
        isValid = false;
    } else {
        errorPassword.innerHTML = "";
    }

    // Confirm Password Validation
    let confirmPassword = document.getElementById("confirm-password").value;
    let errorConfirmPassword = document.getElementById("password-mismatch");

    if (password !== confirmPassword) {
        errorConfirmPassword.innerHTML = forgotPasswordErrorMessages.passwordMismatch;
        isValid = false;
    } else {
        errorConfirmPassword.innerHTML = "";
    }

    return isValid;
}