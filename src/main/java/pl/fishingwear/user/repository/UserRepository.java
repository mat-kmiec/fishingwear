package pl.fishingwear.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.fishingwear.user.model.Role;
import pl.fishingwear.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.addresses WHERE u.id = :id")
    Optional<User> findByIdWithAddresses(@Param("id") Long id);

    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.email) LIKE %:search% " +
            "OR LOWER(u.firstName) LIKE %:search% " +
            "OR LOWER(u.lastName) LIKE %:search%")
    Page<User> searchUsers(@Param("search") String search, Pageable pageable);

    List<User> findByRole(Role role);

    boolean existsByEmail(String email);

    List<User> findByRoleIn(List<Role> roles);
    List<User> findAllBySelectedThemeId(Long themeId);
}

