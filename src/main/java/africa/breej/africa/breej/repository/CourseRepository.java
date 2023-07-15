package africa.breej.africa.breej.repository;

import africa.breej.africa.breej.model.booking.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourseRepository  extends MongoRepository<Course, String>, CustomCourseRepository{
    Optional<Course> findByIdAndDeleted(String id, boolean deleted);

}
