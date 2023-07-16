package africa.breej.africa.breej.controller;

import africa.breej.africa.breej.exception.ConflictException;
import africa.breej.africa.breej.exception.NotAcceptableException;
import africa.breej.africa.breej.exception.NotFoundException;
import africa.breej.africa.breej.model.booking.Course;
import africa.breej.africa.breej.payload.Response;
import africa.breej.africa.breej.payload.booking.CourseRequest;
import africa.breej.africa.breej.security.CurrentUser;
import africa.breej.africa.breej.security.UserPrincipal;
import africa.breej.africa.breej.service.booking.CourseService;
import africa.breej.africa.breej.service.booking.CourseServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/course")
public class CourseController {
    CourseService courseService;

    public CourseController(CourseServiceImpl courseService) {
        this.courseService = courseService;
    }

    @PostMapping("")
    public ResponseEntity<Response> createCourse(@CurrentUser UserPrincipal userPrincipal,
                                                 @Validated @RequestBody CourseRequest courseRequest)
            throws NotAcceptableException, ConflictException, NotFoundException {
        Course course = courseService.createCourse(userPrincipal.getId(), courseRequest);
        Response response = new Response(true, true, "Booking Created successfully", course);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<?> deleteCourse(@CurrentUser UserPrincipal userPrincipal,
                                          @PathVariable(value = "courseId") final String courseId) {
        boolean isDeleted = courseService.deleteCourse(courseId);
        Response response = new Response(true, true, "Booking Deleted successfully", isDeleted);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<?> updateCourse(@CurrentUser UserPrincipal userPrincipal,
                                             @Validated @RequestBody CourseRequest courseRequest,
                                             @PathVariable(value = "courseId") final String courseId) {
        Course course = courseService.updateCourse(userPrincipal.getId(), courseId, courseRequest);
        Response response = new Response(true, true, "Booking Updated successfully", course);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
