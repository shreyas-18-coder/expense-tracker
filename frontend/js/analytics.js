if (!getToken()) {
    window.location.href = 'login.html';
}

document.getElementById('logoutBtn').addEventListener('click', function () {
    localStorage.removeItem('token');
    window.location.href = 'login.html';
});

const monthNames = ['January','February','March','April','May','June',
    'July','August','September','October','November','December'];

function populateMonthDropdown() {
    const viewMonth = document.getElementById('viewMonth');
    const currentMonth = new Date().getMonth() + 1;

    monthNames.forEach((name, index) => {
        const monthNum = index + 1;
        viewMonth.innerHTML += `<option value="${monthNum}" ${monthNum === currentMonth ? 'selected' : ''}>${name}</option>`;
    });
}

let categoryChartInstance = null;
let trendChartInstance = null;

async function loadAnalytics() {
    const month = document.getElementById('viewMonth').value;
    const year = document.getElementById('viewYear').value;

    // Summary cards
    const summary = await apiRequest(`/analytics/summary?month=${month}&year=${year}`);
    document.getElementById('totalIncome').textContent = `₹${summary.totalIncome.toFixed(2)}`;
    document.getElementById('totalExpense').textContent = `₹${summary.totalExpense.toFixed(2)}`;
    document.getElementById('netBalance').textContent = `₹${summary.netBalance.toFixed(2)}`;

    // Category breakdown (pie chart)
    const breakdown = await apiRequest(`/analytics/category-breakdown?month=${month}&year=${year}`);

    if (categoryChartInstance) categoryChartInstance.destroy();

    categoryChartInstance = new Chart(document.getElementById('categoryChart'), {
        type: 'pie',
        data: {
            labels: breakdown.map(b => b.categoryName),
            datasets: [{
                data: breakdown.map(b => b.totalSpent),
                backgroundColor: ['#2563eb', '#dc2626', '#16a34a', '#f59e0b', '#8b5cf6', '#ec4899']
            }]
        }
    });

    // Monthly trend (line chart)
    const trend = await apiRequest('/analytics/trend');

    if (trendChartInstance) trendChartInstance.destroy();

    trendChartInstance = new Chart(document.getElementById('trendChart'), {
        type: 'line',
        data: {
            labels: trend.map(t => `${monthNames[t.month - 1].substring(0, 3)} ${t.year}`),
            datasets: [{
                label: 'Total Spent',
                data: trend.map(t => t.totalSpent),
                borderColor: '#2563eb',
                backgroundColor: 'rgba(37, 99, 235, 0.1)',
                fill: true,
                tension: 0.3
            }]
        }
    });
}

document.getElementById('loadAnalytics').addEventListener('click', loadAnalytics);

populateMonthDropdown();
loadAnalytics();