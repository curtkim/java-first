package com.example.bookservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "book", description = "book에 관한 api들")
@RestController
@RequestMapping("/book")
public class BookController {

  @Autowired
  private BookRepository repository;

  @GetMapping("/{id}")
  public Book findById(@PathVariable long id) {
    return repository.findById(id)
        .orElseThrow(() -> new BookNotFoundException());
  }


  @Operation(summary = "전체 책 목록을 반환한다.", description = "전체 책목록을 반환한다.", tags = { "book" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = Book.class)))),
      @ApiResponse(responseCode = "404", description = "not found")
  })
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
