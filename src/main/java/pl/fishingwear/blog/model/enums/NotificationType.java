package pl.fishingwear.blog.model.enums;

public enum NotificationType {
    COMMENT_PENDING,   // Nowy komentarz czeka
    POST_SUBMITTED,    // Użytkownik wysłał post do akceptacji
    CATEGORY_ASSIGNED, // Zostałeś moderatorem kategorii
    SYSTEM_ALERT       // Komunikaty techniczne
}