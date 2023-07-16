package africa.breej.africa.breej.model.booking;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "project_breej_db_course")
public class Course {
    @Id
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

    private boolean deleted=false;

    LocalDateTime timeCreated;

    LocalDateTime timeUpdated;

}
