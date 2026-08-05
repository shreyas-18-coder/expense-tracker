if (!getToken()) {
    window.location.href = 'login.html';
}

document.getElementById('logoutBtn').addEventListener('click', function () {
    localStorage.removeItem('token');
    window.location.href = 'login.html';
});

async function loadAccounts() {
    const accounts = await apiRequest('/accounts');
    document.getElementById('accountSelect').innerHTML = accounts
        .map(acc => `<option value="${acc.id}">${acc.name}</option>`)
        .join('');
}

document.getElementById('importForm').addEventListener('submit', async function (event) {
    event.preventDefault();
    const errorEl = document.getElementById('formError');
    errorEl.textContent = '';

    const accountId = document.getElementById('accountSelect').value;
    const fileInput = document.getElementById('csvFile');
    const file = fileInput.files[0];

    if (!file) {
        errorEl.textContent = 'Please select a file';
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch(
            `http://localhost:8080/api/import/csv?accountId=${accountId}`,
            {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${getToken()}`
                },
                body: formData
            }
        );

        if (!response.ok) {
            const errData = await response.json();
            throw new Error(errData.message || 'Import failed');
        }

        const result = await response.json();
        displayResult(result);
        fileInput.value = '';

    } catch (error) {
        errorEl.textContent = error.message;
    }
});

function displayResult(result) {
    const section = document.getElementById('resultSection');
    section.innerHTML = `
        <div class="result-box">
            <h3>Import Complete</h3>
            <div class="result-stats">
                <span>Total Rows: ${result.totalRows}</span>
                <span style="color: #16a34a;">Success: ${result.successCount}</span>
                <span style="color: #dc2626;">Failed: ${result.failedCount}</span>
            </div>
            ${result.errors.length > 0 ? `
                <div class="error-list">
                    <strong>Errors:</strong>
                    <ul>${result.errors.map(e => `<li>${e}</li>`).join('')}</ul>
                </div>
            ` : ''}
        </div>
    `;
}

loadAccounts();