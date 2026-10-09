let allSellerProducts = [];

document.addEventListener('DOMContentLoaded', () => {
    loadSellerStats();
    loadSellerProducts();

    const form = document.getElementById('product-form');
    if (form) {
        form.addEventListener('submit', handleProductFormSubmit);
    }
});

async function loadSellerStats() {
    try {
        const res = await fetch(`${window.contextPath}/api/v1/seller/stats`);
        const data = await res.json();
        if (data.success && data.data) {
            const s = data.data;
            document.getElementById('stat-revenue').textContent = '$' + parseFloat(s.totalRevenue || 0).toFixed(2);
            document.getElementById('stat-orders').textContent = s.totalOrders || 0;
            document.getElementById('stat-items').textContent = s.totalItemsSold || 0;
            document.getElementById('stat-products').textContent = s.totalProducts || 0;
            document.getElementById('stat-lowstock').textContent = s.lowStockCount || 0;
        }
    } catch (e) {
        console.error('Error loading seller stats', e);
    }
}

async function loadSellerProducts() {
    const container = document.getElementById('seller-products-table');
    const currentUser = window.currentUserId;

    if (!currentUser) {
        container.innerHTML = '<div class="text-center p-8"><p style="color:#94a3b8;">Please <a href="' + window.contextPath + '/login.jsp" style="color:#38bdf8; font-weight:600; text-decoration:underline;">log in</a> to view and manage your product listings.</p></div>';
        return;
    }

    try {
        const res = await fetch(`${window.contextPath}/api/v1/products?sellerId=${currentUser}`);
        const data = await res.json();
        allSellerProducts = data.data || [];
        renderSellerProductsTable(allSellerProducts);
    } catch (e) {
        container.innerHTML = '<div class="text-center p-8"><p>Error loading product listings.</p></div>';
    }
}

function renderSellerProductsTable(products) {
    const container = document.getElementById('seller-products-table');
    if (!products || products.length === 0) {
        container.innerHTML = '<div class="text-center p-8"><p class="text-gray-400">No product listings found. Click "+ Create New Product Listing" to start.</p></div>';
        return;
    }

    container.innerHTML = `
        <table class="data-table">
            <thead>
                <tr>
                    <th>Preview</th>
                    <th>Title</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                ${products.map(p => `
                    <tr>
                        <td>
                            <img src="${escapeHtml(p.imageUrl || 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e')}" 
                                 style="width:48px; height:48px; object-fit:cover; border-radius:8px;">
                        </td>
                        <td>
                            <strong>${escapeHtml(p.name)}</strong>
                            <div class="text-muted" style="font-size:0.8rem;">ID: #${p.id}</div>
                        </td>
                        <td>${escapeHtml(p.category)}</td>
                        <td style="color:#38bdf8; font-weight:700;">$${parseFloat(p.price).toFixed(2)}</td>
                        <td>${p.stockQty}</td>
                        <td>
                            <span class="badge ${p.stockQty <= 5 ? 'badge-out-of-stock' : 'badge-in-stock'}">
                                ${p.stockQty <= 5 ? (p.stockQty === 0 ? 'Out of Stock' : 'Low Stock (' + p.stockQty + ')') : 'In Stock'}
                            </span>
                        </td>
                        <td>
                            <button class="btn btn-secondary" style="padding:0.3rem 0.6rem; font-size:0.8rem;" onclick="editProduct(${p.id})">✏️ Edit</button>
                            <button class="btn btn-danger" style="padding:0.3rem 0.6rem; font-size:0.8rem;" onclick="deleteProduct(${p.id})">🗑️ Delete</button>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>`;
}

function filterTable() {
    const q = document.getElementById('filter-seller-products').value.toLowerCase().trim();
    if (!q) {
        renderSellerProductsTable(allSellerProducts);
        return;
    }
    const filtered = allSellerProducts.filter(p => 
        p.name.toLowerCase().includes(q) || p.category.toLowerCase().includes(q)
    );
    renderSellerProductsTable(filtered);
}

function showProductModal() {
    document.getElementById('prod-id').value = '';
    document.getElementById('product-form').reset();
    document.getElementById('modal-title').textContent = 'Add New Product Listing';
    document.getElementById('product-modal').style.display = 'flex';
}

function hideProductModal() {
    document.getElementById('product-modal').style.display = 'none';
}

async function editProduct(id) {
    try {
        const res = await fetch(`${window.contextPath}/api/v1/products/${id}`);
        const data = await res.json();
        if (data.success && data.data) {
            const p = data.data;
            document.getElementById('prod-id').value = p.id;
            document.getElementById('prod-name').value = p.name;
            document.getElementById('prod-category').value = p.category;
            document.getElementById('prod-price').value = p.price;
            document.getElementById('prod-stock').value = p.stockQty;
            document.getElementById('prod-image').value = p.imageUrl || '';
            document.getElementById('prod-desc').value = p.description || '';

            document.getElementById('modal-title').textContent = 'Edit Product Listing #' + p.id;
            document.getElementById('product-modal').style.display = 'flex';
        }
    } catch (e) {
        alert('Failed to fetch product details.');
    }
}

async function handleProductFormSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('prod-id').value;
    const name = document.getElementById('prod-name').value.trim();
    const category = document.getElementById('prod-category').value.trim();
    const priceVal = parseFloat(document.getElementById('prod-price').value);
    const stockVal = parseInt(document.getElementById('prod-stock').value, 10);
    let imageUrl = document.getElementById('prod-image').value.trim();
    const description = document.getElementById('prod-desc').value.trim();

    if (!name) {
        alert('Please enter a product name.');
        return;
    }
    if (isNaN(priceVal) || priceVal <= 0) {
        alert('Please enter a valid price greater than 0.');
        return;
    }
    if (isNaN(stockVal) || stockVal < 0) {
        alert('Please enter a valid stock quantity.');
        return;
    }

    if (!imageUrl) {
        imageUrl = 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e';
    }

    const payload = {
        name: name,
        category: category,
        price: priceVal,
        stockQty: stockVal,
        imageUrl: imageUrl,
        description: description
    };

    const url = id ? `${window.contextPath}/api/v1/products/${id}` : `${window.contextPath}/api/v1/products`;
    const method = id ? 'PUT' : 'POST';

    try {
        const res = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const data = await res.json();
        if (data.success) {
            alert(id ? '✨ Product listing updated successfully!' : '🎉 New product listing created successfully!');
            hideProductModal();
            loadSellerStats();
            loadSellerProducts();
        } else {
            alert(data.error ? data.error.message : 'Failed to save product listing.');
        }
    } catch (err) {
        alert('Network error saving product. Please check your session or connection.');
    }
}

async function deleteProduct(id) {
    if (!confirm('Are you sure you want to delete this listing?')) return;
    try {
        const res = await fetch(`${window.contextPath}/api/v1/products/${id}`, { method: 'DELETE' });
        const data = await res.json();
        if (data.success) {
            loadSellerStats();
            loadSellerProducts();
        } else {
            alert(data.error ? data.error.message : 'Failed to delete listing.');
        }
    } catch (e) {
        alert('Error deleting product.');
    }
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}
