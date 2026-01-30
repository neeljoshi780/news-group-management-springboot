function validate() {
    let isValid = true;

    // CategoryName Validation
    let category = document.getElementById("categoryName").value;
    let patternCategory = /^[A-Za-z]{1,50}$/;
    let errorCategory = document.getElementById("errorCategoryName");

    if (category === "") {
        errorCategory.innerHTML = "Category name is required.";
        isValid = false;
    } else if (patternCategory.test(category) === false) {
        errorCategory.innerHTML = "Category name is not valid.";
        isValid = false;
    } else {
        errorCategory.innerHTML = "";
    }

    // Description Validation
    let description = document.getElementById("description").value;
    let patternDescription = /^[A-Za-z]{1,500}$/;
    let errorDescription = document.getElementById("errorDescription");

    if (description!=="" && patternDescription.test(description) === false) {
        errorDescription.innerHTML = "Description name is not valid.";
        isValid = false;
    } else {
        errorDescription.innerHTML = "";
    }

    return isValid;
}