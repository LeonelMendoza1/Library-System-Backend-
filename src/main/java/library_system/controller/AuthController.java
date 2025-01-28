package library_system.controller;

import library_system.dto.UserDTO;
import library_system.dto.UserLoginDTO;
import library_system.model.User;
import library_system.repository.UserRepository;
import library_system.service.UserService;
import library_system.utilCors.JwtUtil;
import library_system.utilCors.TokenBlackList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TokenBlackList tokenBlacklist;
    private final UserRepository userRepository; // Inyectar UserRepository


    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, TokenBlackList tokenBlacklist, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.tokenBlacklist = tokenBlacklist;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserDTO registerDTO) {
        try {
            // Registrar el usuario
            userService.registerUser(registerDTO);

            // Responder con éxito
            Map<String, String> response = Map.of("message", "Usuario registrado exitosamente");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDTO loginRequest) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            String username = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();

            // Buscar al usuario en la base de datos para obtener el userId
            User user = this.userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            Long userId = user.getId();

            // Generar el token con el userId
            String token = jwtUtil.generateToken(username, role, userId);

            Map<String, String> response = Map.of(
                    "message", "Login exitoso",
                    "userID", userId.toString(),
                    "token", token,
                    "role", role
            );

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciales inválidas"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            tokenBlacklist.add(token); // Revocar el token
            return ResponseEntity.ok(Map.of("message", "Logout exitoso"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Token no proporcionado"));
    }
}
