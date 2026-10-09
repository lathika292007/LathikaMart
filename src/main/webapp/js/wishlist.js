document.addEventListener('DOMContentLoaded', loadWishlist);

async function loadWishlist() {
    const container = document.getElementById('wishlist-content');
    if (!container) return;

    try {
        const res = await fetch(`${window.contextPath}/api/v1/wishlist`);
        const data = await res.json();

        if (!data.success) {
            container.innerHTML = `
                <div class="glass-card text-center p-8 col-span-full">
                    <p class="text-gray-400 mb-4">Please log in to view your saved wishlist items.</p>
                    <a href="${window.contextPath}/login.jsp" class="btn btn-primary">Log In Now</a>
                </div>`;
            return;
        }

        const items = data.data || [];

        if (items.length === 0) {
            container.innerHTML = `
                <div class="glass-card text-center p-8 col-span-full">
                    <p class="text-gray-400 mb-4">Your wishlist is empty.</p>
                    <a href="${window.contextPath}/" class="btn btn-primary">Explore Products</a>
                </div>`;
            return;
        }

        container.innerHTML = items.map(item => {
            const p = item.product;
            if (!p) return '';
            return `
                <div class="product-card">
                    <img src="${escapeHtml(p.imageUrl || 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e')}" alt="${escapeHtml(p.name)}" class="product-image">
                    <div class="product-details">
                        <span class="product-category">${escapeHtml(p.category)}</span>
                        <h3 class="product-title">${escapeHtml(p.name)}</h3>
                        <p style="font-size:0.85rem; color:#94a3b8; margin-bottom:0.75rem;">${escapeHtml(p.description || '')}</p>
                        
                        <div style="display:flex; justify-content:space-between; align-items:center; margin-top:auto;">
                            <span class="product-price">$${parseFloat(p.price).toFixed(2)}</span>
                            <span class="badge ${p.stockQty > 0 ? 'badge-in-stock' : 'badge-out-of-stock'}">
                                ${p.stockQty > 0 ? p.stockQty + ' in stock' : 'Out of Stock'}
                            </span>
                        </div>

                        <div style="display:flex; gap:0.5rem; margin-top:1rem;">
                            <button class="btn btn-primary" style="flex:1;" onclick="moveToCart(${p.id})">
                                🛒 Move to Cart
                            </button>
                            <button class="btn btn-danger" style="padding:0.4rem 0.8rem;" onclick="removeFromWishlist(${p.id})">
                                🗑️ Remove
                            </button>
                        </div>
                    </div>
                </div>`;
        }).join('');

    } catch (e) {
        container.innerHTML = '<div class="glass-card text-center p-8 col-span-full"><p>Error loading wishlist.</p></div>';
    }
}

async function moveToCart(productId) {
    try {
        const cartRes = await fetch(`${window.contextPath}/api/v1/cart`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ productId: productId, quantity: 1 })
        });
        const cartData = await cartRes.json();

        if (cartData.success) {
            await fetch(`${window.contextPath}/api/v1/wishlist/${productId}`, { method: 'DELETE' });
            alert('Item moved to your cart!');
            loadWishlist();
        } else {
            alert(cartData.error ? cartData.error.message : 'Could not add item to cart.');
        }
    } catch (e) {
        alert('Failed to move item to cart.');
    }
}

async function removeFromWishlist(productId) {
    try {
        await fetch(`${window.contextPath}/api/v1/wishlist/${productId}`, { method: 'DELETE' });
        loadWishlist();
    } catch (e) {
        alert('Failed to remove item from wishlist.');
    }
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}
