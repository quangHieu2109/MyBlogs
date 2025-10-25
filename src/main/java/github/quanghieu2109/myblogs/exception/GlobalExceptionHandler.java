package github.quanghieu2109.myblogs.exception;

import github.quanghieu2109.myblogs.component.MessageProvider;
import github.quanghieu2109.myblogs.dto.response.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    MessageProvider messageProvider;
    @ExceptionHandler(value = RuntimeException.class) // bắt lỗi runtime
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setSuccess(errorCode.isSuccess());
        response.setData(exception.getMessage());
//        exception.printStackTrace();
        log.error(exception.toString());
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
    @ExceptionHandler(value = AppException.class) // bắt lỗi tự định nghĩa
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setSuccess(errorCode.isSuccess());
        response.setData(exception.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(response);

    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class) // bắt lỗi validation
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode= ErrorCode.valueOf(enumKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setSuccess(errorCode.isSuccess());
        response.setData(messageProvider.getMessage(errorCode.getMessageKey()));
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
//    @ExceptionHandler(value = ConstraintViolationException.class) // bắt lỗi validation
//    ResponseEntity<ApiResponse> handlingMConstraintViolationException(ConstraintViolationException exception){
//        String enumKey = exception.getMessage();
//        ErrorCode errorCode = ErrorCode.INVALID_KEY;
//        try {
//            errorCode= ErrorCode.valueOf(enumKey);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        ApiResponse<String> response = new ApiResponse<>();
//        response.setCode(errorCode.getCode());
//        response.setSuccess(errorCode.isSuccess());
//        response.setData(errorCode.getMessage());
//        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
//    }
    @ExceptionHandler(value = AccessDeniedException.class)// bắt lỗi 403
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()

                        .code(errorCode.getCode())
                        .isSuccess(errorCode.isSuccess())
                        .data(messageProvider.getMessage(errorCode.getMessageKey()))
                        .build()
        );
    }
    @ExceptionHandler(value = MissingRequestHeaderException.class)// bắt lỗi 403
    ResponseEntity<ApiResponse> handlingMissingRequestHeaderException(MissingRequestHeaderException exception){
        ErrorCode errorCode = ErrorCode.ACCESS_TOKEN_INVALID;
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()

                        .code(errorCode.getCode())
                        .isSuccess(errorCode.isSuccess())
                        .data(messageProvider.getMessage(errorCode.getMessageKey()))
                        .build()
        );
    }
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        // Xử lý lỗi (ví dụ: ghi log, trả về thông báo lỗi)
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()

                        .code(errorCode.getCode())
                        .isSuccess(errorCode.isSuccess())
                        .data(messageProvider.getMessage(errorCode.getMessageKey()))
                        .build()
        );
    }
}
