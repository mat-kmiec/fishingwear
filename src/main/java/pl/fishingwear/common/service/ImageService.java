package pl.fishingwear.common.service;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    public String saveImage(MultipartFile file, String UPLOAD_DIR, boolean generateMainFile, int width, int height, double qualityThumbnail, double qualityMainFile, boolean crop) {
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

                generateThumbnail(mainFile, thumbFile, width, height, qualityThumbnail, crop);

            } else {
                generateThumbnail(file, thumbFile, width, height, qualityThumbnail, crop);
            }

            return filename;

        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas zapisu pliku: " + e.getMessage(), e);
        }
    }

    private void generateThumbnail(Object source, File destination, int targetWidth, int targetHeight, double quality, boolean crop) throws IOException {

        BufferedImage originalImage;
        if (source instanceof File) {
            originalImage = ImageIO.read((File) source);
        } else {
            originalImage = ImageIO.read(((MultipartFile) source).getInputStream());
        }

        if (crop) {
            double widthRatio = (double) targetWidth / originalImage.getWidth();
            double heightRatio = (double) targetHeight / originalImage.getHeight();
            double scale = Math.max(widthRatio, heightRatio);

            BufferedImage resizedImage = Thumbnails.of(originalImage)
                    .scale(scale)
                    .asBufferedImage();

            Thumbnails.of(resizedImage)
                    .sourceRegion(Positions.CENTER, targetWidth, targetHeight)
                    .size(targetWidth, targetHeight)
                    .keepAspectRatio(false)
                    .outputQuality(quality)
                    .toFile(destination);
        } else {
            Thumbnails.of(originalImage)
                    .size(targetWidth, targetHeight)
                    .keepAspectRatio(true)
                    .outputQuality(quality)
                    .toFile(destination);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return ".jpg";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}