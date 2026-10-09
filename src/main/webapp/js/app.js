/* LathikaMart Core Client App Logic */

document.addEventListener('DOMContentLoaded', () => {
    // Initial fetch for product catalog if on main page
    if (document.getElementById('product-list-container')) {
        loadProducts();
    }
});

async function loadProducts(search = '', category = 'all', minPrice = '', maxPrice = '', sortBy = 'newest') {
    const container = document.getElementById('product-list-container');
    if (!container) return;

    container.innerHTML = '<div class="glass-card text-center p-8"><p>Loading products...</p></div>';

    try {
        let url = `${window.contextPath}/api/v1/products`;
        const params = new URLSearchParams();
        if (search) params.append('search', search);
        if (category && category !== 'all') params.append('category', category);
        if (minPrice) params.append('minPrice', minPrice);
        if (maxPrice) params.append('maxPrice', maxPrice);
        if (sortBy) params.append('sortBy', sortBy);

        if (params.toString()) url += '?' + params.toString();

        const res = await fetch(url);
        const json = await res.json();

        if (json.success && json.data) {
            renderProducts(json.data);
        } else {
            container.innerHTML = '<div class="glass-card text-center p-8"><p>Failed to load products.</p></div>';
        }
    } catch (e) {
        container.innerHTML = '<div class="glass-card text-center p-8"><p>Error connecting to server.</p></div>';
    }
}

function renderProducts(products) {
    const container = document.getElementById('product-list-container');
    if (!container) return;

    if (products.length === 0) {
        container.innerHTML = `
            <div class="glass-card text-center p-8 col-span-full">
                <h3>No products found</h3>
                <p class="text-gray-400 mt-2">Try adjusting your search query or filters.</p>
            </div>`;
        return;
    }

    container.innerHTML = products.map(p => `
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

                <div style="display:flex; gap:0.4rem; margin-top:1rem; flex-wrap:wrap;">
                    <button class="btn btn-primary" style="flex:1; min-width:120px;" 
                            onclick="addToCart(${p.id})" ${p.stockQty <= 0 ? 'disabled' : ''}>
                        🛒 Add to Cart
                    </button>
                    <button class="btn btn-secondary" style="padding:0.6rem 0.6rem;" 
                            onclick="toggleWishlist(${p.id})" title="Add to Wishlist">
                        ❤️
                    </button>
                    <button class="btn btn-secondary" style="padding:0.6rem 0.6rem;" 
                            onclick="showReviewsModal(${p.id}, '${escapeHtml(p.name).replace(/'/g, "\\'")}')" title="View & Write Reviews">
                        ⭐ Reviews
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

async function addToCart(productId) {
    try {
        const res = await fetch(`${window.contextPath}/api/v1/cart`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ productId: productId, quantity: 1 })
        });
        const data = await res.json();
        if (data.success) {
            alert('Item added to cart!');
            if (typeof updateCartBadge === 'function') updateCartBadge();
        } else {
            alert(data.error ? data.error.message : 'Please log in to add items to cart.');
        }
    } catch (e) {
        alert('Could not add item to cart. Please check your login session.');
    }
}

async function toggleWishlist(productId) {
    try {
        const res = await fetch(`${window.contextPath}/api/v1/wishlist`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ productId: productId })
        });
        const data = await res.json();
        if (data.success) {
            alert('❤️ Added to your Wishlist!');
        } else {
            alert(data.error ? data.error.message : 'Please log in to save items to your wishlist.');
        }
    } catch (e) {
        alert('Could not update wishlist. Please check your login session.');
    }
}

/* Reviews Modal Logic */
async function showReviewsModal(productId, productName) {
    const modal = document.getElementById('reviews-modal');
    if (!modal) return;

    document.getElementById('modal-product-title').innerText = '⭐ Reviews: ' + productName;
    document.getElementById('review-product-id').value = productId;
    document.getElementById('review-comment').value = '';
    modal.classList.add('active');

    loadProductReviews(productId);
}

function closeReviewsModal() {
    const modal = document.getElementById('reviews-modal');
    if (modal) modal.classList.remove('active');
}

async function loadProductReviews(productId) {
    const container = document.getElementById('reviews-list-container');
    if (!container) return;

    container.innerHTML = '<p style="color:#94a3b8; text-align:center;">Loading customer reviews...</p>';

    try {
        const res = await fetch(`${window.contextPath}/api/v1/reviews?productId=${productId}`);
        const data = await res.json();

        if (data.success && data.data && data.data.length > 0) {
            container.innerHTML = data.data.map(r => `
                <div class="review-item">
                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:0.4rem;">
                        <span style="font-weight:700; color:#38bdf8; font-size:0.85rem;">Buyer #${r.userId}</span>
                        <span style="color:#f59e0b; font-weight:700;">${'⭐'.repeat(r.rating || 5)} (${r.rating}/5)</span>
                    </div>
                    <p style="font-size:0.85rem; color:#e2e8f0; margin:0;">${escapeHtml(r.comment)}</p>
                </div>
            `).join('');
        } else {
            container.innerHTML = '<p style="color:#94a3b8; text-align:center; padding:1rem;">No reviews for this product yet. Be the first to write a review!</p>';
        }
    } catch (e) {
        container.innerHTML = '<p style="color:#ef4444; text-align:center;">Failed to load reviews.</p>';
    }
}

async function submitReview(e) {
    e.preventDefault();
    const productId = document.getElementById('review-product-id').value;
    const rating = parseInt(document.getElementById('review-rating').value);
    const comment = document.getElementById('review-comment').value;

    try {
        const res = await fetch(`${window.contextPath}/api/v1/reviews`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ productId: parseInt(productId), rating: rating, comment: comment })
        });
        const data = await res.json();

        if (data.success) {
            alert('Thank you! Your review has been submitted successfully.');
            document.getElementById('review-comment').value = '';
            loadProductReviews(productId);
        } else {
            alert(data.error ? data.error.message : 'Please log in to submit a review.');
        }
    } catch (err) {
        alert('Failed to submit review. Please ensure you are logged in.');
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

