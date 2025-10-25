package github.quanghieu2109.myblogs.configuration;

import github.quanghieu2109.myblogs.component.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration // Đánh dấu lớp này là nguồn cấu hình cho Spring
@EnableWebSecurity // Kích hoạt các tính năng bảo mật của Spring Security
@EnableMethodSecurity
public class SecurityConfig implements WebMvcConfigurer {
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    // Mảng chứa các endpoint công khai không yêu cầu xác thực
    private static final String[] PUBLIC_POST_ENDPOINTS =
            {"/auth/**", "/users/**"
            };
    private static final String[] PUBLIC_GET_ENDPOINTS =
            {
                    "/users/**", "/auth/**"
            };
    private static final String[] SWAGGER_ENDPOINTS =
            {
                    "/swagger.html", "/swagger-ui/**",
                    "/swagger-ui.html", "/docs/**", "/v3/api-docs/**"
            };


    // Lấy giá trị của "jwt.signerKey" từ tệp cấu hình
    @Value("${jwt.signerKey}")
    private String signerKey;

    // Phương thức cấu hình SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // Cấu hình quyền truy cập cho các yêu cầu HTTP
        httpSecurity.authorizeHttpRequests(request ->
                request
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll() // Cho phép tất cả yêu cầu POST đến PUBLIC_ENDPOINTS
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers("*").permitAll()
                        .anyRequest().authenticated()
        ); // Tất cả các yêu cầu khác đều cần xác thực
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        // Tắt tính năng CSRF để tránh lỗi 403
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        httpSecurity.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        // Cấu hình máy chủ tài nguyên OAuth2 để sử dụng JWT
//        httpSecurity.oauth2ResourceServer(oauth2 ->
//                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
//                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
        // điều hướng khi auth thất bại (401)
//                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)


//        ); // Chỉ định bộ giải mã JWT
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build(); // Xây dựng và trả về SecurityFilterChain
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // vì đã thêm role vào token với tên là scope
        // nên nếu muốn đổi thì set lại prefix
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    // Phương thức tạo bean cho JwtDecoder
    @Bean
    JwtDecoder jwtDecoder() {
        // Tạo khóa bí mật từ signerKey với thuật toán HS512
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");

        // Tạo và trả về bộ giải mã JWT với khóa bí mật và thuật toán
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec) // Sử dụng khóa bí mật
                .macAlgorithm(MacAlgorithm.HS512) // Chỉ định thuật toán mã hóa
                .build(); // Xây dựng và trả về JwtDecoder
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
