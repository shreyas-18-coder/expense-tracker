// Route protection: if no token, kick back to login
if (!getToken()) {
    window.location.href = 'login.html';
}

// Logout
document.getElementById('logoutBtn').addEventListener('click', function () {
    localStorage.removeItem('token');
    window.location.href = 'login.html';
});

// Load dashboard data
async function loadDashboard() {
    const now = new Date();
    const month = now.getMonth() + 1; // JS months are 0-indexed
    const year = now.getFullYear();

    try {
        // Load summary
        const summary = await apiRequest(`/analytics/summary?month=${month}&year=${year}`);
        document.getElementById('totalIncome').textContent = `₹${summary.totalIncome.toFixed(2)}`;
        document.getElementById('totalExpense').textContent = `₹${summary.totalExpense.toFixed(2)}`;

        // Load accounts
        const accounts = await apiRequest('/accounts');
        let totalBalance = 0;
        let accountsHtml = '';

        accounts.forEach(account => {
            totalBalance += account.balance;
            accountsHtml += `
                <div class="account-item">
                    <span>${account.name} (${account.type})</span>
                    <span>₹${account.balance.toFixed(2)}</span>
                </div>
            `;
        });

        document.getElementById('totalBalance').textContent = `₹${totalBalance.toFixed(2)}`;
        document.getElementById('accountsList').innerHTML = accountsHtml || '<p>No accounts yet.</p>';

    } catch (error) {
        console.error('Failed to load dashboard:', error);
        if (error.message.includes('403') || error.message.includes('401')) {
            localStorage.removeItem('token');
            window.location.href = 'login.html';
        }
    }
}
document.getElementById('addAccountForm').addEventListener('submit', async function (event) {
    event.preventDefault();
    const errorEl = document.getElementById('accountFormError');
    errorEl.textContent = '';

    const name = document.getElementById('accountName').value.trim();
    const type = document.getElementById('accountType').value;
    const balance = parseFloat(document.getElementById('accountBalance').value) || 0;

    try {
        await apiRequest('/accounts', 'POST', { name, type, balance });

        document.getElementById('accountName').value = '';
        document.getElementById('accountBalance').value = '0';

        loadDashboard();

    } catch (error) {
        errorEl.textContent = error.message;
    }
});

loadDashboard();