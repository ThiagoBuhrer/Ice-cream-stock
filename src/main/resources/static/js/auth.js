// ================================================
// AUTH.JS - User session and authentication
// ================================================


// Loads logged user information
window.onload = function () {

    const username = localStorage.getItem("username");
    const role = localStorage.getItem("role");

    if (username) {
        document.getElementById("loggedUser").innerText = username;
        document.getElementById("welcomeUser").innerText = username;
    }

    if (role === "STAFF") {

        document.getElementById("addButton").style.display = "none";
        document.getElementById("deleteAllButton").style.display = "none";

        document.querySelectorAll(".deleteButton")
            .forEach(button => {

                button.style.display = "none";

            });
    }

};

// Removes user data and returns to login page
function logout() {
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    window.location.href = "/login.html";
}
