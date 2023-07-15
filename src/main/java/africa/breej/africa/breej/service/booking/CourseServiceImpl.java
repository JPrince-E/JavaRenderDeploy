package africa.breej.africa.breej.service.booking;

import africa.breej.africa.breej.exception.NotAcceptableException;
import africa.breej.africa.breej.exception.ResourceNotFoundException;
import africa.breej.africa.breej.model.booking.Course;
import africa.breej.africa.breej.model.user.Role;
import africa.breej.africa.breej.model.user.User;
import africa.breej.africa.breej.payload.booking.CourseRequest;
import africa.breej.africa.breej.repository.CourseRepository;
import africa.breej.africa.breej.service.user.UserService;
import africa.breej.africa.breej.service.user.UserServiceImpl;
import africa.breej.africa.breej.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

    UserService userService;
    CourseRepository courseRepository;

    private final ApplicationEventPublisher publisher;

    public CourseServiceImpl(UserServiceImpl userService, CourseRepository courseRepository, ApplicationEventPublisher publisher) {
        this.userService = userService;
        this.courseRepository = courseRepository;
        this.publisher = publisher;
    }

    @Override
    public Course createCourse(String userId, CourseRequest courseRequest) {
        Optional<User> userOptional = userService.fetchUserById(userId);
        Course course = getCourseFromCourseRequest(courseRequest);
        if (!userOptional.isPresent())
            throw new NotAcceptableException("Not a Tutor!");
        else {
            if (userOptional.get().getRole() != Role.ROLE_TUTOR) {
                throw new ResourceNotFoundException("Course", "userId", userId);
            } else {
                course.setTutorId(userId);
                course.setTimeCreated(LocalDateTime.now());
            }
            return courseRepository.save(course);
        }
    }

    private Course getCourseFromCourseRequest(CourseRequest courseRequest) {
        Course course = new Course();
        try {
            BeanUtils.copyProperties(course, courseRequest);
        } catch (Exception exception) {
            LOGGER.error("Error copying course request properties");
        }
        return course;
    }


    @Override
    public boolean deleteCourse(String courseId) {
        Optional<Course> existingBookingOptional = courseRepository.findByIdAndDeleted(courseId, false);
        if (!existingBookingOptional.isPresent())
            throw new ResourceNotFoundException("Course not found!");
        else {
            Course course = existingBookingOptional.get();
            course.setDeleted(true);
            course.setTimeUpdated(LocalDateTime.now());

            courseRepository.save(course);
            return true;
        }
    }

    @Override
    public Course updateCourse(String id, String courseId, CourseRequest courseRequest) {
        Optional<Course> existingCourseOptional = courseRepository.findByIdAndDeleted(courseId, false);
        if (!existingCourseOptional.isPresent())
            throw new ResourceNotFoundException("Course not found!");
        else {
            Course course = existingCourseOptional.get();

            if (!StringUtil.isBlank(courseRequest.getTutorFirstName()))
                course.setTutorFirstName(courseRequest.getTutorFirstName());

            if (!StringUtil.isBlank(courseRequest.getTutorLastName()))
                course.setTutorLastName(courseRequest.getTutorLastName());

            if (!StringUtil.isBlank(courseRequest.getTutorUsername()))
                course.setTutorUsername(courseRequest.getTutorUsername());

            if (!StringUtil.isBlank(courseRequest.getCourseCode()))
                course.setCourseCode(courseRequest.getCourseCode());

            if (!StringUtil.isBlank(courseRequest.getCourseTitle()))
                course.setCourseTitle(courseRequest.getCourseTitle());

            if (!StringUtil.isBlank(courseRequest.getCourseDes()))
                course.setCourseDes(courseRequest.getCourseDes());

            if (!StringUtil.isBlank(courseRequest.getTime()))
                course.setTime(courseRequest.getTime());

            if (!StringUtil.isBlank(courseRequest.getDate()))
                course.setDate(courseRequest.getDate());

            if (!StringUtil.isBlank(courseRequest.getMode()))
                course.setMode(courseRequest.getMode());

            if (!StringUtil.isBlank(courseRequest.getPrice()))
                course.setPrice(courseRequest.getPrice());

            if (!StringUtil.isBlank(courseRequest.getLink()))
                course.setLink(courseRequest.getLink());

            if (!StringUtil.isBlank(courseRequest.getLocation()))
                course.setLocation(courseRequest.getLocation());

            if (!StringUtil.isBlank(courseRequest.getNoOfStudents()))
                course.setNoOfStudents(courseRequest.getNoOfStudents());

            if (!StringUtil.isBlank(courseRequest.getType()))
                course.setType(courseRequest.getType());

            if (!StringUtil.isBlank(courseRequest.getStartDate()))
                course.setStartDate(courseRequest.getStartDate());

            if (!StringUtil.isBlank(courseRequest.getEndDate()))
                course.setEndDate(courseRequest.getEndDate());

            if (!StringUtil.isBlank(courseRequest.getStatus()))
                course.setStatus(courseRequest.getStatus());

            course.setTimeUpdated(LocalDateTime.now());
            return courseRepository.save(course);
        }
    }
}
