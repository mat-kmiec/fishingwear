package pl.fishingwear.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT COUNT(p) FROM Post p WHERE p.category.id = :categoryId")
    int countPostsByCategory(@Param("categoryId") Long categoryId);
    int countByParentId(Long categoryId);
    @Query("SELECT c FROM BlogCategory c LEFT JOIN FETCH c.assignedModerator")
    List<BlogCategory> findAllWithModerators();

    @Query("SELECT c FROM BlogCategory c LEFT JOIN FETCH c.assignedModerator LEFT JOIN FETCH c.parent")
    List<BlogCategory> findAllFullyPopulated();


    @Modifying
    @Query("UPDATE BlogCategory bc SET bc.parent = NULL WHERE bc.parent.id = :categoryId")
    void disassociateSubCategories(@Param("categoryId") Long categoryId);
}
