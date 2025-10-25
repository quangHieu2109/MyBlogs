package github.quanghieu2109.myblogs.service;

import github.quanghieu2109.myblogs.component.MessageProvider;
import github.quanghieu2109.myblogs.dto.response.request.UserCreateRequest;
import github.quanghieu2109.myblogs.dto.response.response.ApiResponse;
import github.quanghieu2109.myblogs.entity.User;
import github.quanghieu2109.myblogs.exception.AppException;
import github.quanghieu2109.myblogs.exception.ErrorCode;
import github.quanghieu2109.myblogs.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSevice {
    UserRepository userRepo;
    PasswordEncoder passwordEncoder;
    MessageProvider messageProvider;
    public ApiResponse register(UserCreateRequest request){
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .build();
        try {

            userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            Pattern pattern = Pattern.compile("Duplicate entry '(.+?)' for key");
            Matcher matcher = pattern.matcher(e.getMessage());
            if (matcher.find()) {
                throw new AppException(messageProvider, getDuplicateEntryError(user, e.getMessage())); // phần "string"
            }
            log.error(e.getMessage());
        }
        return new ApiResponse("Register successfully");
    }
    private ErrorCode getDuplicateEntryError(User user, String message){
        Map<String, ErrorCode> uniqueKeys = new HashMap<>();
        uniqueKeys.put( user.getUsername(),  ErrorCode.USERNAME_EXISTED);
        uniqueKeys.put(user.getEmail(), ErrorCode.EMAIL_EXISTED);
        Pattern pattern = Pattern.compile("Duplicate entry '(.+?)' for key");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return uniqueKeys.get(matcher.group(1));  // phần "string"
        }
        return null;
    }
}
