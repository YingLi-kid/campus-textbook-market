package com.example.textbook.domain;

import java.util.List;
import java.util.Optional;

/**
 * 教材仓储抽象，业务层只依赖这个接口，不关心具体存储方式。
 */
public interface ITextbookRepository {

    Textbook save(Textbook textbook);

    Optional<Textbook> findById(Long id);

    List<Textbook> findAll();

    List<Textbook> search(String keyword);

    void deleteById(Long id);
}
