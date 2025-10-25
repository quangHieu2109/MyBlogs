package github.quanghieu2109.myblogs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "logout_tokens")
public class LogoutToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(unique = true, nullable = false)
    String tokenId;
    Instant logoutTime;

}
