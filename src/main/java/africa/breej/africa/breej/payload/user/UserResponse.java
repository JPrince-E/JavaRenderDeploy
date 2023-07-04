package africa.breej.africa.breej.payload.user;

import africa.breej.africa.breej.model.user.Gender;
import africa.breej.africa.breej.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private  String id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private Boolean emailVerified = false;

    private String password;

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
