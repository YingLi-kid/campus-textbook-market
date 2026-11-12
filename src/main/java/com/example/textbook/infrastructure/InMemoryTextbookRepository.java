package com.example.textbook.infrastructure;

import com.example.textbook.domain.ITextbookRepository;
import com.example.textbook.domain.Textbook;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 内存版教材仓储，便于在没有数据库的情况下演示依赖倒置。
 */
public class InMemoryTextbookRepository implements ITextbookRepository {

    private final Map<Long, Textbook> textbooks = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Textbook save(Textbook textbook) {
        Objects.requireNonNull(textbook, "textbook must not be null");
        if (textbook.getId() == null) {
            textbook.setId(sequence.incrementAndGet());
        }
        textbooks.put(textbook.getId(), textbook);
        return textbook;
    }

    @Override
    public Optional<Textbook> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(textbooks.get(id));
    }

    @Override
    public List<Textbook> findAll() {
        return List.copyOf(textbooks.values());
    }

    @Override
    public List<Textbook> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        String normalized = keyword.toLowerCase(Locale.ROOT);
        return textbooks.values().stream()
                .filter(textbook -> textbook.getTitle().toLowerCase(Locale.ROOT).contains(normalized)
                        || textbook.getAuthor().toLowerCase(Locale.ROOT).contains(normalized))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) {
            textbooks.remove(id);
        }
    }
}
