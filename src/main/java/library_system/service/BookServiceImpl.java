package library_system.service;

import library_system.model.Book;
import library_system.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setIsbn(updatedBook.getIsbn());
            book.setPublicationYear(updatedBook.getPublicationYear());
            book.setGenre(updatedBook.getGenre());
            book.setImageUrl(updatedBook.getImageUrl());
            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado");
        }
        bookRepository.deleteById(id);
    }
}
