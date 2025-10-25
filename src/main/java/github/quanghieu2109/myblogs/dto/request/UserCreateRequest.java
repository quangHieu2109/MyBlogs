package github.quanghieu2109.myblogs.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    @NotNull(message = "NOTNULL_{username}")
    @Size(min = 6, max = 12, message = "USERNAME_INVALID_SIZE")
    String username;
    @NotNull(message = "NOTNULL_{email}")
    @Email(message = "EMAIL_INVALID_FORMAT")
    String email;
    @NotNull(message = "NOTNULL_{password}")
    @Size(min = 6, max = 20, message = "PASSWORD_INVALID_SIZE")
    String password;
    @NotNull(message = "NOTNULL_{full_name}")
    String fullName;
}
