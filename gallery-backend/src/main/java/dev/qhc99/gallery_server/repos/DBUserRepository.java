package dev.qhc99.gallery_server.repos;

import dev.qhc99.gallery_server.data_class.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Long> {

  Optional<DBUser> findByEmail(String email);

  Boolean existsByEmail(String email);

}
