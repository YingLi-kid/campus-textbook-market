package com.example.textbook.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 二手教材领域对象。
 */
public class Textbook {

    private Long id;
    private final String title;
    private final String author;
    private final String sellerId;
    private final BigDecimal price;
    private TextbookStatus status;

    public Textbook(Long id, String title, String author, String sellerId, BigDecimal price) {
        this.id = id;
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.author = Objects.requireNonNull(author, "author must not be null");
        this.sellerId = Objects.requireNonNull(sellerId, "sellerId must not be null");
        this.price = Objects.requireNonNull(price, "price must not be null");
        this.status = TextbookStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSellerId() {
        return sellerId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public TextbookStatus getStatus() {
        return status;
    }

    public void setStatus(TextbookStatus status) {
        this.status = Objects.requireNonNull(status, "status must not be null");
    }
}
