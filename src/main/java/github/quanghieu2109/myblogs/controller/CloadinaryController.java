package github.quanghieu2109.myblogs.controller;

import com.cloudinary.Cloudinary;
import github.quanghieu2109.myblogs.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloadinaryController {
    CloudinaryService cloudinaryService;
    @PostMapping("/image")
    public String uploadImage(@RequestParam("file") MultipartFile file){
        log.info("Uploading image...");
        return cloudinaryService.uploadImage(file);
    }
}
