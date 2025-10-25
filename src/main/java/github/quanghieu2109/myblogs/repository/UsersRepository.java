package github.quanghieu2109.myblogs.repository;

import github.quanghieu2109.myblogs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

}
