package library_system.controller;

import library_system.model.Borrow;
import library_system.service.BorrowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {
    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Map<String, String>> borrowBook(@RequestParam Long userId, @PathVariable Long bookId) {
        borrowService.borrowBook(userId, bookId);
        Map<String, String> response = Map.of("message", "Libro prestado exitosamente");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/return/{bookId}")
    public ResponseEntity<Map<String, String>> returnBook(@RequestParam Long userId, @PathVariable Long bookId) {
        borrowService.returnBook(userId, bookId);
        Map<String, String> response = Map.of("message", "Libro devuelto exitosamente");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Borrow>> getBorrowedBooksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(borrowService.getBorrowedBooksByUser(userId));
    }
}
