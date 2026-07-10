// ================================================
// CARDS.JS - Card rendering and pagination
// ================================================


// Fetches ice cream data from backend API and renders items as UI cards
function loadCards() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {
            allIceCreams = data;
            currentPage = 0;
            renderCards();
            updateShowMoreButton();
        })
        .catch(error => {
            console.error(error);
        });
}

function renderCards() {
    const cardsContainer = document.getElementById("cardsContainer");
    cardsContainer.innerHTML = "";

    const start = 0;
    const end = Math.min((currentPage + 1) * ITEMS_PER_PAGE, allIceCreams.length);
    const pageItems = allIceCreams.slice(start, end);

    pageItems.forEach(iceCream => {
        const iconName = iceCream.icon || "blank";
        cardsContainer.innerHTML += `
            <div class="col-12 col-md-4 text-center">
                <div class="card shadow-sm p-3">
                    <h5>${iceCream.flavor}</h5>
                    <p>${iceCream.stockQuantityKG} KG</p>
                    <p>${iceCream.stockBuckets} Buckets</p>
                    <img src="images/${iconName}.png" alt="${iconName}" class="card-icon">
                </div>
            </div>
        `;
    });

    updateShowMoreButton();
}

function updateShowMoreButton() {
    const button = document.getElementById("showMoreButton");
    const totalItems = allIceCreams.length;
    const displayedItems = (currentPage + 1) * ITEMS_PER_PAGE;

    if (displayedItems >= totalItems && currentPage > 0) {
        button.textContent = "↑ Show less";
        button.disabled = false;
        button.style.opacity = "1";
        button.style.cursor = "pointer";
    } else if (displayedItems >= totalItems && currentPage === 0) {
        button.textContent = "No more items";
        button.disabled = true;
        button.style.opacity = "0.5";
        button.style.cursor = "not-allowed";
    } else {
        button.textContent = "↓ Show more";
        button.disabled = false;
        button.style.opacity = "1";
        button.style.cursor = "pointer";
    }
}

function handleButtonClick() {
    const button = document.getElementById("showMoreButton");
    const text = button.textContent;
    
    if (text === "↓ Show more") {
        showMore();
    } else if (text === "↑ Show less") {
        resetCards();
    }
}

function showMore() {
    const totalItems = allIceCreams.length;
    const displayedItems = (currentPage + 1) * ITEMS_PER_PAGE;

    if (displayedItems < totalItems) {
        currentPage++;
        renderCards();
    }
}

function resetCards() {
    currentPage = 0;
    renderCards();
}