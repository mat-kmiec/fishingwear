package pl.fishingwear.slider.dto;

import java.time.LocalDateTime;

public record SliderImageDto(Long id, String fileName, String originalFileName, LocalDateTime createdAt) {}
