package pl.fishingwear.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fishingwear.product.model.Size;
import pl.fishingwear.product.repository.SizeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeService {

    private final SizeRepository sizeRepository;

    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }
}
