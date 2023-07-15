package africa.breej.africa.breej.model.user;

import africa.breej.africa.breej.model.auth.AuthProvider;
import africa.breej.africa.breej.model.booking.BookingStatus;
import africa.breej.africa.breej.model.booking.OnlineStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "project_breej_db_user")
public class User {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String username;

    @Email
    private String email;

    private Boolean emailVerified = false;

    private String password;

    private String phoneNumber;

    private Role role;

    @NotNull
    private AuthProvider provider;

    @NotNull
    private Gender gender;

    private String providerId;

    private String courseOfStudy;

    private String department;

    private String faculty;

    private String nameOfSchool;

    private String yearOfEntry;

    private String level;

    private String cgpa;

    private String specialty;

    private String modeOfIdentification;

    private String identificationId;

    private String profilePictureId;

    private String profilePictureUrl;

    private String status;

    private OnlineStatus onlineStatus;

    private BookingStatus bookingStatus;

    private String rating;

    private String about;

    private String referralCode;

    private boolean deleted=false;

    LocalDateTime timeCreated;

    LocalDateTime timeUpdated;

    LocalDateTime lastLogin;

}
