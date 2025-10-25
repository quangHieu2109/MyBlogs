package github.quanghieu2109.myblogs.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHENTICATED(401, "unauthenticated", HttpStatus.UNAUTHORIZED),
    INVALID_REQUEST_BODY(4000, "incorrect.request.body", HttpStatus.BAD_REQUEST),
    NOT_FOUND(4001, "not.found", HttpStatus.BAD_REQUEST),
    NOT_NULL_FIELD(4001, "not.null.field.", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID_SIZE(4002, "user.username.invalid.size", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID_SIZE(4003, "user.password.invalid.size", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID_FORMAT(4004, "user.email.invalid", HttpStatus.BAD_REQUEST),
    PHONENUMBER_INVALID(4005, "user.phone_number.invalid", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(4006, "user.username.duplicate", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(4007, "user.email.duplicate", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(4008, "user.phone_number.duplicate", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTS(4009, "user.not.exists", HttpStatus.BAD_REQUEST),
    PASSWORD_INCORRECT(4010, "password.incorrect", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_FOUND(4011, "refresh.token.not.found", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_INVALID(4012, "refresh.token.invalid", HttpStatus.BAD_REQUEST),
    ACCESS_TOKEN_INVALID(4013, "access.token.invalid", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(4014, "user.token.invalid", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTS(4087, "email.not.exists", HttpStatus.BAD_REQUEST),
    ROLE_INVALID(4103, "user.role.invalid", HttpStatus.BAD_REQUEST),
    CANT_CHANGE_ADMIN_ROLE(4104, "user.role.admin", HttpStatus.BAD_REQUEST),
    ROLE_NO_HAVE_CHANGE(4105, "user.role.no.have.change", HttpStatus.BAD_REQUEST),


    INVALID_KEY(5002, "message.key.invalid", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_EXCEPTION(500, "uncategorized.exception", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVER_ERROR(5001, "server.error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(403, "unauthorized", HttpStatus.FORBIDDEN);

    private  int code;
    private  String messageKey;
    private  HttpStatus statusCode;

    ErrorCode(int code, String messageKey, HttpStatus statusCode) {
        this.code = code;
        this.messageKey = messageKey;
        this.statusCode = statusCode;
    }
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }
    public boolean isSuccess(){
        return false;
    }
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}