// Ensure DOM is fully loaded before running script
document.addEventListener("DOMContentLoaded", () => {
    // Tab switching
    const tabLinks = document.querySelectorAll(".nav-tabs a");
    const sections = document.querySelectorAll(".section");

    tabLinks.forEach((link) => {
        link.addEventListener("click", (e) => {
            e.preventDefault();
            const tabId = link.getAttribute("data-tab");

            tabLinks.forEach((l) => l.classList.remove("active"));
            link.classList.add("active");

            sections.forEach((section) => section.classList.remove("active"));
            document.querySelector(`.section.${tabId}`).classList.add("active");
        });
    });

    // Popup management
    window.showPopup = function (event) {
        event.preventDefault();
        const popup = document.getElementById("confirmation-popup");
        if (popup) {
            popup.style.display = "block";
        } else {
            console.error("Confirmation popup not found!");
        }
    };

    // Initial state (User Details active by default)
    document.querySelector('.nav-tabs a[data-tab="user-details"]').classList.add("active");
    document.querySelector(".section.user-details").classList.add("active");
});

function profileValidate() {
    const fileInput = document.getElementById("profile-photo");
    const file = fileInput.files[0];
    let errorProfile = document.getElementById("profile-photo-invalid");

    if (!file) {
        errorProfile.innerHTML = userProfileErrorMessages.noFile;
        return false;
    }

    const allowedTypes = ["image/jpg", "image/jpeg", "image/png"];
    if (!allowedTypes.includes(file.type)) {
        errorProfile.innerHTML = userProfileErrorMessages.invalidType;
        return false;
    }

    const maxSize = 2 * 1024 * 1024;
    if (file.size > maxSize) {
        errorProfile.innerHTML = userProfileErrorMessages.tooLarge;
        return false;
    }

    return true;
}

