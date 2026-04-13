package pro.hiking.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pro.hiking.auth.entity.User;
import pro.hiking.auth.repository.UserRepository;
import pro.hiking.auth.service.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository; // НОВОЕ: инжектим репозиторий

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // НОВОЕ: находим пользователя в базе по email, чтобы получить его ID
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found after Google login"));

        // ИЗМЕНЕНО: генерируем токен с email и id
        String token = jwtService.generateToken(email, user.getId());

        // Редиректим на наш HTML файл на домене shyn-api.site
        String targetUrl = UriComponentsBuilder.fromUriString("shynapp://login-callback")
                .queryParam("token", token)
                .build()
                .toUriString();
        //http://localhost:8080/swagger-ui/index.html

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}