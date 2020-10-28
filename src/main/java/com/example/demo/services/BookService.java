package com.example.demo.services;

import com.example.demo.entities.Book;
import com.example.demo.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j // use this lombok annotation instead of instantiate a logger object
@RequiredArgsConstructor // use this lombok annotation to replace autowired UserRepository
public class BookService {
    private final BookRepository bookRepository;

    //User find all books (no matter if it is available or not)
    @Cacheable(value = "bookCache")
    public List<Book> findAll(String name, boolean sortOnAuthorName){
        log.info("Request to find all books");
        log.warn("Fresh data...");

        var books = bookRepository.findAll();
        if(name!=null){
            books = books.stream()
                    .filter(book -> book.getName().equals(name))
                    .collect(Collectors.toList());
        }
        if(sortOnAuthorName){
            books.sort(Comparator.comparing(Book::getAuthor));
        }
        return books;
    }

    //User find all books available
    @Cacheable(value = "bookCache")
    public List<Book> findByIsAvailable(boolean isAvailable){
        var books = bookRepository.findAll();
        books = books.stream()
                .filter(book -> book.isAvailable() == isAvailable)
                .collect(Collectors.toList());
        return books;
    }

    //User find book by Id
    @Cacheable(value = "bookCache", key = "#id")
    public Book findById(String id){
        return bookRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Could not find the book by id %s.", id)));
    }

    //user search for book by BookName
    @Cacheable(value = "bookCache")
    public Book findByName(String name){
        return bookRepository.findByName(name)
                .orElseThrow(()-> new ResponseStatusException((HttpStatus.NOT_FOUND), //404 - NOT FOUND
                        String.format("Could not find the book by name %s.", name)));
    }

    //user search for book by BookISBN
    @Cacheable(value = "bookCache")
    public Book findByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(()-> new ResponseStatusException((HttpStatus.NOT_FOUND), //404 - NOT FOUND
                        String.format("Could not find the book by ISBN %s.", isbn)));
    }

    //user search for book by Author
    @Cacheable(value = "bookCache")
    public Book findByAuthor(String author){
        return bookRepository.findByAuthor(author)
                .orElseThrow(()-> new ResponseStatusException((HttpStatus.NOT_FOUND), //404 - NOT FOUND
                        String.format("Could not find the book by Author %s.", author)));
    }

    //librarian save new book
    @CachePut(value = "bookCache", key = "#result.id")
    public Book save(Book book){
        return bookRepository.save(book);
    }

    //librarian update books
    @CachePut(value = "bookCache", key = "#id")
    public void update(String id, Book book){
//        var isLibrarian = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().toUpperCase().equals("ROLE_LIBRARIAN"));
//        var isCurrentUser =
//
    if (!bookRepository.existsById(id)){
        log.error(String.format("Could not find the book by id %s.", id));// add one log for the error
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 - NOT FOUND
                String.format("Could not find the book by id %s.", id));}

    book.setId(id);

    book = Book.builder()
            .id(id)
            .isbn(book.getIsbn())
            .name(book.getName())
            .plot(book.getPlot())
            .author(book.getAuthor())
            .genre(book.getGenre())
            .isAvailable(book.isAvailable())
            .build();

    bookRepository.save(book);
    }

    //librarian remove a book
    @CacheEvict(value = "bookCache", key = "#id")
    public void delete(String id) {
        if (!bookRepository.existsById(id)) {
            log.error(String.format("Could not find the book by id %s.", id));// add one log for the error
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 - NOT FOUND
                    String.format("Could not find the book by id %s.", id));
        }

        bookRepository.deleteById(id);
    }

    //user borrow a book
    @CachePut(value = "bookCache", key = "#id")
    public void borrowBook(String id, Book book){
        if(!bookRepository.existsById(id)) {
            log.error(String.format("Could not find the book by id %s.", id));// add one log for the error
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 - NOT FOUND
                    String.format("Could not find the book by id %s.", id));
        }
        if(!bookRepository.findById(id).get().isAvailable())
        {
            log.error(String.format("This book %s is not available.", id));// add one log for the error
            throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, //451
                    String.format("This book %s is not available.", id));
        }

        book.setId(id);
        book.setAvailable(false);
        bookRepository.save(book);
    }

    //user return a book
    public void returnBook(String id, Book book){
        if (!bookRepository.existsById(id)) {
            log.error(String.format("Could not find the book by id %s.", id));// add one log for the error
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, //404 - NOT FOUND
                    String.format("Could not find the book by id %s.", id));
        }
        book.setId(id);
        book.setAvailable(true);

        bookRepository.save(book);
    }
}