// Email not update
document.getElementById("email").readOnly = true;
function validate() {
    let isValid = true;

    // FirstName Validation
    let firstName = document.getElementById("first-name").value;
    let patternFirstName = /^[A-Za-z]{1,30}$/;
    let errorFirstName = document.getElementById("first-name-invalid");

    if (firstName !== "" && patternFirstName.test(firstName) === false) {
        errorFirstName.innerHTML = userProfileErrorMessages.firstNameInvalid;
        isValid = false;
    } else {
        errorFirstName.innerHTML = "";
    }

    // LastName Validation
    let lastName = document.getElementById("last-name").value;
    let patternLastName = /^[A-Za-z]{1,30}$/;
    let errorLastName = document.getElementById("last-name-invalid");

    if (lastName !== "" && patternLastName.test(lastName) === false) {
        errorLastName.innerHTML = userProfileErrorMessages.lastNameInvalid;
        isValid = false;
    } else {
        errorLastName.innerHTML = "";
    }

    // Phone Validation
    let phone = document.getElementById("phone").value;
    let patternPhone = /^[6-9][0-9]{9}$/;
    let errorPhone = document.getElementById("phone-invalid");
    console.log(phone);

    if (phone !== "" && patternPhone.test(phone) === false) {
        errorPhone.innerHTML = userProfileErrorMessages.phoneInvalid;
        isValid = false;
    } else {
        errorPhone.innerHTML = "";
    }

    // City Validation
    let city = document.getElementById("city").value;
    let patternCity = /^[A-Za-z]{1,40}$/;
    let errorCity = document.getElementById("city-invalid");

    if (city !== "" && patternCity.test(city) === false) {
        errorCity.innerHTML = userProfileErrorMessages.cityInvalid;
        isValid = false;
    } else {
        errorCity.innerHTML = "";
    }

    // State Validation
    let state = document.getElementById("state").value;
    let patternState = /^[A-Za-z]{1,40}$/;
    let errorState = document.getElementById("state-invalid");

    if (state !== "" && patternState.test(state) === false) {
        errorState.innerHTML = userProfileErrorMessages.stateInvalid;
        isValid = false;
    } else {
        errorState.innerHTML = "";
    }

    // Birthdate Validation
    let birthDate = document.getElementById("dob").value;
    let errorBirthdate = document.getElementById("dob-invalid");

    const inputDate = new Date(birthDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    inputDate.setHours(0, 0, 0, 0);

    if (inputDate >= today) {
        errorBirthdate.innerHTML = userProfileErrorMessages.dobInvalid;
        isValid = false;
    }else{
        errorBirthdate.innerHTML = "";
    }

    // Gender Validation
    let gender = document.getElementById("gender").value;
    let errorGender = document.getElementById("gender-invalid");

    if(gender!=="" && gender !== "MALE" && gender !== "FEMALE" && gender !== "OTHER"){
        errorGender.innerHTML = userProfileErrorMessages.genderInvalid;
        isValid = false;
    }else{
        errorGender.innerHTML = "";
    }

    return isValid;
}

function passwordSetValidate() {
    let isValid = true;

    let setPassword = document.getElementById("set-new-password").value;
    let patternSetPassword =
        /^(?=.*[A-Za-z])(?=.*\d+)(?=.*[@$!%*?&]+)[A-Za-z\d@$!%*?&]{8,16}$/;
    let errorSetPassword = document.getElementById("set-new-password-error");

    if (setPassword === "") {
        errorSetPassword.innerHTML = userProfileErrorMessages.setPasswordRequired;
        isValid = false;
    } else if (patternSetPassword.test(setPassword) === false) {
        errorSetPassword.innerHTML = userProfileErrorMessages.newPasswordInvalid;
        isValid = false;
    } else {
        errorSetPassword.innerHTML = "";
    }

    // Confirm Password Validation
    let confirmPassword = document.getElementById("set-confirm-password").value;
    let errorConfirmPassword = document.getElementById(
        "set-confirm-password-error"
    );

    if (setPassword !== confirmPassword) {
        errorConfirmPassword.innerHTML = userProfileErrorMessages.confirmPasswordMismatch;
        isValid = false;
    } else {
        errorConfirmPassword.innerHTML = "";
    }

    return isValid;
}

function passwordValidate() {
    let isValid = true;

    // OldPassword Validation
    let password = document.getElementById("old-password").value;
    let patternPassword =
        /^(?=.*[A-Za-z])(?=.*\d+)(?=.*[@$!%*?&]+)[A-Za-z\d@$!%*?&]{8,16}$/;
    let errorPassword = document.getElementById("old-password-error");

    if (password === "") {
        errorPassword.innerHTML = userProfileErrorMessages.oldPasswordRequired;
        isValid = false;
    } else if (patternPassword.test(password) === false) {
        errorPassword.innerHTML = userProfileErrorMessages.oldPasswordInvalid;
        isValid = false;
    } else {
        errorPassword.innerHTML = "";
    }

    // Password Validation
    let newPassword = document.getElementById("new-password").value;
    let patternNewPassword =
        /^(?=.*[A-Za-z])(?=.*\d+)(?=.*[@$!%*?&]+)[A-Za-z\d@$!%*?&]{8,16}$/;
    let errorNewPassword = document.getElementById("new-password-error");

    if (newPassword === "") {
        errorNewPassword.innerHTML = userProfileErrorMessages.newPasswordRequired;
        isValid = false;
    } else if (patternNewPassword.test(newPassword) === false) {
        errorNewPassword.innerHTML = userProfileErrorMessages.newPasswordInvalid;
        isValid = false;
    } else {
        errorNewPassword.innerHTML = "";
    }

    // Confirm Password Validation
    let confirmPassword = document.getElementById("confirm-password").value;
    let errorConfirmPassword = document.getElementById(
        "confirm-password-error"
    );

    if (newPassword !== confirmPassword) {
        errorConfirmPassword.innerHTML = userProfileErrorMessages.confirmPasswordMismatch;
        isValid = false;
    } else {
        errorConfirmPassword.innerHTML = "";
    }

    return isValid;
}