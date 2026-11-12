package com.example.textbook.application;

import com.example.textbook.domain.Textbook;

import java.util.List;

/**
 * 教材业务接口，供接口层调用。
 */
public interface ITextbookService {

    Textbook publish(Textbook textbook);

    Textbook getById(Long id);

    List<Textbook> search(String keyword);

    void takeDown(Long id);
}
