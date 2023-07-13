package africa.breej.africa.breej.repository;

import africa.breej.africa.breej.model.user.Role;
import africa.breej.africa.breej.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository  {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByRoleAndDeleted(Role role, boolean deleted);

    Optional<User> findByEmailAndDeleted(String email, boolean deleted);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndDeleted(String phoneNumber, boolean deleted);

    Boolean existsByPhoneNumberAndDeleted(String phoneNumber, boolean deleted);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndDeleted(String email, boolean deleted);

}
