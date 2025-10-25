package github.quanghieu2109.myblogs.dto.response.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    @Size(min = 6, max = 12, message = "USERNAME_INVALID")
    String username;
    @Email(message = "EMAIL_INVALID")
    String email;
    String password;
    String fullName;
}
