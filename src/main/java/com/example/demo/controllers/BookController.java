package com.example.demo.controllers;

import com.example.demo.entities.Book;
import com.example.demo.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library/books")
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;

    @Secured({"ROLE_USER","ROLE_LIBRARIAN"})
    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks(@RequestParam(required = false) String name, @RequestParam(required = false) boolean sort){
        return ResponseEntity.ok(bookService.findAll(name, sort));
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN"})
    @GetMapping("/available/") //for example: localhost:7000/api/library/books/available/?isAvailable=true
    public ResponseEntity<List<Book>> findAllBooksAvailable(@RequestParam(required = false) boolean isAvailable){
        return ResponseEntity.ok(bookService.findByIsAvailable(isAvailable));
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN"})
    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable String id){
        return ResponseEntity.ok(bookService.findById(id));
    }

    @Secured("ROLE_LIBRARIAN")
    @PostMapping
    public ResponseEntity<Book> saveBook(@Validated @RequestBody Book book){
        return ResponseEntity.ok(bookService.save(book));
    }

    @Secured("ROLE_LIBRARIAN")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@PathVariable String id, @Validated @RequestBody Book book){
        bookService.update(id, book);
    }

    @Secured("ROLE_LIBRARIAN")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id){
        bookService.delete(id);
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN"})
    @GetMapping("/name={name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Book> findByName(@PathVariable String name){
        return ResponseEntity.ok(bookService.findByName(name));
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN"})
    @GetMapping("/isbn={isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Book> findByIsbn(@PathVariable String isbn){
        return ResponseEntity.ok(bookService.findByIsbn(isbn));
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN"})
    @GetMapping("/author={author}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Book> findByAuthor(@PathVariable String author){
        return ResponseEntity.ok(bookService.findByAuthor(author));
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN"})
    @PutMapping("/borrow/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrowBook(@PathVariable String id, @Validated @RequestBody Book book){
        bookService.borrowBook(id, book);
    }

    @Secured({"ROLE_USER","ROLE_LIBRARIAN"})
    @PutMapping("/return/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnBook(@PathVariable String id, @Validated @RequestBody Book book){
        bookService.returnBook(id, book);
    }
}
