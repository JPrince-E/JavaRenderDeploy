package africa.breej.africa.breej.controller;

import africa.breej.africa.breej.exception.ConflictException;
import africa.breej.africa.breej.exception.NotAcceptableException;
import africa.breej.africa.breej.exception.NotFoundException;
import africa.breej.africa.breej.model.auth.AuthProvider;
import africa.breej.africa.breej.model.auth.UserOverview;
import africa.breej.africa.breej.model.booking.BookingStatus;
import africa.breej.africa.breej.model.user.Gender;
import africa.breej.africa.breej.model.user.Role;
import africa.breej.africa.breej.model.user.User;
import africa.breej.africa.breej.payload.Response;
import africa.breej.africa.breej.payload.user.UpdateUserPasswordRequest;
import africa.breej.africa.breej.payload.user.UpdateUserProfileRequest;
import africa.breej.africa.breej.security.CurrentUser;
import africa.breej.africa.breej.security.UserPrincipal;
import africa.breej.africa.breej.service.user.UserService;
import africa.breej.africa.breej.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.fetchAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
       return userService.fetchUserById(userPrincipal.getId()).get();
    }

    @GetMapping("/tutors/{role}")
    public ResponseEntity<List<User>> getAllTutors(@CurrentUser UserPrincipal userPrincipal,
                                                   @PathVariable(value = "role") final Role role,
                                                   @RequestParam(value = "page", defaultValue = "0") final int page,
                                                   @RequestParam(value = "limit", defaultValue = "50") final int limit) {
        List<User> users = userService.fetchUsersByRole(userPrincipal.getId(), role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/tutors/{specialty}")
    public ResponseEntity<List<User>> getTutorsBySpecialty(@CurrentUser UserPrincipal userPrincipal,
                                                   @PathVariable(value = "specialty") final String specialty,
                                                   @RequestParam(value = "page", defaultValue = "0") final int page,
                                                   @RequestParam(value = "limit", defaultValue = "50") final int limit) {
        List<User> users = userService.fetchUsersBySpecialty(userPrincipal.getId(), specialty);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/tutors/{bookingStatus}")
    public ResponseEntity<List<User>> getTutorsByBookingStatus(@CurrentUser UserPrincipal userPrincipal,
                                                           @PathVariable(value = "bookingStatus") final BookingStatus bookingStatus,
                                                           @RequestParam(value = "page", defaultValue = "0") final int page,
                                                           @RequestParam(value = "limit", defaultValue = "50") final int limit) {
        List<User> users = userService.fetchUsersByBookingStatus(userPrincipal.getId(), bookingStatus);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update-password")
    public ResponseEntity<Response> updateUserPin(@CurrentUser UserPrincipal userPrincipal, @Validated @RequestBody UpdateUserPasswordRequest updateUserPasswordRequest)
            throws NotAcceptableException, ConflictException, NotFoundException {
        User user = userService.updatePassword(userPrincipal.getId(), updateUserPasswordRequest);
        return ResponseEntity.ok(new Response(true, true, "Password updated successfully", user));
    }

    @PutMapping("/update-profile")
    public User updateUserProfile(@CurrentUser UserPrincipal userPrincipal,@Validated @RequestBody UpdateUserProfileRequest userProfileRequest) {
        return userService.updateUser(userPrincipal.getId(), userProfileRequest);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteUser(@CurrentUser UserPrincipal userPrincipal)
            throws NotAcceptableException, ConflictException, NotFoundException {
        boolean deleted = userService.deleteUser(userPrincipal.getId());
        return ResponseEntity.ok(new Response(deleted, deleted, "User deleted successfully", null));
    }

    @GetMapping("/overview")
    public ResponseEntity<?> getUserOverview(@CurrentUser UserPrincipal userPrincipal, @RequestParam(value = "id", required = false) final String id,
                                             @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") final LocalDateTime from,
                                             @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") final LocalDateTime to
    ) {
        UserOverview userOverview = userService.fetchTotalUsers(id,from,to);
        Response response = new Response(true, true, "Gotten User successfully", userOverview);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search/hashtag")
    public List<User> searchTutorsByHashtag(@CurrentUser UserPrincipal userPrincipal,
            @RequestParam("hashtag") String hashtag) {
        return userService.searchTutorsByHashtag(userPrincipal.getId(), hashtag);
    }

    @GetMapping("/search/specialty")
    public List<User> searchCoursesByHashtag(@CurrentUser UserPrincipal userPrincipal,
            @RequestParam("hashtag") String hashtag) {
        return userService.searchUsersBySpecialty(userPrincipal.getId(), hashtag);
    }

    @GetMapping("/search")
    public List<User> searchCoursesForBooking(@CurrentUser UserPrincipal userPrincipal,
            @RequestParam("hashtag") String hashtag) {
        return userService.fetchTutorsForBooking(userPrincipal.getId(), hashtag);
    }

    @GetMapping("/filters")
    public ResponseEntity<?> getUsersFilter(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "page", defaultValue = "0") final int page,
            @RequestParam(value = "limit", defaultValue = "50") final int limit,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") final LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") final LocalDateTime to,
            @RequestParam(value = "id", required = false) final String id,
            @RequestParam(value = "phoneNumber", required = false) final String phoneNumber,
            @RequestParam(value = "email", required = false) final String email,
            @RequestParam(value = "firstName", required = false) final String firstName,
            @RequestParam(value = "lastName", required = false) final String lastName,
            @RequestParam(value = "username", required = false) final String username,
            @RequestParam(value = "gender", required = false) final Gender gender,
            @RequestParam(value = "role", required = false) final Role role,
            @RequestParam(value = "department", required = false) final String department,
            @RequestParam(value = "courseOfStudy", required = false) final String courseOfStudy,
            @RequestParam(value = "faculty", required = false) final String faculty,
            @RequestParam(value = "nameOfSchool", required = false) final String nameOfSchool,
            @RequestParam(value = "yearOfEntry", required = false) final String yearOfEntry,
            @RequestParam(value = "level", required = false) final String level,
            @RequestParam(value = "cgpa", required = false) final String cgpa,
            @RequestParam(value = "specialty", required = false) final String specialty,
            @RequestParam(value = "status", required = false) final String status,
            @RequestParam(value = "rating", required = false) final String rating,
            @RequestParam(value = "about", required = false) final String about,
            @RequestParam(value = "provider", required = false) final AuthProvider provider,
            @RequestParam(value = "providerId", required = false) final String providerId,
            @RequestParam(value = "sort", defaultValue = "timeCreated") final List<String> sort,
            @RequestParam(value = "direction", defaultValue = "DESC") final Sort.Direction direction
    ) {
        final HashMap<String, Object> filters = new HashMap<>();
        if (!StringUtil.isBlank(phoneNumber)) {
            filters.put("phoneNumber", phoneNumber);
        }

        if (!StringUtil.isBlank(email)) {
            filters.put("email", email);
        }

        if (!StringUtil.isBlank(id)) {
            filters.put("id", id);
        }

        if (!StringUtil.isBlank(firstName)) {
            filters.put("firstName", firstName);
        }

        if (!StringUtil.isBlank(lastName)) {
            filters.put("lastName", lastName);
        }

        if (!StringUtil.isBlank(username)) {
            filters.put("username", username);
        }

        if (gender!=null) {
            filters.put("gender", gender.name());
        }

        if (role!=null) {
            filters.put("role", role.name());
        }

        if (!StringUtil.isBlank(department)) {
            filters.put("department", department);
        }

        if (!StringUtil.isBlank(courseOfStudy)) {
            filters.put("courseOfStudy", courseOfStudy);
        }

        if (!StringUtil.isBlank(faculty)) {
            filters.put("faculty", faculty);
        }

        if (!StringUtil.isBlank(nameOfSchool)) {
            filters.put("nameOfSchool", nameOfSchool);
        }

        if (!StringUtil.isBlank(yearOfEntry)) {
            filters.put("yearOfEntry", yearOfEntry);
        }

        if (!StringUtil.isBlank(level)) {
            filters.put("level", level);
        }

        if (!StringUtil.isBlank(cgpa)) {
            filters.put("cgpa", cgpa);
        }

        if (!StringUtil.isBlank(specialty)) {
            filters.put("specialty", specialty);
        }

        if (!StringUtil.isBlank(status)) {
            filters.put("status", status);
        }

        if (!StringUtil.isBlank(rating)) {
            filters.put("rating", rating);
        }

        if (!StringUtil.isBlank(about)) {
            filters.put("about", about);
        }

        if (provider!=null) {
            filters.put("provider", provider.name());
        }


        if (!StringUtil.isBlank(providerId)) {
            filters.put("providerId", providerId);
        }


        final PageRequest pageRequest = PageRequest.of(
                page < 0 ? 0 : page,
                limit,
                Sort.by(Sort.Direction.DESC, "timeCreated"));


        final Page<User> userPage = userService.fetchUserByFilters(
                filters,
                from,
                to,
                pageRequest);

        Response response = new Response(true, true, "Fetched user by filter", userPage);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
