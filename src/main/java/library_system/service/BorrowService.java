package library_system.service;

import library_system.model.Borrow;

import java.util.List;

public interface BorrowService {

    Borrow borrowBook(Long userId, Long bookId);
    Borrow returnBook(Long userId, Long bookId);
    List<Borrow> getBorrowedBooksByUser(Long userId);

}
