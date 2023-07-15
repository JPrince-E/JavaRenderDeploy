package africa.breej.africa.breej.service.booking;

import africa.breej.africa.breej.model.booking.Course;
import africa.breej.africa.breej.payload.booking.CourseRequest;

public interface CourseService {
    Course createCourse(String userId, CourseRequest courseRequest);

    boolean deleteCourse(String courseId);

    Course updateCourse(String id, String courseId, CourseRequest courseRequest);

}
