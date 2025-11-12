package pl.fishingwear.auth.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pl.fishingwear.auth.model.AuthProvider;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        String name = null;
        AuthProvider provider = AuthProvider.LOCAL;

        // === Google ===
        if ("google".equals(registrationId)) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            provider = AuthProvider.GOOGLE;
        }

        // === GitHub ===
        else if ("github".equals(registrationId)) {
            name = (String) attributes.get("login");
            email = (String) attributes.get("email");
            provider = AuthProvider.GITHUB;

            if (email == null) {
                String token = userRequest.getAccessToken().getTokenValue();
                email = fetchEmailFromGithub(token);
            }
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("Nie udało się pobrać adresu e-mail użytkownika OAuth2");
        }

        String finalEmail = email.toLowerCase().trim();
        AuthProvider finalProvider = provider;

        userRepository.findByEmail(finalEmail)
                .map(user -> {
                    if (user.getAuthProvider() == null) {
                        user.setAuthProvider(finalProvider);
                        userRepository.save(user);
                    }
                    return user;
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(finalEmail);
                    newUser.setPassword("OAUTH2");
                    newUser.setRole("USER");
                    newUser.setAuthProvider(finalProvider);
                    return userRepository.save(newUser);
                });

        return oAuth2User;
    }

    private String fetchEmailFromGithub(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> emails = response.getBody();
        if (emails == null || emails.isEmpty()) return null;

        return (String) emails.stream()
                .filter(e -> Boolean.TRUE.equals(e.get("primary")))
                .map(e -> e.get("email"))
                .findFirst()
                .orElse(emails.getFirst().get("email"));
    }
}
