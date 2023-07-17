package africa.breej.africa.breej.repository;

import africa.breej.africa.breej.model.booking.BookingStatus;
import africa.breej.africa.breej.model.user.Role;
import africa.breej.africa.breej.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository  {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByRoleAndDeleted(Role role, boolean deleted);

    List<User> findAllBySpecialtyAndDeleted(String specialty, boolean deleted);

    List<User> findAllByBookingStatusAndDeleted(BookingStatus bookingStatus, boolean deleted);

    List<User> findByHashtagsContaining(String specialty);

    @Query("{$or: [{'hashtags': {$regex: ?0, $options: 'i'}}, {'hashtags': {$in: ?1}}]}")
    List<User> findByHashtags(String inputHashtag, Set<String> allHashtags);

    Optional<User> findByEmailAndDeleted(String email, boolean deleted);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndDeleted(String phoneNumber, boolean deleted);

    Boolean existsByPhoneNumberAndDeleted(String phoneNumber, boolean deleted);

    Boolean existsByBookingStatusAndDeleted(BookingStatus bookingStatus, boolean deleted);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndDeleted(String email, boolean deleted);

}
