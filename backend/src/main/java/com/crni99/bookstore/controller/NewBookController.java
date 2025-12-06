package com.crni99.bookstore.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.crni99.bookstore.model.Book;
import com.crni99.bookstore.service.BookService;

@RestController
@RequestMapping("/api/books")
public class NewBookController {

    private final BookService bookService;

    public NewBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Page<Book> getBooks(
            @RequestParam(required = false) String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return bookService.findPaginated(pageable, term);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        book.setId(null);
        Book savedBook = bookService.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        return bookService.findBookById(id)
                .map(existingBook -> {
                    book.setId(id);
                    Book updatedBook = bookService.save(book);
                    return ResponseEntity.ok(updatedBook);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
