// ================================================
// SELL.JS - Sell ice cream cups
// ================================================


// Opens sell offcanvas and loads flavors
function openSellOffcanvas() {
    loadFlavorsForSell();
    const offcanvas = new bootstrap.Offcanvas(
        document.getElementById('sellOffcanvas')
    );
    offcanvas.show();
}

// Fetches flavors and populates dropdown, disables those with 0 stock
function loadFlavorsForSell() {
    fetch("/icecream")
        .then(response => response.json())
        .then(data => {
            const select = document.getElementById("sellFlavor");
            select.innerHTML = '<option value="">Select a flavor...</option>';
            
            data.forEach(iceCream => {
                const option = document.createElement("option");
                option.value = iceCream.flavor;
                option.textContent = iceCream.flavor + " (" + iceCream.stockQuantityKG + " KG)";
                
                if (iceCream.stockQuantityKG <= 0) {
                    option.disabled = true;
                    option.style.color = "gray";
                }
                
                select.appendChild(option);
            });
        })
        .catch(error => {
            console.error("Error loading flavors:", error);
        });
}

// Processes sell request, validates inputs, shows modals for success/error
function sellFromOffcanvas(event) {
    event.preventDefault();

    const flavor = document.getElementById("sellFlavor").value;
    const cups = document.getElementById("sellCups").value;

    if (!flavor) {
        showSellError("Please select a flavor.");
        return;
    }

    if (!cups || cups <= 0) {
        showSellError("Please enter a valid number of cups.");
        return;
    }

    fetch(`/sell?flavor=${flavor}&cups=${cups}`, {
        method: "POST"
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text);
            });
        }
        return response.text();
    })
    .then(data => {
        showSellSuccess(data);
        refreshUI();
        const offcanvas = bootstrap.Offcanvas.getInstance(document.getElementById('sellOffcanvas'));
        if (offcanvas) offcanvas.hide();
    })
    .catch(err => {
        showSellError(err.message);
    });
}

// Shows error modal
function showSellError(message) {
    document.getElementById("sellErrorMessage").textContent = message;
    new bootstrap.Modal(document.getElementById("sellErrorModal")).show();
}

// Shows success modal
function showSellSuccess(message) {
    document.getElementById("sellSuccessMessage").textContent = message;
    new bootstrap.Modal(document.getElementById("sellSuccessModal")).show();
}
