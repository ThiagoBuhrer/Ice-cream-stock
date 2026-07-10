// ================================================
// UI.JS - UI interactions and frontend-only features
// ================================================


// Icon selector for Add Ice Cream form
document.addEventListener("DOMContentLoaded", function() {
    const iconOptions = document.querySelectorAll(".icon-option");
    iconOptions.forEach(option => {
        option.addEventListener("click", function() {
            iconOptions.forEach(opt => opt.classList.remove("selected"));
            this.classList.add("selected");
            document.getElementById("selectedIcon").value = this.dataset.icon;
        });
    });
});