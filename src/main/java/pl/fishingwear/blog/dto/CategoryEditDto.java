package pl.fishingwear.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Setter;

public class CategoryEditDto {

    // Id jest wymagane do identyfikacji edytowanej kategorii
    @Setter
    private Long id;

    @Setter
    @NotBlank(message = "Nazwa kategorii nie może być pusta.")
    @Size(min = 2, max = 100, message = "Nazwa musi mieć od 2 do 100 znaków.")
    private String name;
    private Long parentCategoryId;
    private Long moderatorId;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = (parentCategoryId != null && parentCategoryId == 0) ? null : parentCategoryId;
    }

    public Long getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(Long moderatorId) {
        this.moderatorId = (moderatorId != null && moderatorId == 0) ? null : moderatorId;
    }
}