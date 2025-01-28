package library_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/current-user")
    public ResponseEntity<String> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok("Usuario autenticado: " + authentication.getName() +
                ", Roles: " + authentication.getAuthorities());
    }
}
