package com.crni99.bookstore.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crni99.bookstore.model.Book;
import com.crni99.bookstore.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // 1. CACHE LIST & SEARCH
    @Cacheable(value = "books_search", key = "{#pageable.pageNumber, #pageable.pageSize, #term}")
    public Page<Book> findPaginated(Pageable pageable, String term) {
        return page(pageable, term);
    }

    private Page<Book> page(Pageable pageable, String term) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        ArrayList<Book> books;
        List<Book> list;

        if (term == null) {
            books = (ArrayList<Book>) bookRepository.findAll();
        } else {
            books = (ArrayList<Book>) bookRepository.findByNameContaining(term);
        }

        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), books.size());
    }

    // 2. CACHE CHI TIẾT
    @Cacheable(value = "books", key = "#id")
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    // 3. XỬ LÝ KHI UPDATE/INSERT
    @Caching(
            put = {@CachePut(value = "books", key = "#book.id")},
            evict = {@CacheEvict(value = "books_search", allEntries = true)}
    )
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    // 4. XỬ LÝ KHI DELETE
    @Caching(
            evict = {
                    @CacheEvict(value = "books", key = "#id"),
                    @CacheEvict(value = "books_search", allEntries = true)
            }
    )
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}