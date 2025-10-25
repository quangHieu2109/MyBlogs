package github.quanghieu2109.myblogs.repository;

import github.quanghieu2109.myblogs.entity.LogoutToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogoutTokenRepository extends JpaRepository<LogoutToken,Long> {

    Optional<LogoutToken> findByTokenId(String tokenId);
}
