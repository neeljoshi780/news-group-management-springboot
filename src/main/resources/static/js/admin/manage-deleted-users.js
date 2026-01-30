document.addEventListener("DOMContentLoaded", () => {
    const recoverPopup = document.getElementById("recoverPopup");
    const confirmRecoverBtn = recoverPopup.querySelector(".confirm-btn");
    const cancelBtn = recoverPopup.querySelector(".cancel-btn");
    let currentForm = null;

    // Handle recover button click
    document.querySelectorAll(".btn-recover").forEach((button) => {
        button.addEventListener("click", (e) => {
            e.preventDefault();
            currentForm = button.closest("form");
            recoverPopup.style.display = "flex";
        });
    });

    // Handle recover confirmation
    confirmRecoverBtn.addEventListener("click", () => {
        if (currentForm) {
            currentForm.submit();
        }
        recoverPopup.style.display = "none";
        currentForm = null;
    });

    // Handle cancellation
    cancelBtn.addEventListener("click", (e) => {
        e.preventDefault();
        recoverPopup.style.display = "none";
        currentForm = null;
    });

    // Close popup if clicked outside
    recoverPopup.addEventListener("click", (e) => {
        if (e.target === recoverPopup) {
            recoverPopup.style.display = "none";
            currentForm = null;
        }
    });
});
