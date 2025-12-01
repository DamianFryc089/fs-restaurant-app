package fun.kociarnia.bazy_danych_projekt.auth;

import fun.kociarnia.bazy_danych_projekt.JwtService;
import fun.kociarnia.bazy_danych_projekt.exception.AuthorizationFailedException;
import fun.kociarnia.bazy_danych_projekt.user.User;
import fun.kociarnia.bazy_danych_projekt.user.UserService;
import fun.kociarnia.bazy_danych_projekt.user.dto.UserDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userService.getUserByUsername(request.getUsername());
            String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole().name());

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60*60);
            cookie.setAttribute("SameSite", "strict");
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("status", "ok"));
        } catch (AuthenticationException e) {
            throw new AuthorizationFailedException("Invalid username or password");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
        String token = jwtService.findJWTTokenInCookies(request);
        if (token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            Claims claims = jwtService.extractAllClaims(token);
            Long userId = claims.get("id", Long.class);
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(UserDTO.fromEntity(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
