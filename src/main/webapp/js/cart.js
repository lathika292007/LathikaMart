document.addEventListener('DOMContentLoaded', loadCart);

async function loadCart() {
    const container = document.getElementById('cart-content');
    if (!container) return;

    try {
        const res = await fetch(`${window.contextPath}/api/v1/cart`);
        const data = await res.json();

        if (!data.success) {
            container.innerHTML = `
                <div class="glass-card text-center p-8">
                    <p class="text-gray-400 mb-4">Please log in to view your shopping cart.</p>
                    <a href="${window.contextPath}/login.jsp" class="btn btn-primary">Log In Now</a>
                </div>`;
            return;
        }

        const items = data.data.items || [];
        const total = data.data.totalAmount || 0;

        if (items.length === 0) {
            container.innerHTML = `
                <div class="glass-card text-center p-8">
                    <p class="text-gray-400 mb-4">Your cart is empty.</p>
                    <a href="${window.contextPath}/" class="btn btn-primary">Start Shopping</a>
                </div>`;
            return;
        }

        container.innerHTML = `
            <div class="glass-card">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Subtotal</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${items.map(item => `
                            <tr>
                                <td>
                                    <div style="font-weight:700;">${escapeHtml(item.product ? item.product.name : 'Product')}</div>
                                </td>
                                <td>$${item.product ? parseFloat(item.product.price).toFixed(2) : '0.00'}</td>
                                <td>
                                    <input type="number" min="1" value="${item.quantity}" 
                                           style="width:60px;" class="form-input" 
                                           onchange="updateQty(${item.id}, this.value)">
                                </td>
                                <td style="color:#38bdf8; font-weight:700;">
                                    $${(item.product ? parseFloat(item.product.price) * item.quantity : 0).toFixed(2)}
                                </td>
                                <td>
                                    <button onclick="removeItem(${item.id})" class="btn btn-danger" style="padding:0.3rem 0.6rem; font-size:0.8rem;">Remove</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>

                <div style="margin-top:2rem; display:flex; justify-content:space-between; align-items:center; border-top:1px solid rgba(255,255,255,0.1); padding-top:1.5rem;">
                    <div>
                        <span style="font-size:1.25rem; font-weight:800;">Total Running Amount: </span>
                        <span style="font-size:1.5rem; font-weight:800; color:#38bdf8;">$${parseFloat(total).toFixed(2)}</span>
                    </div>
                    <button onclick="placeOrder()" class="btn btn-primary" style="padding:0.75rem 1.5rem; font-size:1.1rem;">
                        💳 Place Order (Mock Payment)
                    </button>
                </div>
            </div>`;

    } catch (e) {
        container.innerHTML = '<div class="glass-card text-center p-8"><p>Error loading cart.</p></div>';
    }
}

async function updateQty(cartItemId, quantity) {
    try {
        await fetch(`${window.contextPath}/api/v1/cart/${cartItemId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ quantity: parseInt(quantity) })
        });
        loadCart();
    } catch (e) {
        alert('Failed to update item quantity.');
    }
}

async function removeItem(cartItemId) {
    try {
        await fetch(`${window.contextPath}/api/v1/cart/${cartItemId}`, { method: 'DELETE' });
        loadCart();
    } catch (e) {
        alert('Failed to remove item.');
    }
}

async function placeOrder() {
    if (!confirm('Confirm placement of order via mock payment gateway?')) return;
    try {
        const res = await fetch(`${window.contextPath}/api/v1/orders`, { method: 'POST' });
        const data = await res.json();
        if (data.success) {
            alert('Order placed successfully! Order ID: #' + data.data.id);
            window.location.href = `${window.contextPath}/orders.jsp`;
        } else {
            alert(data.error ? data.error.message : 'Failed to place order.');
        }
    } catch (e) {
        alert('Order placement error.');
    }
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}
