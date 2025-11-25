package pl.fishingwear.common.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    public String saveImage(MultipartFile file, String UPLOAD_DIR, boolean generateMainFile, int width, int height, double qualityThumbnail, double qualityMainFile) {
        try {
            String originalExtension = getFileExtension(file.getOriginalFilename());
            String uuid = UUID.randomUUID().toString();
            String filename = uuid + originalExtension;

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            File mainFile = uploadPath.resolve(filename).toFile();
            File thumbFile = uploadPath.resolve("thumb_" + filename).toFile();
            if (generateMainFile) {
                Thumbnails.of(file.getInputStream())
                        .scale(1.0)
                        .outputQuality(qualityMainFile)
                        .toFile(mainFile);

                Thumbnails.of(mainFile)
                        .size(width, height)
                        .keepAspectRatio(true)
                        .outputQuality(qualityThumbnail)
                        .toFile(thumbFile);
            } else {
                Thumbnails.of(file.getInputStream())
                        .size(width, height)
                        .keepAspectRatio(true)
                        .outputQuality(qualityThumbnail)
                        .toFile(thumbFile);

            }

            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas zapisu pliku: " + e.getMessage(), e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return ".jpg";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
