package github.quanghieu2109.myblogs.exception;

import github.quanghieu2109.myblogs.component.MessageProvider;
import lombok.extern.slf4j.Slf4j;
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppException extends RuntimeException {
    private ErrorCode errorCode;

    public AppException(MessageProvider messageProvider, ErrorCode errorCode) {
        super(messageProvider.getMessage(errorCode.getMessageKey())); // Lấy thông điệp từ MessageProvider
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}