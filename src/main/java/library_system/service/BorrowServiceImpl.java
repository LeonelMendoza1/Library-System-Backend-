package library_system.service;

import library_system.model.Book;
import library_system.model.Borrow;
import library_system.model.User;
import library_system.repository.BookRepository;
import library_system.repository.BorrowRepository;
import library_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService{

    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public BorrowServiceImpl(BorrowRepository borrowRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Borrow borrowBook(Long userId, Long bookId) {
        // Verifica si el usuario existe
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Verifica si el libro existe
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        // Verifica si el libro ya está prestado
        if (borrowRepository.findByBookAndReturnDateIsNull(book).isPresent()) {
            throw new RuntimeException("El libro ya está prestado");
        }
        book.setAvailable(false);
        // Crea un registro de préstamo
        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(LocalDate.now());
        return borrowRepository.save(borrow);
    }

    @Override
    public Borrow returnBook(Long userId, Long bookId) {
        // Verifica si el préstamo existe y si pertenece al usuario
        Borrow borrow = borrowRepository.findByBookAndReturnDateIsNull(
                bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Libro no encontrado"))
        ).orElseThrow(() -> new RuntimeException("El libro no está actualmente prestado"));

        if (!borrow.getUser().getId().equals(userId)) {
            throw new RuntimeException("El usuario no tiene este libro en préstamo");
        }
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        book.setAvailable(true);
        // Marca el libro como devuelto
        borrow.setReturnDate(LocalDate.now());
        return borrowRepository.save(borrow);
    }

    @Override
    public List<Borrow> getBorrowedBooksByUser(Long userId) {
        // Verificar si el usuario existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Consultar los préstamos activos
        return borrowRepository.findByUserAndReturnDateIsNull(user);
    }
}
