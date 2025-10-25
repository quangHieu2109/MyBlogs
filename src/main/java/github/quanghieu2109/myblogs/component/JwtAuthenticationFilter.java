package github.quanghieu2109.myblogs.component;

import github.quanghieu2109.myblogs.configuration.JwtAuthenticationEntryPoint;
import github.quanghieu2109.myblogs.exception.AppException;
import github.quanghieu2109.myblogs.exception.ErrorCode;
import github.quanghieu2109.myblogs.repository.UserRepository;
import github.quanghieu2109.myblogs.security.UserDetailsImpl;
import github.quanghieu2109.myblogs.service.AuthenticationService;
import github.quanghieu2109.myblogs.service.UserDetailImplService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @NonFinal
    @Value("${server.servlet.context-path}")
    String contextPath;
    MessageProvider messageProvider;
    AuthenticationService authService;
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    UserDetailImplService userDetailServiceImpl;
    private static final String[] PUBLIC_POST_ENDPOINTS =
            {
                    "/auth/**", "/users/**"
            };
    private static final String[] PUBLIC_GET_ENDPOINTS =
            {
                    "/auth/**", "/users"
            };
    private static final String[] SWAGGER_ENDPOINTS =
            {
                    "/swagger.html", "/swagger-ui/**",
                    "/swagger-ui.html", "/docs/**", "/v3/api-docs/**"
            };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            jwtAuthenticationEntryPoint.commence(request, response,
                    new BadCredentialsException("Missing or malformed Authorization header"));
            return;
        }

        final String token = authHeader.substring(7);

        try {
            if (!authService.verifyToken(token)) {
                throw new BadCredentialsException("Invalid token");
            }

            String username = authService.getClaimsSet(token).getSubject();
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetailsImpl userDetails = userDetailServiceImpl.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            jwtAuthenticationEntryPoint.commence(request, response, ex);
//            return;
        }
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        if (matches(path, SWAGGER_ENDPOINTS)) return true;
        else {
            // Kiểm tra GET
            if ("GET".equalsIgnoreCase(method)) {
                return matches(path, PUBLIC_GET_ENDPOINTS);
            }

            // Kiểm tra POST
            if ("POST".equalsIgnoreCase(method)) {
                return matches(path, PUBLIC_POST_ENDPOINTS);
            }
        }
        // Có thể mở rộng cho PUT, DELETE nếu cần
        return false;
    }

    private boolean matches(String path, String[] patterns) {
        for (String pattern : patterns) {
            if (pathMatcher().match(contextPath + pattern, path)) {
                return true;
            }
        }
        return false;
    }

    private PathMatcher pathMatcher() {
        return new AntPathMatcher(); // hỗ trợ pattern kiểu "/auth/**"
    }
}
