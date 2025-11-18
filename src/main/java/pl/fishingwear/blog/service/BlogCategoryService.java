package pl.fishingwear.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fishingwear.blog.dto.BlogCategorySidebarDto;
import pl.fishingwear.blog.model.BlogCategory;
import pl.fishingwear.blog.model.enums.PostStatus;
import pl.fishingwear.blog.repository.BlogCategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogCategoryService {

    private final BlogCategoryRepository categoryRepository;


    public List<BlogCategorySidebarDto> getAllCategoriesWithPostCount() {
        return categoryRepository.findAllCategoriesWithPostCount();
    }
}