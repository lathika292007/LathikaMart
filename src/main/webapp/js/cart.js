document.addEventListener('DOMContentLoaded', loadCart);

let appliedDiscountPercent = 0;
let appliedDiscountFlat = 0;
let appliedCouponCode = '';

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
        const rawTotal = data.data.totalAmount || 0;

        if (items.length === 0) {
            container.innerHTML = `
                <div class="glass-card text-center p-8">
                    <p class="text-gray-400 mb-4">Your cart is empty.</p>
                    <a href="${window.contextPath}/" class="btn btn-primary">Start Shopping</a>
                </div>`;
            return;
        }

        let finalTotal = rawTotal;
        if (appliedDiscountPercent > 0) {
            finalTotal = rawTotal * (1 - appliedDiscountPercent / 100);
        } else if (appliedDiscountFlat > 0) {
            finalTotal = Math.max(0, rawTotal - appliedDiscountFlat);
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

                <!-- Promo Coupon Section -->
                <div style="margin-top:1.5rem; background:rgba(255,255,255,0.03); padding:1rem; border-radius:8px; display:flex; align-items:center; gap:1rem; flex-wrap:wrap;">
                    <span style="font-weight:700;">🎟️ Promo Code:</span>
                    <input type="text" id="couponInput" placeholder="Try LATHIKA10 or WELCOME20" class="form-input" style="width:220px; text-transform:uppercase;">
                    <button onclick="applyCoupon()" class="btn btn-secondary">Apply Coupon</button>
                    <span id="couponMsg" style="font-weight:700; color:#4ade80;">
                        ${appliedCouponCode ? `✅ Coupon ${appliedCouponCode} active!` : ''}
                    </span>
                </div>

                <div style="margin-top:1.5rem; display:flex; justify-content:space-between; align-items:center; border-top:1px solid rgba(255,255,255,0.1); padding-top:1.5rem;">
                    <div>
                        <div style="font-size:1rem; color:#94a3b8;">Subtotal: $${parseFloat(rawTotal).toFixed(2)}</div>
                        ${appliedCouponCode ? `<div style="font-size:0.9rem; color:#4ade80;">Discount Applied: -$${(rawTotal - finalTotal).toFixed(2)}</div>` : ''}
                        <div style="font-size:1.25rem; font-weight:800;">Total Amount: </div>
                        <div style="font-size:1.5rem; font-weight:800; color:#38bdf8;">$${parseFloat(finalTotal).toFixed(2)}</div>
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

async function applyCoupon() {
    const input = document.getElementById('couponInput');
    if (!input || !input.value.trim()) return;
    const code = input.value.trim().toUpperCase();

    try {
        const res = await fetch(`${window.contextPath}/api/v1/coupon/apply`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ code })
        });
        const data = await res.json();
        if (data.success) {
            appliedCouponCode = code;
            if (data.data.isPercentage) {
                appliedDiscountPercent = parseFloat(data.data.discountValue);
                appliedDiscountFlat = 0;
            } else {
                appliedDiscountFlat = parseFloat(data.data.discountValue);
                appliedDiscountPercent = 0;
            }
            alert(`🎉 Coupon ${code} applied successfully!`);
            loadCart();
        } else {
            alert(data.error ? data.error.message : 'Invalid coupon code.');
        }
    } catch (e) {
        alert('Failed to apply coupon.');
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
