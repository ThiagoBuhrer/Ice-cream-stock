// ================================================
// APP.JS - Global variables and initialization
// ================================================


// Stores the ID of the ice cream currently being edited
let editingId = null;
// Stores the ID of the ice cream currently being deleted
let deleteTargetId = null;
// Defines how many kilograms are contained in one ice cream bucket
const KG_PER_BUCKET = 5;
// Stores all ice cream data fetched from the API for pagination purposes
let allIceCreams = [];
// Controls the current page index for the "Show More" pagination feature
let currentPage = 0;
// Defines the maximum number of ice cream cards displayed per page
const ITEMS_PER_PAGE = 9;
// API base URL used to fetch all ice cream records from the backend
const API_URL = "/icecream";

// Refreshes the user interface by reloading both the card view and the table view from the backend
function refreshUI() {
    loadCards();
    loadTable();
}

// Executes when the DOM is fully loaded to ensure all HTML elements exist
document.addEventListener("DOMContentLoaded", () => {
    loadCards();
    loadTable();
});
