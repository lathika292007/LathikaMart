package com.lathika.lathikamart.model;

import java.sql.Timestamp;

public class Wishlist {
    private Long id;
    private Long userId;
    private Long productId;
    private Product product;
    private Timestamp createdAt;

    public Wishlist() {}

    public Wishlist(Long id, Long userId, Long productId, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
