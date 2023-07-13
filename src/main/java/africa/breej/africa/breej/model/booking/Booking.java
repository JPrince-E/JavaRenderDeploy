package africa.breej.africa.breej.model.booking;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;

@Getter
@Setter
@Document(collection = "project_breej_db_booking")
public class Booking {
    @Id
    private String id;

    private String courseId;

    private String tutorId;

    private String tutorName;

    private String tutorPhone;

    @Email
    private String tutorEmail;

    private String tutorPictureId;

    private String tutorPictureUrl;

    private String studentId;

    private String studentFirstName;

    private String studentLastName;

    @Email
    private String studentEmail;

    private String studentPhone;

    private String studentGender;

    private String studentPictureId;

    private String studentPictureIUrl;

    private String courseCode;

    private String courseTitle;

    private String courseDes;

    private String time;

    private String date;

    private String mode;

    private String price;

    private String link;

    private String location;

    private String noOfStudents;

    private String type;

    private String startDate;

    private String endDate;

    private String tutorRating;

    private String platform;

    private String status;

}
