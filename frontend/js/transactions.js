if (!getToken()) {
    window.location.href = 'login.html';
}

document.getElementById('logoutBtn').addEventListener('click', function () {
    localStorage.removeItem('token');
    window.location.href = 'login.html';
});

let accountsCache = [];
let categoriesCache = [];

async function loadDropdowns() {
    accountsCache = await apiRequest('/accounts');
    categoriesCache = await apiRequest('/categories');

    const accountSelect = document.getElementById('accountSelect');
    accountSelect.innerHTML = accountsCache
        .map(acc => `<option value="${acc.id}">${acc.name}</option>`)
        .join('');

    const categorySelect = document.getElementById('categorySelect');
    const filterCategory = document.getElementById('filterCategory');
    const categoryOptions = categoriesCache
        .map(cat => `<option value="${cat.id}">${cat.name}</option>`)
        .join('');

    categorySelect.innerHTML = categoryOptions;
    filterCategory.innerHTML = '<option value="">All</option>' + categoryOptions;
}

async function loadTransactions(categoryId = '') {
    let endpoint = '/transactions?page=0&size=20';
    if (categoryId) {
        endpoint += `&categoryId=${categoryId}`;
    }

    const data = await apiRequest(endpoint);
    const list = document.getElementById('transactionsList');

    if (data.content.length === 0) {
        list.innerHTML = '<p>No transactions found.</p>';
        return;
    }

    list.innerHTML = data.content.map(t => `
        <div class="transaction-row">
            <span>${t.date} — ${t.description || '(no description)'}</span>
            <span>${t.accountName} / ${t.categoryName}</span>
            <span class="amount ${t.type.toLowerCase()}">
                ${t.type === 'INCOME' ? '+' : '-'}₹${t.amount.toFixed(2)}
            </span>
        </div>
    `).join('');
}

document.getElementById('transactionForm').addEventListener('submit', async function (event) {
    event.preventDefault();
    const errorEl = document.getElementById('formError');
    errorEl.textContent = '';

    const payload = {
        amount: parseFloat(document.getElementById('amount').value),
        type: document.getElementById('type').value,
        date: document.getElementById('date').value,
        description: document.getElementById('description').value,
        accountId: parseInt(document.getElementById('accountSelect').value),
        categoryId: parseInt(document.getElementById('categorySelect').value)
    };

    try {
        await apiRequest('/transactions', 'POST', payload);
        document.getElementById('transactionForm').reset();
        loadTransactions();
    } catch (error) {
        errorEl.textContent = error.message;
    }
});

document.getElementById('applyFilter').addEventListener('click', function () {
    const categoryId = document.getElementById('filterCategory').value;
    loadTransactions(categoryId);
});

async function init() {
    await loadDropdowns();
    await loadTransactions();
}

init();