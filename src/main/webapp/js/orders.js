/* LathikaMart Orders Client Logic */

let currentMode = 'buyer';

document.addEventListener('DOMContentLoaded', () => loadOrders(currentMode));

function setMode(mode) {
    currentMode = mode;
    const buyerBtn = document.getElementById('mode-buyer');
    const sellerBtn = document.getElementById('mode-seller');
    if (buyerBtn) buyerBtn.className = mode === 'buyer' ? 'btn btn-primary' : 'btn btn-secondary';
    if (sellerBtn) sellerBtn.className = mode === 'seller' ? 'btn btn-primary' : 'btn btn-secondary';
    loadOrders(mode);
}

async function loadOrders(mode) {
    const container = document.getElementById('orders-list');
    if (!container) return;

    try {
        const res = await fetch(`${window.contextPath}/api/v1/orders?mode=${mode}`);
        const data = await res.json();

        if (!data.success) {
            container.innerHTML = `
                <div class="glass-card text-center p-8">
                    <p class="text-gray-400 mb-4">Please log in to view orders.</p>
                    <a href="${window.contextPath}/login.jsp" class="btn btn-primary">Log In Now</a>
                </div>`;
            return;
        }

        const orders = data.data || [];
        if (orders.length === 0) {
            container.innerHTML = '<div class="glass-card text-center p-8"><p>No orders found.</p></div>';
            return;
        }

        container.innerHTML = orders.map(order => `
            <div class="glass-card mb-6" style="margin-bottom:1.5rem;">
                <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid rgba(255,255,255,0.1); padding-bottom:1rem; margin-bottom:1rem;">
                    <div>
                        <span style="font-weight:800; font-size:1.1rem;">Order #${order.id}</span>
                        <span class="text-muted" style="margin-left:1rem; font-size:0.85rem;">
                            ${formatDate(order.createdAt)}
                        </span>
                    </div>
                    <div>
                        <a href="${window.contextPath}/invoice?orderId=${order.id}" target="_blank" class="btn btn-secondary" style="margin-right:0.5rem; padding:0.25rem 0.75rem; font-size:0.85rem; text-decoration:none;">📄 Invoice</a>
                        <span class="badge" style="background:rgba(99,102,241,0.2); color:#818cf8; font-size:0.9rem;">
                            Status: ${order.status}
                        </span>
                        ${mode === 'seller' ? `
                            <select style="margin-left:0.5rem;" class="form-select" onchange="updateStatus(${order.id}, this.value)">
                                <option value="">Update Status...</option>
                                <option value="CONFIRMED" ${order.status === 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                <option value="SHIPPED" ${order.status === 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                <option value="DELIVERED" ${order.status === 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                <option value="CANCELLED" ${order.status === 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                            </select>
                        ` : ''}
                    </div>
                </div>

                <!-- Step-by-Step Order Progress Tracker -->
                ${renderOrderProgress(order.status)}

                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Unit Price</th>
                            <th>Quantity</th>
                            <th>Subtotal</th>
                            ${order.status === 'DELIVERED' && mode === 'buyer' ? '<th>Review</th>' : ''}
                        </tr>
                    </thead>
                    <tbody>
                        ${(order.items || []).map(item => `
                            <tr>
                                <td>${escapeHtml(item.productName || 'Product #' + item.productId)}</td>
                                <td>$${parseFloat(item.unitPrice).toFixed(2)}</td>
                                <td>${item.quantity}</td>
                                <td>$${(parseFloat(item.unitPrice) * item.quantity).toFixed(2)}</td>
                                ${order.status === 'DELIVERED' && mode === 'buyer' ? `
                                    <td>
                                        <button class="btn btn-secondary" style="padding:0.25rem 0.5rem; font-size:0.75rem;" 
                                                onclick="promptReview(${item.productId})">
                                            ⭐ Leave Review
                                        </button>
                                    </td>
                                ` : ''}
                            </tr>
                        `).join('')}
                    </tbody>
                </table>

                <div style="text-align:right; margin-top:1rem; font-weight:800; font-size:1.2rem; color:#38bdf8;">
                    Total Amount: $${parseFloat(order.totalAmount).toFixed(2)}
                </div>
            </div>
        `).join('');

    } catch (e) {
        container.innerHTML = '<div class="glass-card text-center p-8"><p>Error loading orders.</p></div>';
    }
}

async function updateStatus(orderId, status) {
    if (!status) return;
    try {
        const res = await fetch(`${window.contextPath}/api/v1/orders/${orderId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status })
        });
        const data = await res.json();
        if (data.success) {
            loadOrders(currentMode);
        } else {
            alert('Status update failed: ' + (data.error ? data.error.message : 'Invalid transition'));
        }
    } catch (e) {
        alert('Could not update status');
    }
}

async function promptReview(productId) {
    const rating = prompt('Enter star rating (1 to 5):', '5');
    if (!rating) return;
    const comment = prompt('Enter review comment:');
    if (!comment) return;

    try {
        const res = await fetch(`${window.contextPath}/api/v1/reviews`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ productId, rating: parseInt(rating), comment })
        });
        const data = await res.json();
        if (data.success) {
            alert('Thank you for your review!');
        } else {
            alert(data.error ? data.error.message : 'Review submission failed.');
        }
    } catch (e) {
        alert('Review submission failed');
    }
}

function renderOrderProgress(status) {
    if (status === 'CANCELLED') {
        return `
            <div style="background:rgba(239,68,68,0.15); border:1px solid rgba(239,68,68,0.3); border-radius:8px; padding:0.75rem; margin-bottom:1rem; color:#f87171; text-align:center; font-weight:700;">
                ❌ Order Cancelled
            </div>`;
    }

    const steps = [
        { id: 'PENDING', label: '1. Order Placed' },
        { id: 'CONFIRMED', label: '2. Confirmed' },
        { id: 'SHIPPED', label: '3. Shipped' },
        { id: 'DELIVERED', label: '4. Delivered' }
    ];

    const statusOrder = ['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED'];
    const currentIndex = statusOrder.indexOf(status);

    return `
        <div style="display:flex; justify-content:space-between; align-items:center; background:rgba(255,255,255,0.03); border-radius:8px; padding:0.75rem 1.5rem; margin-bottom:1rem; flex-wrap:wrap; gap:0.5rem;">
            ${steps.map((step, idx) => {
                const isActive = idx <= currentIndex;
                return `
                    <div style="display:flex; align-items:center; gap:0.5rem; opacity:${isActive ? '1' : '0.4'};">
                        <span style="background:${isActive ? '#38bdf8' : 'rgba(255,255,255,0.2)'}; color:${isActive ? '#0f172a' : '#fff'}; width:24px; height:24px; border-radius:50%; display:inline-flex; align-items:center; justify-content:center; font-weight:800; font-size:0.75rem;">
                            ${isActive ? '✓' : idx + 1}
                        </span>
                        <span style="font-size:0.85rem; font-weight:${isActive ? '700' : '400'}; color:${isActive ? '#e2e8f0' : '#94a3b8'};">
                            ${step.label}
                        </span>
                    </div>
                `;
            }).join('<div style="flex:1; height:2px; background:rgba(255,255,255,0.1); min-width:20px;"></div>')}
        </div>`;
}

function formatDate(ts) {
    if (!ts) return 'N/A';
    return new Date(ts).toLocaleString();
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}
