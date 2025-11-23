package pl.fishingwear.theme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.fishingwear.theme.model.Theme;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    Optional<Theme> findThemeById(Long id);
    List<Theme> findAllByOrderByIdAsc();
}
