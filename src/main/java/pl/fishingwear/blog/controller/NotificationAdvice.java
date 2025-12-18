package pl.fishingwear.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.fishingwear.blog.service.NotificationService;

@ControllerAdvice
@RequiredArgsConstructor
public class NotificationAdvice {

    private final NotificationService notificationService;

    @ModelAttribute("unreadNotificationsCount")
    public long getUnreadCount() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated() &&
                    auth.getPrincipal() != null && !"anonymousUser".equals(auth.getName())) {

                return notificationService.countUnreadNotifications(auth.getName());
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
}