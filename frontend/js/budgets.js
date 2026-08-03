if (!getToken()) {
    window.location.href = 'login.html';
}

document.getElementById('logoutBtn').addEventListener('click', function () {
    localStorage.removeItem('token');
    window.location.href = 'login.html';
});

const monthNames = ['January','February','March','April','May','June',
    'July','August','September','October','November','December'];

function populateMonthDropdowns() {
    const monthSelect = document.getElementById('monthSelect');
    const viewMonth = document.getElementById('viewMonth');
    const currentMonth = new Date().getMonth() + 1;

    monthNames.forEach((name, index) => {
        const monthNum = index + 1;
        const option1 = `<option value="${monthNum}" ${monthNum === currentMonth ? 'selected' : ''}>${name}</option>`;
        monthSelect.innerHTML += option1;
        viewMonth.innerHTML += option1;
    });
}

async function loadCategories() {
    const categories = await apiRequest('/categories');
    document.getElementById('categorySelect').innerHTML = categories
        .map(cat => `<option value="${cat.id}">${cat.name}</option>`)
        .join('');
}

async function loadBudgets() {
    const month = document.getElementById('viewMonth').value;
    const year = document.getElementById('viewYear').value;

    const budgets = await apiRequest(`/budgets?month=${month}&year=${year}`);
    const list = document.getElementById('budgetsList');

    if (budgets.length === 0) {
        list.innerHTML = '<p>No budgets set for this month.</p>';
        return;
    }

    list.innerHTML = budgets.map(b => {
        const percentUsed = Math.min((b.amountSpent / b.monthlyLimit) * 100, 100);
        const isOverBudget = b.amountSpent > b.monthlyLimit;

        return `
            <div class="budget-card">
                <div class="budget-card-header">
                    <strong>${b.categoryName}</strong>
                    <span>₹${b.amountSpent.toFixed(2)} / ₹${b.monthlyLimit.toFixed(2)}</span>
                </div>
                <div class="progress-bar-bg">
                    <div class="progress-bar-fill ${isOverBudget ? 'over-budget' : ''}"
                         style="width: ${percentUsed}%"></div>
                </div>
                <div class="budget-details">
                    <span>${isOverBudget ? 'Over budget!' : 'Remaining: ₹' + b.remaining.toFixed(2)}</span>
                </div>
            </div>
        `;
    }).join('');
}

document.getElementById('budgetForm').addEventListener('submit', async function (event) {
    event.preventDefault();
    const errorEl = document.getElementById('formError');
    errorEl.textContent = '';

    const payload = {
        categoryId: parseInt(document.getElementById('categorySelect').value),
        monthlyLimit: parseFloat(document.getElementById('monthlyLimit').value),
        month: parseInt(document.getElementById('monthSelect').value),
        year: parseInt(document.getElementById('yearInput').value)
    };

    try {
        await apiRequest('/budgets', 'POST', payload);
        document.getElementById('budgetForm').reset();
        loadBudgets();
    } catch (error) {
        errorEl.textContent = error.message;
    }
});

document.getElementById('loadBudgets').addEventListener('click', loadBudgets);

async function init() {
    populateMonthDropdowns();
    await loadCategories();
    await loadBudgets();
}

init();