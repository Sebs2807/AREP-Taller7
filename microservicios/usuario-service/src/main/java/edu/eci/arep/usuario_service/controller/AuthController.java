package edu.eci.arep.usuario_service.controller;

import edu.eci.arep.usuario_service.model.Usuario;
import edu.eci.arep.usuario_service.repository.UsuarioRepository;
import edu.eci.arep.usuario_service.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "https://minitwitter-camilo.s3.amazonaws.com/index.html")
public class AuthController {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) return ResponseEntity.badRequest().body("username and password required");
        if (usuarioRepo.findByUsername(username) != null) return ResponseEntity.badRequest().body("username already exists");

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setDisplayName(body.getOrDefault("displayName", username));
        u.setEmail(body.getOrDefault("email", ""));

        Usuario saved = usuarioRepo.save(u);
        return ResponseEntity.ok(Map.of("id", saved.getId(), "username", saved.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) return ResponseEntity.badRequest().body("username and password required");

        Usuario user = usuarioRepo.findByUsername(username);
        if (user == null) return ResponseEntity.status(401).body("invalid credentials");
        if (!passwordEncoder.matches(password, user.getPassword())) return ResponseEntity.status(401).body("invalid credentials");

        String token = jwtUtil.generateToken(user.getId(), 1000L * 60 * 60 * 24);
        return ResponseEntity.ok(Map.of("token", token, "id", user.getId(), "username", user.getUsername()));
    }
}
