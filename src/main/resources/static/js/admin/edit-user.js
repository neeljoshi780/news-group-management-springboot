function validate() {
    let isValid = true;

    // FirstName Validation
    let firstName = document.getElementById("firstName").value;
    let patternFirstName = /^[A-Za-z]{1,30}$/;
    let errorFirstName = document.getElementById("errorFirstName");

    if (firstName !== "" && patternFirstName.test(firstName) === false) {
        errorFirstName.innerHTML = "First name is not valid.";
        isValid = false;
    } else {
        errorFirstName.innerHTML = "";
    }

    // LastName Validation
    let lastName = document.getElementById("lastName").value;
    let patternLastName = /^[A-Za-z]{1,30}$/;
    let errorLastName = document.getElementById("errorLastName");

    if (lastName !== "" && patternLastName.test(lastName) === false) {
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
    if (patternEmail.test(email) === false) {
        errorEmail.innerHTML = "Email is not valid.";
        isValid = false;
    } else {
        errorEmail.innerHTML = "";
    }

    // Phone Validation
    let phone = document.getElementById("phone").value;
    let patternPhone = /^[6-9][0-9]{9}$/;
    let errorPhone = document.getElementById("errorPhone");

    if (phone !== "" && patternPhone.test(phone) === false) {
        errorPhone.innerHTML = "Enter a valid 10-digit Phone number.";
        isValid = false;
    } else {
        errorPhone.innerHTML = "";
    }

    // City Validation
    let city = document.getElementById("city").value;
    let patternCity = /^[A-Za-z]{1,40}$/;
    let errorCity = document.getElementById("errorCity");

    if (city !== "" && patternCity.test(city) === false) {
        errorCity.innerHTML = "City is not valid.";
        isValid = false;
    } else {
        errorCity.innerHTML = "";
    }

    // State Validation
    let state = document.getElementById("state").value;
    let patternState = /^[A-Za-z]{1,40}$/;
    let errorState = document.getElementById("errorState");

    if (state !== "" && patternState.test(state) === false) {
        errorState.innerHTML = "State is not valid.";
        isValid = false;
    } else {
        errorState.innerHTML = "";
    }

    // Birthdate Validation
    let birthDate = document.getElementById("dob").value;
    let errorBirthdate = document.getElementById("errorDOB");

    const inputDate = new Date(birthDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    inputDate.setHours(0, 0, 0, 0);

    if (inputDate >= today) {
        errorBirthdate.innerHTML = "Dob is not valid.";
        isValid = false;
    } else {
        errorBirthdate.innerHTML = "";
    }

    return isValid;
}