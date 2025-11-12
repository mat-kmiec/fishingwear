package pl.fishingwear.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.fishingwear.cart.service.CartMergeService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CartMergeService cartMergeService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String email;

        if (authentication instanceof OAuth2AuthenticationToken token) {
            OAuth2User oauthUser = token.getPrincipal();
            email = oauthUser.getAttribute("email");
            log.info("Logowanie OAuth2 wykryte. Email: {}", email);
        } else {
            email = authentication.getName();
            log.info("Logowanie przez formularz wykryte. Email: {}", email);
        }

        if (email == null) {
            log.error("Nie można ustalić adresu email. Przerywanie scalania koszyka.");
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        final String finalEmail = email;

        try {
            cartMergeService.mergeCartsOnLogin(finalEmail, request, response);
        } catch (Exception e) {
            log.error("Błąd podczas scalania koszyków dla użytkownika {}", finalEmail, e);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}