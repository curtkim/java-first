package com.example.springdocfirst;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/book")
public class BookController {

  @Autowired
  private BookRepository repository;

  @GetMapping("/{id}")
  public Book findById(@PathVariable long id) {
    return repository.findById(id)
        .orElseThrow(() -> new BookNotFoundException());
  }

  @GetMapping("/all")
  public Collection<Book> findBooks() {
    return repository.getBooks();
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Book updateBook(
      @PathVariable("id") final String id, @RequestBody final Book book) {
    repository.add(book);
    return book;
  }

  @GetMapping("/")
  public Page<Book> filterBooks(@ParameterObject Pageable pageable) {
    return repository.getBooks(pageable);
  }
}
