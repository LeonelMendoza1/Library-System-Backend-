package library_system.service;

import library_system.model.Book;

import java.util.List;

public interface BookService {
    Book saveBook(Book book);
    List<Book> getAllBooks();
    Book updateBook(Long id, Book updatedBook);
    void deleteBook(Long id);
}
