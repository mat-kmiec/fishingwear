package pl.fishingwear.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.fishingwear.user.model.Address;
import pl.fishingwear.user.model.User;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUser(User user);
}
