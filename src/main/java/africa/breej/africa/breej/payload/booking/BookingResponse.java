package africa.breej.africa.breej.payload.booking;

import africa.breej.africa.breej.model.booking.BookingStatus;
import africa.breej.africa.breej.model.booking.OnlineStatus;
import africa.breej.africa.breej.model.user.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class BookingResponse {

    private String id;

    private String courseId;

    private String tutorId;

    private String tutorFirstName;

    private String tutorLastName;

    private String tutorUsername;

    private String tutorPhone;

    @Email
    private String tutorEmail;

    private String tutorPictureId;

    private String tutorPictureUrl;

    private String studentId;

    private String studentFirstName;

    private String studentLastName;

    private String studentUsername;

    private Gender tutorGender;

    @Email
    private String studentEmail;

    private String studentPhone;

    private Gender studentGender;

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

    private boolean pending;

    private boolean approved;

    private OnlineStatus onlineStatus;

    private BookingStatus bookingStatus;
}
