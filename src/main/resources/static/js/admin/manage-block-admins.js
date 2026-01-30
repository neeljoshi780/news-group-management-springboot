document.addEventListener("DOMContentLoaded", () => {
    const blockPopup = document.getElementById("blockPopup");
    const confirmUnblockBtn = document.querySelector("#blockPopup .confirm-btn");
    const cancelBtn = document.querySelector("#blockPopup .cancel-btn");
    let currentForm = null;

    // Handle unblock button click
    document.querySelectorAll(".btn-unblock").forEach((button) => {
        button.addEventListener("click", (e) => {
            e.preventDefault();
            currentForm = button.closest("form");
            blockPopup.style.display = "flex";
        });
    });

    // Handle unblock confirmation
    confirmUnblockBtn.addEventListener("click", () => {
        if (currentForm) {
            currentForm.submit();
        }
        blockPopup.style.display = "none";
        currentForm = null;
    });

    // Handle cancellation
    cancelBtn.addEventListener("click", (e) => {
        e.preventDefault();
        blockPopup.style.display = "none";
        currentForm = null;
    });

    // Close popup if clicked outside
    blockPopup.addEventListener("click", (e) => {
        if (e.target === blockPopup) {
            blockPopup.style.display = "none";
            currentForm = null;
        }
    });
});
