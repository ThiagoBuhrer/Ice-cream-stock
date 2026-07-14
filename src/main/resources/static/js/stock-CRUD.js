// ================================================
// STOCK-CRUD.JS - Create, Read, Update and Delete operations
// ================================================


// Opens Bootstrap Offcanvas for "Add New Ice Cream" Form
function openAddOffcanvas() {
    editingId = null;
    setCreateModeUI();
    clearForm();

    const el = document.getElementById("addOffcanvas");
    const offcanvas = new bootstrap.Offcanvas(el);
    offcanvas.show();
}

// Calculates Stock (KG) based on bucket input live
function updateStockLive() {
    const buckets = parseInt(document.getElementById("addBuckets").value) || 0;
    const kg = buckets * KG_PER_BUCKET;

    document.getElementById("calculatedStock").innerText = kg;
}

// Sends new ice cream data to backend API (POST/PUT)
function saveIceCream(event) {
    event.preventDefault();

    const flavor = document.getElementById("addFlavor").value.trim();
    const buckets = parseInt(document.getElementById("addBuckets").value) || 0;
    const kg = buckets * KG_PER_BUCKET;
    const madeAt = document.getElementById("addDate").value;

    if (!madeAt) {
        showDateError();
        return;
    }

    document.getElementById("addError").style.display = "none";
    document.getElementById("addError").textContent = "";

    const iceCream = {
        flavor: flavor,
        stockBuckets: buckets,
        stockQuantityKG: kg,
        madeAt: madeAt,
        icon: document.getElementById("selectedIcon").value || "blank"
    };

    // EDIT MODE (PUT)
    if (editingId !== null) {
        fetch(`/icecream/${editingId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(iceCream)
        })
        .then(r => {
            if (!r.ok) return r.text().then(e => { throw new Error(e); });
            return r.json();
        })
        .then(() => {
            editingId = null;
            setCreateModeUI();
            clearForm();
            refreshUI();
            // Close the offcanvas after successful update
            const offcanvas = bootstrap.Offcanvas.getInstance(document.getElementById('addOffcanvas'));
            if (offcanvas) {
                offcanvas.hide();
            }
        })
        .catch(err => alert("Error: " + err.message));
        return;
    }

    // CREATE MODE (POST)
    console.log("Sending iceCream:", iceCream);
    fetch("/icecream")
        .then(r => r.json())
        .then(list => {
            const exists = list.some(i => i.flavor.toLowerCase() === flavor.toLowerCase());
            if (exists) {
                showDuplicateFlavorError(flavor);
                return;
            }
            return fetch("/icecream", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(iceCream)
            });
        })
        .then(r => {
            if (!r) return;
            if (!r.ok) return r.text().then(e => { throw new Error(e); });
            return r.json();
        })
        .then(() => {
            clearForm();
            refreshUI();
            // Close the offcanvas after successful create
            const offcanvas = bootstrap.Offcanvas.getInstance(document.getElementById('addOffcanvas'));
            if (offcanvas) {
                offcanvas.hide();
            }
        })
        .catch(err => alert("Error: " + err.message));
}

// Resets form
function clearForm() {
    document.getElementById("addFlavor").value = "";
    document.getElementById("addBuckets").value = "";
    document.getElementById("addDate").value = "";
    document.getElementById("calculatedStock").innerText = "0";

    // Reset icon to blank
    document.getElementById("selectedIcon").value = "blank";
    document.querySelectorAll(".icon-option").forEach(opt => opt.classList.remove("selected"));
    document.querySelector(".icon-option[data-icon='blank']").classList.add("selected");
}

// Resets the UI to "create mode" by changing the main action button text back to "Create"
function setCreateModeUI() {
    document.querySelector("#addOffcanvas .btn-success").innerText = "Create";
}

// Changes the UI to "edit mode" by updating the main action button text to "Update"
function setEditModeUI() {
    document.querySelector("#addOffcanvas .btn-success").innerText = "Update";
}

// Fetches ice cream data from the backend API and renders it into an HTML table (READ)
function loadTable() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {

            const tableBody = document.getElementById("tableBody");

            // Clears existing table content before inserting updated rows
            tableBody.innerHTML = "";

            // Iterates over each ice cream object returned by the API
            data.forEach(iceCream => {

                // Dynamically creates a table row for each ice cream entry
                // Delete button only works if you are logged as Manager, not as Staff. Edit button works for both roles.
                tableBody.innerHTML += `
                    <tr>
                        <td>${iceCream.id ?? "-"}</td>
                        <td>${iceCream.flavor}</td>
                        <td>${iceCream.stockQuantityKG}</td>
                        <td>${iceCream.stockBuckets}</td>
                        <td>

                            <button class="btn btn-warning btn-sm"
                                onclick="startEditIceCream(${iceCream.id}, '${iceCream.flavor}', 
                                ${iceCream.stockBuckets}, '${iceCream.madeAt}', '${iceCream.icon || 'blank'}')">
                                Edit
                            </button>

                            ${localStorage.getItem("role") === "MANAGER" ? `
                            <button class="btn btn-danger btn-sm deleteButton"
                                onclick="deleteIceCream(${iceCream.id})">
                                Delete
                            </button>
                            ` : ""}

                        </td>
                    </tr>
                `;

            });

        })
        .catch(error => {
            console.error("Error loading table:", error);
        });
}

// Sends DELETE request to the backend to remove an ice cream by its ID
function deleteIceCream(id) {
    deleteTargetId = id;

    const modal = new bootstrap.Modal(document.getElementById("deleteModal"));
    modal.show();
}

// Deletes the selected ice cream
document.addEventListener("DOMContentLoaded", function () {

// Waits for DOM to load, then handles delete confirmation by sending a DELETE request to the backend.
// On success, it closes the modal, resets state, and refreshes the UI.

    document.getElementById("confirmDeleteBtn").addEventListener("click", function () {

        if (deleteTargetId === null) return;

        fetch(`/icecream/${deleteTargetId}`, {
            method: "DELETE"
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(err => { throw new Error(err); });
            }
            return response.text();
        })
        .then(() => {
            deleteTargetId = null;

            const modalEl = document.getElementById("deleteModal");
            const modal = bootstrap.Modal.getInstance(modalEl);
            modal.hide();

            refreshUI();
        })
        .catch(err => alert("Error: " + err.message));

    });
});

// Opens the edit form (offcanvas) and pre-fills it with the selected ice cream data
function startEditIceCream(id, flavor, buckets, madeAt, icon) {
    editingId = id;

    document.getElementById("addFlavor").value = flavor;
    document.getElementById("addBuckets").value = buckets;
    document.getElementById("addDate").value = madeAt;

    const currentIcon = icon || "blank";
    document.getElementById("selectedIcon").value = currentIcon;
    document.querySelectorAll(".icon-option").forEach(opt => {
        opt.classList.remove("selected");
        if (opt.dataset.icon === currentIcon) {
            opt.classList.add("selected");
        }
    });

    updateStockLive();
    setEditModeUI();

    const offcanvas = new bootstrap.Offcanvas(
        document.getElementById("addOffcanvas")
    );
    offcanvas.show();
}

// Deletes all ice cream records from the database
function deleteAllIceCreams() {

    const confirmModal = new bootstrap.Modal(
        document.getElementById("deleteAllModal")
    );

    confirmModal.show();
}

// Opens confirmation modal before deleting all ice creams
function openDeleteAllModal() {

    const modal = new bootstrap.Modal(
        document.getElementById("deleteAllModal")
    );

    modal.show();
}

// Confirms deleting all ice cream records
document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("confirmDeleteAllBtn")
    .addEventListener("click", function () {

        fetch("/icecream/all", {
            method: "DELETE"
        })
        .then(response => {

            return response.text().then(message => {

                if (!response.ok) {
                    throw new Error(message);
                }

                return message;

            });

        })
        .then(message => {

            const modal = bootstrap.Modal.getInstance(
                document.getElementById("deleteAllModal")
            );

            modal.hide();


            document.getElementById("successModalMessage").innerText = message;


            const successModal = new bootstrap.Modal(
                document.getElementById("successModal")
            );

            successModal.show();


            refreshUI();

        })
        .catch(error => {

            document.getElementById("successModalMessage").innerText =
                error.message;


            const successModal = new bootstrap.Modal(
                document.getElementById("successModal")
            );

            successModal.show();

        });

    });

});

// Displays an error message when user tries to create an ice cream that already exists
function showDuplicateFlavorError(flavor) {
    const el = document.getElementById("addError");
    el.textContent = "Flavor '" + flavor + "' already exists! Try again.";
    el.style.display = "block";
}

// Displays message indicating the user missed the field "Created at: " when creating new ice cream
function showDateError() {
    new bootstrap.Modal(document.getElementById("dateErrorModal")).show();
}
