package library_system.repository;

import library_system.model.Book;
import library_system.model.Borrow;
import library_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    Optional<Borrow> findByBookAndReturnDateIsNull(Book book);

    List<Borrow> findByUserAndReturnDateIsNull(User user);


}
