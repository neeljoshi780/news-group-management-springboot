function validate() {
    let isValid = true;

    // Category Validation
    let category = document.getElementById("news-category").value;
    let errorCategory = document.getElementById("errorCategoryId");

    if (category === "") {
        errorCategory.innerHTML = newsShareErrorMessages.categoryRequired;
        isValid = false;
    } else {
        errorCategory.innerHTML = "";
    }

    // Title Validation
    let title = document.getElementById("news-title").value;
    let errorTitle = document.getElementById("errorTitle");
    let patternTitle =
        /^(?=.{2,500}$)[a-zA-Z0-9][a-zA-Z0-9.,:;'"$%&?!()\-–—]*(?: (?! )[a-zA-Z0-9.,:;'"$%&?!()\-–—]+){0,100}$/;

    if (title === "") {
        errorTitle.innerHTML = newsShareErrorMessages.titleRequired;
        isValid = false;
    } else if (patternTitle.test(title) === false) {
        errorTitle.innerHTML = newsShareErrorMessages.titleInvalid;
        isValid = false;
    } else {
        errorTitle.innerHTML = "";
    }

    // Description Validation
    let description = document.getElementById("news-description").value;
    let errorDescription = document.getElementById("errorDescription");
    let patternDescription =
        /^(?=.{2,1000000}$)[a-zA-Z0-9][a-zA-Z0-9.,:;'"$%&?!()\-–—]*(?: (?! )[a-zA-Z0-9.,:;'"$%&?!()\-–—]+){0,1000}$/;

    if (description === "") {
        errorDescription.innerHTML = newsShareErrorMessages.descriptionRequired;
        isValid = false;
    } else if (patternDescription.test(description) === false) {
        errorDescription.innerHTML = newsShareErrorMessages.descriptionInvalid;
        isValid = false;
    } else {
        errorDescription.innerHTML = "";
    }

    // Image Validation
    let image = document.getElementById("news-image");
    let errorImage = document.getElementById("errorFile");

    if (!image.files.length) {
        errorImage.innerHTML = newsShareErrorMessages.imageRequired;
        isValid = false;
    } else {
        let file = image.files[0];
        if (!/\.(jpg|jpeg|png)$/i.test(file.name)) {
            errorImage.innerHTML = newsShareErrorMessages.imageType;
            isValid = false;
        } else if (file.size > 2 * 1024 * 1024) {
            errorImage.innerHTML = newsShareErrorMessages.imageSize;
            isValid = false;
        } else {
            errorImage.innerHTML = "";
        }
    }

    return isValid;
}