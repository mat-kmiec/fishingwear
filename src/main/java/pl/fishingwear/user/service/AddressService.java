package pl.fishingwear.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fishingwear.user.model.Address;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.AddressRepository;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public void save(Address address) {
        addressRepository.save(address);
    }

    public void deleteByIdAndUser(Long id, User user) {
        addressRepository.findById(id).ifPresent(address -> {
            if (address.getUser().getId().equals(user.getId())) {
                addressRepository.delete(address);
            }
        });
    }
}
