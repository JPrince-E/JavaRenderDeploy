package africa.breej.africa.breej.payload.user;

import africa.breej.africa.breej.model.user.Gender;
import africa.breej.africa.breej.model.user.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UpdateUserProfileRequest {

    private String firstName;

    private String lastName;

    private String username;

    @Email
    private String email;

    private String phoneNumber;

    private Role role;

    private Gender gender;

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

}