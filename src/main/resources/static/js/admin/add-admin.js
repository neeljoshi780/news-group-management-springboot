function validate() {
    let isValid = true;

    // FirstName Validation
    let firstName = document.getElementById("firstName").value;
    let patternFirstName = /^[A-Za-z]{1,30}$/;
    let errorFirstName = document.getElementById("errorFirstName");

    if (firstName == "") {
        errorFirstName.innerHTML = "First name is required.";
        isValid = false;
    } else if (patternFirstName.test(firstName) === false) {
        errorFirstName.innerHTML = "First name is not valid.";
        isValid = false;
    } else {
        errorFirstName.innerHTML = "";
    }

    // LastName Validation
    let lastName = document.getElementById("lastName").value;
    let patternLastName = /^[A-Za-z]{1,30}$/;
    let errorLastName = document.getElementById("errorLastName");

    if (lastName == "") {
        errorLastName.innerHTML = "Last name is required.";
        isValid = false;
    } else if (patternLastName.test(lastName) === false) {
        errorLastName.innerHTML = "Last name is not valid.";
        isValid = false;
    } else {
        errorLastName.innerHTML = "";
    }

    // Email Validation
    let email = document.getElementById("email").value;
    let patternEmail =
        /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.(com|org|net|info|biz|pro|edu|gov|mil|int|ai|io|tech|store|online|site|xyz|finance|media|design|photography|museum|travel|blog|app|dev|cloud|solutions|company|consulting|academy|family|news|club|fashion|shop|vip|love|wiki|eco|space|dog|beer|fun|ninja|guru|rocks|ac\.in|co\.in|gov\.in|edu\.in|res\.in|net\.in|org\.in|ac\.uk|co\.uk|gov\.uk|org\.uk|ltd\.uk|plc\.uk|net\.uk|sch\.uk|com\.au|net\.au|org\.au|edu\.au|gov\.au|co\.nz|org\.nz|ac\.nz|govt\.nz|co\.za|org\.za|gov\.za|ac\.za|net\.za|com\.br|net\.br|gov\.br|org\.br|edu\.br|mil\.br|com\.cn|gov\.cn|edu\.cn|com\.jp|co\.jp|or\.jp|ac\.jp|go\.jp|com\.hk|org\.hk|edu\.hk|gov\.hk|net\.hk)$/i;
    let errorEmail = document.getElementById("errorEmail");
    if (email == "") {
        errorEmail.innerHTML = "Email is required.";
        isValid = false;
    } else if (patternEmail.test(email) === false) {
        errorEmail.innerHTML = "Email is not valid.";
        isValid = false;
    } else {
        errorEmail.innerHTML = "";
    }

    // Phone Validation
    let phone = document.getElementById("phone").value;
    let patternPhone = /^[6-9][0-9]{9}$/;
    let errorPhone = document.getElementById("errorPhone");

    if (phone == "") {
        errorPhone.innerHTML = "Phone is required.";
        isValid = false;
    } else if (patternPhone.test(phone) === false) {
        errorPhone.innerHTML = "Enter a valid 10-digit Phone number.";
        isValid = false;
    } else {
        errorPhone.innerHTML = "";
    }

    return isValid;
}