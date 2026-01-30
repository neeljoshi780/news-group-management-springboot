document.addEventListener("DOMContentLoaded", () => {
    const deletePopup = document.getElementById("deletePopup");
    const blockPopup = document.getElementById("blockPopup");
    const confirmDeleteBtn = document.querySelector("#deletePopup .confirm-btn");
    const confirmBlockBtn = document.querySelector("#blockPopup .confirm-btn");
    const cancelBtns = document.querySelectorAll(".cancel-btn");
    let currentForm = null;

    // Handle delete button click
    document.querySelectorAll(".btn-delete").forEach((button) => {
        button.addEventListener("click", (e) => {
            e.preventDefault();
            currentForm = button.closest("form");
            deletePopup.style.display = "flex";
        });
    });

    // Handle block button click
    document.querySelectorAll(".btn-block").forEach((button) => {
        button.addEventListener("click", (e) => {
            e.preventDefault();
            currentForm = button.closest("form");
            blockPopup.style.display = "flex";
        });
    });

    // Handle delete confirmation
    confirmDeleteBtn.addEventListener("click", () => {
        if (currentForm) {
            currentForm.submit();
        }
        deletePopup.style.display = "none";
    });

    // Handle block confirmation
    confirmBlockBtn.addEventListener("click", () => {
        if (currentForm) {
            currentForm.submit();
        }
        blockPopup.style.display = "none";
    });

    // Handle cancellation for both popups
    cancelBtns.forEach((btn) => {
        btn.addEventListener("click", (e) => {
            e.preventDefault(); // Prevent any default form submission or page refresh
            deletePopup.style.display = "none";
            blockPopup.style.display = "none";
            currentForm = null;
        });
    });

    // Close popup if clicked outside
    [deletePopup, blockPopup].forEach((popup) => {
        popup.addEventListener("click", (e) => {
            if (e.target === popup) {
                popup.style.display = "none";
                currentForm = null;
            }
        });
    });
});