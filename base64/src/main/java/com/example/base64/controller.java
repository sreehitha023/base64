package com.example.base64;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;import java.io.IOException;
import java.nio.file.Files;import java.nio.file.Path;import java.nio.file.StandardCopyOption;import java.util.Base64;
@RestController
@RequestMapping("/base64")
class Controller {
    private static final String UPLOAD_DIR = "uploads";

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        Path imagePath = Path.of(uploadDir.getAbsolutePath(), file.getOriginalFilename());
        Files.copy(file.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        return "Image uploaded successfully.";
    }

    @GetMapping(value = "/download/{filename}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String downloadAndConvertToBase64(@PathVariable String filename) throws IOException {
        String imagePath = UPLOAD_DIR + File.separator + filename;
        byte[] imageBytes = Files.readAllBytes(Path.of(imagePath));
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        return "{\"base64Image\": \"" + base64Image + "\"}";
    }
}
