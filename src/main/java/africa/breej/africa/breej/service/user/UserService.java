package africa.breej.africa.breej.service.user;

import africa.breej.africa.breej.model.auth.UserOverview;
import africa.breej.africa.breej.model.booking.BookingStatus;
import africa.breej.africa.breej.model.user.Role;
import africa.breej.africa.breej.model.user.User;
import africa.breej.africa.breej.payload.auth.SignUpRequest;
import africa.breej.africa.breej.payload.user.UpdateUserPasswordRequest;
import africa.breej.africa.breej.payload.user.UpdateUserProfileRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User createUser(SignUpRequest users);

    Optional<User> fetchUserById(String id);

    Optional<User> fetchUserByPhoneNumber(String phoneNumber);

    List<User> fetchAllUsers();

    List<User> fetchUsersByRole(String userId, Role role);

    User updatePassword(String id, UpdateUserPasswordRequest updateUserPinRequest);

    User updateUser(String id, UpdateUserProfileRequest userProfileRequest);

    boolean deleteUser(String id);

    Page<User> fetchUserByFilters(HashMap<String, Object> filters, LocalDateTime from, LocalDateTime to, PageRequest pageRequest);

    UserOverview fetchTotalUsers(String userId, LocalDateTime from, LocalDateTime to);

    List<User> searchTutorsByHashtag(String userId, String hashtag);

    List<User> fetchUsersBySpecialty(String userId, String specialty);

    List<User> searchUsersBySpecialty(String userId, String hashtag);

    List<User> fetchTutorsForBooking(String userId, String hashtag);

    List<User> fetchUsersByBookingStatus(String id, BookingStatus bookingStatus);
}
