// Edit Save Toggle in Form
const editToggle = document.getElementById("edit-toggle");
const profileForm = document.getElementById("profile-form");
const imageForm = document.getElementById("image-form");
const inputs = profileForm.querySelectorAll("input, textarea, select");
const formActions = profileForm.querySelector(".form-actions");
const cancelEdit = document.getElementById("cancel-edit");

editToggle.addEventListener("click", () => {
    inputs.forEach((input) => {
        if (input.id !== "email") {
            input.removeAttribute("readonly");
            input.removeAttribute("disabled");
        }
    });
    formActions.classList.remove("hidden");
    imageForm.classList.remove("hidden");
    editToggle.classList.add("hidden");
});

cancelEdit.addEventListener("click", () => {
    inputs.forEach((input) => {
        input.setAttribute("readonly", true);
        if (input.tagName === "SELECT") input.setAttribute("disabled", true);
    });
    formActions.classList.add("hidden");
    imageForm.classList.add("hidden");
    editToggle.classList.remove("hidden");
    profileForm.reset();
});

function validateProfileImg() {
    let isValid = true;

    let image = document.getElementById("profile-img-input");
    let errorImage = document.getElementById("errorImage");
    console.log(errorImage);

    if (!image.files.length) {
        errorImage.innerHTML = "Please upload an image.";
        isValid = false;
    } else {
        let file = image.files[0];
        if (file && !/\.(jpg|jpeg)$/i.test(file.name)) {
            errorImage.innerHTML = "Only JPG or JPEG images are allowed.";
            isValid = false;
        } else {
            errorImage.innerHTML = "";
        }
    }

    return isValid;
}

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

    // Phone Validation
    let phone = document.getElementById("mobile").value;
    let patternPhone = /^[6-9][0-9]{9}$/;
    let errorPhone = document.getElementById("errorMobile");
    console.log(phone);

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

    // Gender Validation
    let gender = document.getElementById("gender").value;
    let errorGender = document.getElementById("errorGender");

    if (
        gender !== "" &&
        gender !== "MALE" &&
        gender !== "FEMALE" &&
        gender !== "OTHER"
    ) {
        errorGender.innerHTML = "Gender is not valid.";
        isValid = false;
    } else {
        errorGender.innerHTML = "";
    }

    return isValid;
}