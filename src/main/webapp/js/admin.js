/* LathikaMart Admin Governance Client Logic */

document.addEventListener('DOMContentLoaded', () => {
    loadStats();
    loadUsers();
    loadListings();
    loadAuditLogs();
});

async function loadStats() {
    try {
        const res = await fetch(`${window.contextPath}/api/v1/admin/stats`);
        const data = await res.json();
        if (data.success && data.data) {
            document.getElementById('stat-users').textContent = data.data.totalUsers || 0;
            document.getElementById('stat-sellers').textContent = data.data.totalSellers || 0;
            document.getElementById('stat-products').textContent = data.data.totalProducts || 0;
            document.getElementById('stat-orders').textContent = data.data.totalOrders || 0;
            document.getElementById('stat-revenue').textContent = '$' + (parseFloat(data.data.totalRevenue) || 0).toFixed(2);
        }
    } catch (e) {}
}

async function loadUsers() {
    const container = document.getElementById('users-table-container');
    if (!container) return;

    try {
        const res = await fetch(`${window.contextPath}/api/v1/admin/users`);
        const data = await res.json();
        const users = data.data || [];

        container.innerHTML = `
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Current Role</th>
                        <th>Registered</th>
                        <th>Promote / Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${users.map(u => `
                        <tr>
                            <td>#${u.id}</td>
                            <td><strong>${escapeHtml(u.name)}</strong></td>
                            <td>${escapeHtml(u.email)}</td>
                            <td>
                                <span class="badge" style="background:${getRoleColor(u.role).bg}; color:${getRoleColor(u.role).color}; padding:0.25rem 0.5rem; border-radius:6px; font-weight:700;">
                                    ${u.role}
                                </span>
                            </td>
                            <td>${formatDate(u.createdAt)}</td>
                            <td>
                                ${u.role === 'BUYER' ? `
                                    <button class="btn btn-secondary" style="padding:0.2rem 0.5rem; font-size:0.75rem;" onclick="promoteUser(${u.id}, 'SELLER')">
                                        ⬆️ Make Seller
                                    </button>
                                ` : ''}
                                ${u.role !== 'ADMIN' ? `
                                    <button class="btn btn-primary" style="padding:0.2rem 0.5rem; font-size:0.75rem;" onclick="promoteUser(${u.id}, 'ADMIN')">
                                        👑 Make Admin
                                    </button>
                                ` : '<span class="text-muted" style="font-size:0.8rem;">System Admin</span>'}
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>`;
    } catch (e) {
        container.innerHTML = 'Error loading users.';
    }
}

async function promoteUser(userId, newRole) {
    if (!confirm(`Admin Action: Promote user #${userId} to ${newRole}?`)) return;
    try {
        const res = await fetch(`${window.contextPath}/api/v1/admin/users/${userId}/role`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ role: newRole })
        });
        const data = await res.json();
        if (data.success) {
            loadUsers();
            loadStats();
            loadAuditLogs();
        } else {
            alert('Error promoting user: ' + (data.error ? data.error.message : 'Failed'));
        }
    } catch (e) {
        alert('Request failed');
    }
}

async function loadListings() {
    const container = document.getElementById('products-table-container');
    if (!container) return;

    try {
        const res = await fetch(`${window.contextPath}/api/v1/products`);
        const data = await res.json();
        const products = data.data || [];

        container.innerHTML = `
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Seller ID</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    ${products.map(p => `
                        <tr>
                            <td>#${p.id}</td>
                            <td><strong>${escapeHtml(p.name)}</strong></td>
                            <td>${escapeHtml(p.category)}</td>
                            <td>$${parseFloat(p.price).toFixed(2)}</td>
                            <td>Seller #${p.sellerId}</td>
                            <td>
                                <button class="btn btn-danger" style="padding:0.25rem 0.5rem; font-size:0.8rem;" 
                                        onclick="moderateRemove(${p.id})">
                                    🗑️ Moderation Delete
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>`;
    } catch (e) {
        container.innerHTML = 'Error loading listings.';
    }
}

async function moderateRemove(id) {
    if (!confirm('Admin Moderation Action: Are you sure you want to delete product listing #' + id + '?')) return;
    try {
        const res = await fetch(`${window.contextPath}/api/v1/admin/products/${id}`, { method: 'DELETE' });
        const data = await res.json();
        if (data.success) {
            loadListings();
            loadStats();
            loadAuditLogs();
        } else {
            alert('Error removing listing: ' + (data.error ? data.error.message : 'Failed'));
        }
    } catch (e) {
        alert('Request failed');
    }
}

async function loadAuditLogs() {
    const container = document.getElementById('audit-table-container');
    if (!container) return;

    try {
        const res = await fetch(`${window.contextPath}/api/v1/admin/audit-logs`);
        const data = await res.json();
        const logs = data.data || [];

        if (logs.length === 0) {
            container.innerHTML = '<div class="text-muted" style="padding:1rem; text-align:center;">No audit log records found.</div>';
            return;
        }

        container.innerHTML = `
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Log ID</th>
                        <th>Admin Email</th>
                        <th>Action</th>
                        <th>Target</th>
                        <th>Details</th>
                        <th>Timestamp</th>
                    </tr>
                </thead>
                <tbody>
                    ${logs.map(l => `
                        <tr>
                            <td>#${l.id}</td>
                            <td><strong>${escapeHtml(l.adminEmail)}</strong></td>
                            <td><span class="badge" style="background:rgba(234,179,8,0.2); color:#eab308; font-weight:700;">${l.action}</span></td>
                            <td>${l.targetType} #${l.targetId}</td>
                            <td style="font-size:0.85rem;">${escapeHtml(l.details)}</td>
                            <td style="font-size:0.8rem; color:#94a3b8;">${formatDate(l.createdAt)}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>`;
    } catch (e) {
        container.innerHTML = 'Error loading audit logs.';
    }
}

function getRoleColor(role) {
    switch(role) {
        case 'ADMIN': return { bg: 'rgba(239,68,68,0.2)', color: '#ef4444' };
        case 'SELLER': return { bg: 'rgba(168,85,247,0.2)', color: '#a855f7' };
        default: return { bg: 'rgba(59,130,246,0.2)', color: '#3b82f6' };
    }
}

function formatDate(ts) {
    if (!ts) return 'N/A';
    return new Date(ts).toLocaleString();
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}
