package com.example.textbook.application;

import com.example.textbook.domain.Textbook;
import com.example.textbook.domain.TextbookStatus;
import com.example.textbook.infrastructure.InMemoryTextbookRepository;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TextbookServiceTest {

    private ITextbookService service;

    @Before
    public void setUp() {
        service = new TextbookService(new InMemoryTextbookRepository());
    }

    @Test
    public void shouldPublishAndFindTextbook() {
        Textbook textbook = new Textbook(null, "Java 编程思想", "Bruce Eckel", "202631020442", new BigDecimal("45.00"));

        Textbook saved = service.publish(textbook);

        assertNotNull(saved.getId());
        assertEquals(TextbookStatus.PENDING, saved.getStatus());
        assertEquals("Java 编程思想", service.getById(saved.getId()).getTitle());
    }

    @Test
    public void shouldSearchByTitleOrAuthor() {
        service.publish(new Textbook(null, "Java 编程思想", "Bruce Eckel", "202631020442", new BigDecimal("45.00")));
        service.publish(new Textbook(null, "数据结构", "严蔚敏", "202631020442", new BigDecimal("30.00")));

        List<Textbook> javaBooks = service.search("java");
        List<Textbook> authorBooks = service.search("严蔚敏");

        assertEquals(1, javaBooks.size());
        assertEquals(1, authorBooks.size());
        assertEquals("数据结构", authorBooks.get(0).getTitle());
    }

    @Test
    public void shouldTakeDownTextbook() {
        Textbook saved = service.publish(new Textbook(null, "计算机网络", "谢希仁", "202631020442", new BigDecimal("35.00")));

        service.takeDown(saved.getId());

        assertEquals(TextbookStatus.OFF_SHELF, service.getById(saved.getId()).getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNonPositivePrice() {
        service.publish(new Textbook(null, "操作系统", "汤小丹", "202631020442", BigDecimal.ZERO));
    }

    @Test
    public void shouldReturnEmptyWhenKeywordDoesNotMatch() {
        service.publish(new Textbook(null, "软件工程", "张海藩", "202631020442", new BigDecimal("40.00")));

        assertTrue(service.search("不存在的教材").isEmpty());
    }
}
