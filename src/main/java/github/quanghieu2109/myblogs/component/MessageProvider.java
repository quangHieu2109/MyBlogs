package github.quanghieu2109.myblogs.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
public class MessageProvider {

    @Autowired
    private MessageSource messageSource;

    private static final Map<String, Locale> mapLocal = Map.of(
            "vi", new Locale("vi", "VN"),
            "en", Locale.ENGLISH
    );

    private static final Locale defaultLocale = new Locale("vi", "VN");

    // ✅ ThreadLocal để lưu locale riêng cho từng request
    private static final ThreadLocal<Locale> localeHolder = ThreadLocal.withInitial(() -> defaultLocale);

    public void setLocale(String language) {
        Locale newLocale = mapLocal.getOrDefault(language, defaultLocale);
        localeHolder.set(newLocale);
    }

    public Locale getLocale() {
        return localeHolder.get();
    }

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, getLocale());
    }

    // ✅ Xóa locale sau khi xử lý xong request
    public void clear() {
        localeHolder.remove();
    }
}
