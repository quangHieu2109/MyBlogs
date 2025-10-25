package github.quanghieu2109.myblogs.exception;

import github.quanghieu2109.myblogs.component.MessageProvider;
import github.quanghieu2109.myblogs.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String [] ATTRIBUTES = {"min", "max", "length"};

    @Autowired
    MessageProvider messageProvider;
    @ExceptionHandler(value = RuntimeException.class) // bắt lỗi runtime
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setSuccess(errorCode.isSuccess());
        response.setData(exception.getMessage());
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
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handlingRequestBodyInvalid(HttpMessageNotReadableException exception) {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setSuccess(errorCode.isSuccess());
        response.setData(messageProvider.getMessage(errorCode.getMessageKey()));
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(false);
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        //null validate
        if(enumKey.startsWith("NOTNULL_")) {
            errorCode = ErrorCode.NOT_NULL_FIELD;
            apiResponse.setCode(errorCode.getCode());
            apiResponse.setData(messageProvider.getMessage(errorCode.getMessageKey()+getNullField(enumKey)));
        }else{// other validate
            Map<String, Object> attributes = null;
            try {
                errorCode = ErrorCode.valueOf(enumKey);
                var constraintViolation =
                        exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
                attributes = constraintViolation.getConstraintDescriptor().getAttributes();
                apiResponse.setCode(errorCode.getCode());
                String responseMessage = messageProvider.getMessage(errorCode.getMessageKey());
                apiResponse.setData(
                        Objects.nonNull(attributes)
                                ? mapAttribute(responseMessage, attributes)
                                : responseMessage);
            } catch (IllegalArgumentException e) {

            }
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        for(String attribute : ATTRIBUTES){
            if(attributes.containsKey(attribute)){
                message =   message.replace("{" + attribute + "}", attributes.get(attribute).toString());
            }
        }

        return message;
    }
    private String getNullField(String defaultExceptinMsg){
        String fieldName ="";
        Pattern pattern = Pattern.compile("NOTNULL_\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(defaultExceptinMsg);
        if (matcher.find()) {
            fieldName = matcher.group(1);
        }
        return fieldName;
    }

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
