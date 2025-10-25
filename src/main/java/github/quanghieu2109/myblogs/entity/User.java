package github.quanghieu2109.myblogs.entity;

import github.quanghieu2109.myblogs.enums.AccountStatus;
import github.quanghieu2109.myblogs.enums.Genders;
import github.quanghieu2109.myblogs.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(unique = true, nullable = false, length = 50)
    String username;
    @Column
    String password;
    @Column(unique = true, nullable = false, length = 100)
    String email;
    @Column
    String avtPath;
    @Column
    String fullName;
    @Enumerated(EnumType.STRING)
    Genders gender;
    @Enumerated(EnumType.STRING)
    Roles role;
    @Column
    Date dob;
    @Column
    @Enumerated(EnumType.STRING)
    AccountStatus accountStatus;
    @Column
    Instant createdAt;
    @Column
    Instant updatedAt;


}
