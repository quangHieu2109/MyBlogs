package github.quanghieu2109.myblogs.component;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

@Component
@Slf4j
public class LanguageFilter implements Filter {

    @Autowired
    private MessageProvider MessageProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String acceptLanguage = httpRequest.getHeader("Accept-Language");
        acceptLanguage = acceptLanguage != null ? acceptLanguage : "";
        MessageProvider.setLocale(acceptLanguage);

        try {
            chain.doFilter(request, response);
        } finally {
            MessageProvider.clear(); // ✅ Xóa locale sau khi xử lý xong
        }

        log.info("URI: '{}' Method: {} Response status: {}",
                httpRequest.getRequestURI(), httpRequest.getMethod(), httpResponse.getStatus());
    }

}