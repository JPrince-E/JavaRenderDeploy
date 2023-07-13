package africa.breej.africa.breej.service.user;

import africa.breej.africa.breej.exception.ConflictException;
import africa.breej.africa.breej.exception.NotAcceptableException;
import africa.breej.africa.breej.exception.NotFoundException;
import africa.breej.africa.breej.exception.ResourceNotFoundException;
import africa.breej.africa.breej.model.auth.UserOverview;
import africa.breej.africa.breej.model.auth.UserReport;
import africa.breej.africa.breej.model.user.Role;
import africa.breej.africa.breej.model.user.User;
import africa.breej.africa.breej.payload.auth.SignUpRequest;
import africa.breej.africa.breej.payload.user.UpdateUserPasswordRequest;
import africa.breej.africa.breej.payload.user.UpdateUserProfileRequest;
import africa.breej.africa.breej.repository.UserRepository;
import africa.breej.africa.breej.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User createUser(SignUpRequest users) {

        User user = new User();

        user.setFirstName(users.getFirstName());
        user.setLastName(users.getLastName());
        user.setEmail(users.getEmail());

        user.setPassword(passwordEncoder.encode(users.getPassword()));

        return userRepository.save(user);
}

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> fetchUserById(String id) {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id)));
    }

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public List<User> fetchUsersByRole(String userId, Role role) {
        if (role != Role.ROLE_TUTOR){
            throw new NotFoundException("Tutor not found");
        }
        return userRepository.findAllByRoleAndDeleted(role, false);
    }

    @Override
    public User updatePassword(String id, UpdateUserPasswordRequest updateUserPasswordRequest) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!passwordEncoder.matches(updateUserPasswordRequest.getCurrentPassword(), user.getPassword()))
                throw new NotAcceptableException("current password is incorrect");

            if (passwordEncoder.matches(updateUserPasswordRequest.getNewPassword(), user.getPassword()))
                throw new ConflictException("new password cannot be the same as current pin");
            else {
                String newPasswordHash = passwordEncoder.encode(updateUserPasswordRequest.getNewPassword());
                user.setPassword(newPasswordHash);
                user.setTimeUpdated(LocalDateTime.now());
                userRepository.save(user);
                return user;
            }
        } else {
            throw new NotFoundException("User is not found");
        }
    }

    @Override
    public User updateUser(String userId, UpdateUserProfileRequest updateUserProfileRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (!StringUtil.isBlank(updateUserProfileRequest.getFirstName()))
                user.setFirstName(updateUserProfileRequest.getFirstName());

            if (!StringUtil.isBlank(updateUserProfileRequest.getLastName()))
                user.setLastName(updateUserProfileRequest.getLastName());

            if (!StringUtil.isBlank(updateUserProfileRequest.getUsername()))
                user.setUsername(updateUserProfileRequest.getUsername());

            if (!StringUtil.isBlank(updateUserProfileRequest.getPhoneNumber())) {
                if (fetchUserByPhoneNumber(updateUserProfileRequest.getPhoneNumber()).isPresent())
                    throw new NotAcceptableException("Phone number already exists");
                user.setPhoneNumber(updateUserProfileRequest.getPhoneNumber());
            }

            if (!StringUtil.isBlank(updateUserProfileRequest.getEmail())) {
                if (fetchUserByEmail(updateUserProfileRequest.getEmail()).isPresent())
                    throw new NotAcceptableException("Email already exists");
                user.setEmail(updateUserProfileRequest.getEmail());
            }

            if(updateUserProfileRequest.getGender() != null)
                user.setGender(updateUserProfileRequest.getGender());

            if (!StringUtil.isBlank(updateUserProfileRequest.getDepartment()))
                user.setDepartment(updateUserProfileRequest.getDepartment());

            if (!StringUtil.isBlank(updateUserProfileRequest.getCourseOfStudy()))
                user.setCourseOfStudy(updateUserProfileRequest.getCourseOfStudy());

            if (!StringUtil.isBlank(updateUserProfileRequest.getFaculty()))
                user.setFaculty(updateUserProfileRequest.getFaculty());

            if (!StringUtil.isBlank(updateUserProfileRequest.getLevel()))
                user.setLevel(updateUserProfileRequest.getLevel());

            if (!StringUtil.isBlank(updateUserProfileRequest.getYearOfEntry()))
                user.setYearOfEntry(updateUserProfileRequest.getYearOfEntry());

            if (!StringUtil.isBlank(updateUserProfileRequest.getNameOfSchool()))
                user.setNameOfSchool(updateUserProfileRequest.getNameOfSchool());

            if (!StringUtil.isBlank(updateUserProfileRequest.getCgpa()))
                user.setCgpa(updateUserProfileRequest.getCgpa());

            if (!StringUtil.isBlank(updateUserProfileRequest.getSpecialty()))
                user.setSpecialty(updateUserProfileRequest.getSpecialty());

            if (!StringUtil.isBlank(updateUserProfileRequest.getModeOfIdentification()))
                user.setModeOfIdentification(updateUserProfileRequest.getModeOfIdentification());

            if (!StringUtil.isBlank(updateUserProfileRequest.getStatus()))
                user.setStatus(updateUserProfileRequest.getStatus());

            if(updateUserProfileRequest.getBookingStatus() != null)
                user.setBookingStatus(updateUserProfileRequest.getBookingStatus());

            if(updateUserProfileRequest.getOnlineStatus() != null)
                user.setOnlineStatus(updateUserProfileRequest.getOnlineStatus());

            if (!StringUtil.isBlank(updateUserProfileRequest.getRating()))
                user.setRating(updateUserProfileRequest.getRating());

            if (!StringUtil.isBlank(updateUserProfileRequest.getAbout()))
                user.setAbout(updateUserProfileRequest.getAbout());

            if(updateUserProfileRequest.getRole() != null)
                user.setRole(updateUserProfileRequest.getRole());

            user.setTimeUpdated(LocalDateTime.now());

            return userRepository.save(user);
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public boolean deleteUser(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent())
            throw new NotFoundException("User not found");
        User user = userOptional.get();
        user.setDeleted(true);
        user.setTimeUpdated(LocalDateTime.now());

        //userRepository.save(user);

        userRepository.deleteById(userId);

        // TODO create a deleted user repository

        return true;
    }

    public Page<User> fetchUserByFilters(HashMap<String, Object> filters, LocalDateTime from, LocalDateTime to, PageRequest pageRequest) {
        return userRepository.findUserByFilters(filters, from, to, pageRequest);
    }

    public Optional<User> fetchUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> fetchUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public UserOverview fetchTotalUsers(String id, LocalDateTime from, LocalDateTime to) {

        UserOverview userOverview = new UserOverview();

        List<UserReport> userReportList = userRepository.userOverviewAggregation(from,to);

        for(UserReport userReport: userReportList){
            userOverview.setNumberOfUsers(userReport.getTotalCount());

        }
        return userOverview;

    }


}
