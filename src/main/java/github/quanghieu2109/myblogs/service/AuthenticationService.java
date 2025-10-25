package github.quanghieu2109.myblogs.service;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import github.quanghieu2109.myblogs.component.MessageProvider;
import github.quanghieu2109.myblogs.dto.response.response.ApiResponse;
import github.quanghieu2109.myblogs.dto.response.request.LoginRequest;
import github.quanghieu2109.myblogs.dto.response.response.LoginResponse;
import github.quanghieu2109.myblogs.entity.LogoutToken;
import github.quanghieu2109.myblogs.entity.RefreshToken;
import github.quanghieu2109.myblogs.entity.User;
import github.quanghieu2109.myblogs.exception.AppException;
import github.quanghieu2109.myblogs.exception.ErrorCode;
import github.quanghieu2109.myblogs.repository.LogoutTokenRepository;
import github.quanghieu2109.myblogs.repository.RefreshTokenRepository;
import github.quanghieu2109.myblogs.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    MessageProvider messageProvider;
    LogoutTokenRepository logoutRepo;
    RefreshTokenRepository refreshRepo;
    UserRepository  userRepo;
    PasswordEncoder passwordEncoder;
    @NonFinal // đánh dấu để không tự tạo bở các annotation khác
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESH_DURATION;
    public ApiResponse login(LoginRequest loginRequest) {
        User user = userRepo.findByUsername(loginRequest.getUsername()).orElseThrow(() ->
                new AppException(messageProvider, ErrorCode.USER_NOT_EXISTS));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) throw
                new AppException(messageProvider, ErrorCode.PASSWORD_INCORRECT);
        String accessToken = generateToken(user);
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken refreshTokenObject = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(user)
                .expiresAt(Instant.now().plus(REFRESH_DURATION, ChronoUnit.SECONDS))
                .build();
        refreshRepo.save(refreshTokenObject);
        return new ApiResponse(new LoginResponse(accessToken, refreshToken));
    }
    public ApiResponse logout(){
        String token = getCurrentToken();
        String tokenId = getClaimsSet(token).getJWTID();
        LogoutToken logoutToken = LogoutToken.builder()
                .tokenId(tokenId)
                .logoutTime(Instant.now())
                .build();
        logoutRepo.save(logoutToken);
        return new ApiResponse("Logout successful");
    }
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())// đại diện cho người dùng đang đăng nhập
                .issuer("quangHiu.com")// người cấp token
                .issueTime(new Date(Instant.now().toEpochMilli())) // thời gian phát hành token này
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli())) // thời gian hết hạn của token
                // thep nguyên tắc của oauth2 thì role có name là scope
                .claim("scope", user.getRole()) // thêm claim tự do
                .claim("userId", user.getId())
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {

            throw new RuntimeException(e);
        }

    }

    public long getUserId(String token) {
        try {
            return getClaimsSet(token).getLongClaim("userId");
        } catch (Exception e) {

            throw new AppException(messageProvider, ErrorCode.SERVER_ERROR);
        }
    }

    public boolean verifyToken(String token) {
        try {
//            token = token.replace("Bearer ", "");
            var claimsSet = getClaimsSet(token);
            //check expirationTime
            if (claimsSet.getExpirationTime().before(new Date(Instant.now().toEpochMilli()))) {
                return false;
            }
            //check logout
            if (logoutRepo.findByTokenId(claimsSet.getJWTID()).isPresent()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public JWTClaimsSet getClaimsSet(String token) {
        try {
            token = token.replace("Bearer ", "");
            JWSObject jwsObject = JWSObject.parse(token);
            MACVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
            if (jwsObject.verify(verifier)) {
                // Lấy claims từ token
                JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
                return claims;
            } else {
                throw new AppException(messageProvider, ErrorCode.ACCESS_TOKEN_INVALID);
            }

        } catch (Exception e) {
            throw new AppException(messageProvider, ErrorCode.ACCESS_TOKEN_INVALID);
        }
    }
    public static String getCurrentToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getCredentials() instanceof String) {
            return (String) auth.getCredentials();
        }
        return null;
    }
}
