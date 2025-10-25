package github.quanghieu2109.myblogs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.cloudinary.Cloudinary;
@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {
    @Value("${cloudinary.upload_preset}")
    String uploadPreset;
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("upload_preset", uploadPreset); // ✅ thêm preset
            log.info("upload preset: {}", uploadPreset);
            return cloudinary.uploader().upload(file.getBytes(), Map.of()).get("url").toString();
        } catch (IOException e) {

            throw new RuntimeException("Upload failed", e);
        }
    }
}
