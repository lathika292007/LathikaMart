document.addEventListener('DOMContentLoaded', () => {
    loadProfile();

    const profileForm = document.getElementById('profileForm');
    if (profileForm) {
        profileForm.addEventListener('submit', handleProfileSubmit);
    }
});

async function loadProfile() {
    try {
        const response = await fetch('/lathikamart/api/v1/profile');
        const data = await response.json();
        if (data.success && data.data) {
            document.getElementById('profileName').value = data.data.name || '';
            document.getElementById('profileEmail').value = data.data.email || '';
        }
    } catch (e) {
        console.error("Failed to load profile:", e);
    }
}

async function handleProfileSubmit(e) {
    e.preventDefault();
    const alertDiv = document.getElementById('profileAlert');
    alertDiv.innerHTML = '';

    const name = document.getElementById('profileName').value.trim();
    const currentPassword = document.getElementById('currentPassword').value.trim();
    const newPassword = document.getElementById('newPassword').value.trim();

    try {
        const response = await fetch('/lathikamart/api/v1/profile', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, currentPassword, newPassword })
        });
        const data = await response.json();
        if (data.success) {
            alertDiv.innerHTML = `<div class="alert alert-success">✅ Profile updated successfully!</div>`;
            document.getElementById('currentPassword').value = '';
            document.getElementById('newPassword').value = '';
        } else {
            alertDiv.innerHTML = `<div class="alert alert-danger">❌ ${data.error ? data.error.message : 'Update failed'}</div>`;
        }
    } catch (e) {
        alertDiv.innerHTML = `<div class="alert alert-danger">❌ Network error updating profile.</div>`;
    }
}
