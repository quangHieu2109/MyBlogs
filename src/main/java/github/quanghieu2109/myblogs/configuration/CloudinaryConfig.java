package github.quanghieu2109.myblogs.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud.name}")
    String cloudName;
    @Value("${cloudinary.aip.key}")
    String aipKey;
    @Value("${cloudinary.aip.secret}")
    String aipSecret;
    @Bean
    public Cloudinary cloudinary(){
        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", aipKey);
        config.put("api_secret", aipSecret);
//        config.put("secure", true);
        return new Cloudinary(config);
    }
}
