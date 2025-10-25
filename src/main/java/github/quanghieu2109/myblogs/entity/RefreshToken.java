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
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String refreshToken;
    @ManyToOne(fetch = FetchType.LAZY)
    User user;
    Instant expiresAt;
    Instant createdAt;
}
