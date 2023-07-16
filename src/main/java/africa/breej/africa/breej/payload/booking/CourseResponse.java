package africa.breej.africa.breej.payload.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private String id;

    private String tutorId;

    private String tutorFirstName;

    private String tutorLastName;

    private String tutorUsername;

    private String courseTitle;

    private String courseCode;

    private String courseDes;

    private String time;

    private String location;

    private String price;

    private String mode;

    private String platform;

    private String date;

    private String noOfStudents;

    private String maxStudentsNo;

    private String status;

    private String link;

    private String difference;

    private String type;

    private String startDate;

    private String endDate;
}
