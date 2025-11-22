package pl.fishingwear.admin.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.fishingwear.admin.dto.BlogCategoryDto;
import pl.fishingwear.admin.mapper.BlogCategoryMapper;
import pl.fishingwear.blog.repository.BlogCategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminCategoryService {

    private BlogCategoryRepository blogCategoryRepository;


    public List<BlogCategoryDto> getAllCategories() {
        return blogCategoryRepository.findAll().stream().map(BlogCategoryMapper::toDto).toList();
    }

}
