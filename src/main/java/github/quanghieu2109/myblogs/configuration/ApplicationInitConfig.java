package github.quanghieu2109.myblogs.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;


@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j // annotation để sử dụng log
public class ApplicationInitConfig {
//    @Autowired // có thể dùng @RequiredArgsConstructor để thay thế
//    PasswordEncoder passwordEncoder;
//    @Bean
//    ApplicationRunner applicationRunner(UserRepository userRepository){ // được chạy mỗi khi chương trình dc start
//        return args -> {
//            if(userRepository.findByUsername("admin").isEmpty()){
//                User user = User.builder()
//                        .username("admin")
//                        .avtPath("https://i.imgur.com/W60xqJf.png")
//                        .fullName("At Van Min")
//                        .email("admin@min.ad")
//                        .password(passwordEncoder.encode("123123"))
//                        .role(Role.ADMIN.name())
//                        .build();
//                userRepository.save(user);
//                log.warn("Default admin user has been created with username: admin and password: 123");
//            }
//        };
//    }
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
    @Bean
        // định nghĩa 1 Bean để dùng @Autowired hoặc @RequiredArgsConstructor
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
