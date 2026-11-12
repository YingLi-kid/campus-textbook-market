package com.example.textbook.application;

import com.example.textbook.domain.ITextbookRepository;
import com.example.textbook.domain.Textbook;
import com.example.textbook.domain.TextbookStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 教材业务实现。业务层依赖 ITextbookRepository 抽象，而不是具体存储实现。
 */
public class TextbookService implements ITextbookService {

    private final ITextbookRepository repository;

    public TextbookService(ITextbookRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    @Override
    public Textbook publish(Textbook textbook) {
        Objects.requireNonNull(textbook, "textbook must not be null");
        if (textbook.getTitle().isBlank()) {
            throw new IllegalArgumentException("教材名称不能为空");
        }
        if (textbook.getAuthor().isBlank()) {
            throw new IllegalArgumentException("作者不能为空");
        }
        BigDecimal price = textbook.getPrice();
        if (price.signum() <= 0) {
            throw new IllegalArgumentException("教材价格必须大于 0");
        }
        textbook.setStatus(TextbookStatus.PENDING);
        return repository.save(textbook);
    }

    @Override
    public Textbook getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("教材 ID 不能为空");
        }
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("教材不存在: " + id));
    }

    @Override
    public List<Textbook> search(String keyword) {
        return repository.search(keyword);
    }

    @Override
    public void takeDown(Long id) {
        Textbook textbook = getById(id);
        textbook.setStatus(TextbookStatus.OFF_SHELF);
        repository.save(textbook);
    }
}
