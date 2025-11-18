package pl.fishingwear.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.fishingwear.blog.dto.BlogCategorySidebarDto;
import pl.fishingwear.blog.model.BlogCategory;

import java.util.List;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {

    @Query("SELECT new pl.fishingwear.blog.dto.BlogCategorySidebarDto( " +
            "c.id, " +
            "c.name, " +
            "SUM(CASE WHEN p.status = 'PUBLISHED' THEN 1L ELSE 0L END) ) " +
            "FROM BlogCategory c " +
            "LEFT JOIN c.posts p " +
            "GROUP BY c.id, c.name " +
            "ORDER BY c.name ASC")
    List<BlogCategorySidebarDto> findAllCategoriesWithPostCount();
}
