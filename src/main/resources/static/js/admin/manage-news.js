document.addEventListener("DOMContentLoaded", () => {
    const deletePopup = document.getElementById("deletePopup");
    const confirmDeleteBtn = document.querySelector("#deletePopup .confirm-btn");
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

    // Handle delete confirmation
    confirmDeleteBtn.addEventListener("click", () => {
        if (currentForm) {
            currentForm.submit();
        }
        deletePopup.style.display = "none";
        currentForm = null;
    });

    // Handle cancellation
    cancelBtns.forEach((btn) => {
        btn.addEventListener("click", (e) => {
            e.preventDefault();
            deletePopup.style.display = "none";
            currentForm = null;
        });
    });

    // Close popup if clicked outside
    deletePopup.addEventListener("click", (e) => {
        if (e.target === deletePopup) {
            deletePopup.style.display = "none";
            currentForm = null;
        }
    });
});