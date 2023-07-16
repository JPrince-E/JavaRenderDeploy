package africa.breej.africa.breej.service.booking;

import africa.breej.africa.breej.exception.NotAcceptableException;
import africa.breej.africa.breej.exception.ResourceNotFoundException;
import africa.breej.africa.breej.model.booking.Booking;
import africa.breej.africa.breej.model.booking.BookingStatus;
import africa.breej.africa.breej.model.booking.Course;
import africa.breej.africa.breej.model.user.Role;
import africa.breej.africa.breej.model.user.User;
import africa.breej.africa.breej.payload.booking.BookingRequest;
import africa.breej.africa.breej.repository.BookingRepository;
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
public class BookingServiceImpl implements BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingServiceImpl.class);

    UserService userService;
    CourseService courseService;
    BookingRepository bookingRepository;

    private final ApplicationEventPublisher publisher;

    public BookingServiceImpl(UserServiceImpl userService, CourseService courseService, BookingRepository bookingRepository, ApplicationEventPublisher publisher) {
        this.userService = userService;
        this.courseService = courseService;
        this.bookingRepository = bookingRepository;
        this.publisher = publisher;
    }

    @Override
    public Booking createBooking(String userId, BookingRequest bookingRequest) {
        Optional<User> userOptional = userService.fetchUserById(userId);
        Booking booking = getBookingFromBookingRequest(bookingRequest);
        if (userOptional.isPresent()) {
            Optional<User> tutor = userService.fetchUserById(bookingRequest.getTutorId());
            Optional<Course> course = courseService.fetchCourseById(bookingRequest.getCourseId());
            if (!tutor.isPresent()) {
                throw new NotAcceptableException("Tutor does not exist!");
            }
            if (tutor.get().getRole() == Role.ROLE_TUTOR) {
                boolean existingBookingStatus = tutor.get().getBookingStatus() == BookingStatus.NOT_AVAILABLE;
                if (existingBookingStatus) {
                    throw new NotAcceptableException("Tutor not available for booking!");
                } else {

                    booking.setStudentId(userId);

                    booking.setTutorFirstName(tutor.get().getFirstName());
                    booking.setTutorLastName(tutor.get().getLastName());
                    booking.setTutorUsername(course.get().getTutorUsername());
                    booking.setTutorGender(tutor.get().getGender());
                    booking.setTutorPhone(tutor.get().getPhoneNumber());
                    booking.setTutorEmail(tutor.get().getEmail());
                    booking.setTutorPictureId(tutor.get().getProfilePictureId());
                    booking.setTutorPictureUrl(tutor.get().getProfilePictureUrl());

                    booking.setStudentFirstName(userOptional.get().getFirstName());
                    booking.setStudentLastName(userOptional.get().getLastName());

                    if (StringUtil.isBlank(bookingRequest.getStudentUsername()))
                        booking.setStudentUsername(userOptional.get().getUsername());

                    booking.setStudentEmail(userOptional.get().getEmail());
                    booking.setStudentPhone(userOptional.get().getPhoneNumber());
                    booking.setStudentGender(tutor.get().getGender());
                    booking.setStudentPictureId(userOptional.get().getProfilePictureId());
                    booking.setStudentPictureIUrl(userOptional.get().getProfilePictureUrl());

                    booking.setCourseCode(course.get().getCourseCode());
                    booking.setCourseTitle(course.get().getCourseTitle());
                    booking.setCourseDes(course.get().getCourseDes());
                    booking.setTime(bookingRequest.getTime());
                    booking.setDate(bookingRequest.getDate());
                    booking.setPrice(course.get().getPrice());
                    booking.setLocation(course.get().getLocation());

                    booking.setNoOfStudents(bookingRequest.getNoOfStudents());

                    booking.setType(course.get().getType());
                    booking.setStartDate(bookingRequest.getStartDate());
                    booking.setEndDate(bookingRequest.getEndDate());
                    booking.setStatus(course.get().getStatus());

                    booking.setPending(true);
                    booking.setBookingStatus(BookingStatus.BOOKED);
                    tutor.get().setBookingStatus(BookingStatus.BOOKED);

                    booking.setTimeCreated(LocalDateTime.now());
                }
            } else {
                throw new ResourceNotFoundException("Booking", "userId", userId);
            }
        }

        return bookingRepository.save(booking);

    }

    public Booking cancelBooking(String userId, String bookingId) {

        Optional<Booking> existingBookingOptional = bookingRepository.findByIdAndDeleted(bookingId, false);
        if (!existingBookingOptional.isPresent())
            throw new ResourceNotFoundException("Booking not found!");
        else {
            Booking booking = existingBookingOptional.get();
            if (booking.isPending() == true)
                booking.setPending(false);

            if (booking.isApproved() == true)
                booking.setApproved(false);

            booking.setTimeUpdated(LocalDateTime.now());
            return bookingRepository.save(booking);
        }
    }

    private Booking getBookingFromBookingRequest(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        try {
            BeanUtils.copyProperties(booking, bookingRequest);
        } catch (Exception exception) {
            LOGGER.error("Error copying booking request properties");
        }
        return booking;
    }

    public boolean deleteBooking(String bookingId) {
        Optional<Booking> existingBookingOptional = bookingRepository.findByIdAndDeleted(bookingId, false);
        if (!existingBookingOptional.isPresent())
            throw new ResourceNotFoundException("Booking not found!");
        else {
            Booking booking = existingBookingOptional.get();
            booking.setDeleted(true);
            booking.setTimeUpdated(LocalDateTime.now());

            bookingRepository.save(booking);
            return true;
        }
    }

    public Booking concludeBooking(String userId, String bookingId, BookingRequest bookingRequest) {

        Optional<Booking> existingBookingOptional = bookingRepository.findByIdAndDeleted(bookingId, false);
        if (!existingBookingOptional.isPresent())
            throw new ResourceNotFoundException("Booking not found!");
        else {
            Booking booking = existingBookingOptional.get();

            if (!StringUtil.isBlank(bookingRequest.getTutorFirstName()))
                booking.setTutorFirstName(bookingRequest.getTutorFirstName());

            if (!StringUtil.isBlank(bookingRequest.getTutorLastName()))
                booking.setTutorLastName(bookingRequest.getTutorLastName());

            if (bookingRequest.getTutorGender() != null)
                booking.setTutorGender(booking.getTutorGender());

            if (!StringUtil.isBlank(bookingRequest.getTutorPhone()))
                booking.setTutorPhone(bookingRequest.getTutorPhone());

            if (!StringUtil.isBlank(bookingRequest.getTutorEmail()))
                booking.setTutorEmail(bookingRequest.getTutorEmail());

            if (!StringUtil.isBlank(bookingRequest.getStudentFirstName()))
                booking.setStudentFirstName(booking.getStudentFirstName());

            if (!StringUtil.isBlank(bookingRequest.getStudentLastName()))
                booking.setStudentLastName(booking.getStudentLastName());

            if (!StringUtil.isBlank(bookingRequest.getStudentUsername()))
                booking.setStudentUsername(booking.getStudentUsername());

            if (!StringUtil.isBlank(bookingRequest.getStudentEmail()))
                booking.setStudentEmail(bookingRequest.getStudentEmail());

            if (!StringUtil.isBlank(bookingRequest.getStudentPhone()))
                booking.setStudentPhone(bookingRequest.getStudentPhone());

            if (bookingRequest.getStudentGender() != null)
                booking.setStudentGender(booking.getStudentGender());

            if (!StringUtil.isBlank(bookingRequest.getOtherStudents()))
                booking.setOtherStudents(bookingRequest.getOtherStudents());

            if (!StringUtil.isBlank(bookingRequest.getCourseCode()))
                booking.setCourseCode(bookingRequest.getCourseCode());

            if (!StringUtil.isBlank(bookingRequest.getCourseTitle()))
                booking.setCourseTitle(bookingRequest.getCourseTitle());

            if (!StringUtil.isBlank(bookingRequest.getCourseDes()))
                booking.setCourseDes(bookingRequest.getCourseDes());

            if (!StringUtil.isBlank(bookingRequest.getTime()))
                booking.setTime(bookingRequest.getTime());

            if (!StringUtil.isBlank(bookingRequest.getDate()))
                booking.setDate(bookingRequest.getDate());

            if (!StringUtil.isBlank(bookingRequest.getMode()))
                booking.setMode(bookingRequest.getMode());

            if (!StringUtil.isBlank(bookingRequest.getPrice()))
                booking.setPrice(bookingRequest.getPrice());

            if (!StringUtil.isBlank(bookingRequest.getLink()))
                booking.setLink(bookingRequest.getLink());

            if (!StringUtil.isBlank(bookingRequest.getLocation()))
                booking.setLocation(bookingRequest.getLocation());

            if (!StringUtil.isBlank(bookingRequest.getNoOfStudents()))
                booking.setNoOfStudents(bookingRequest.getNoOfStudents());

            if (!StringUtil.isBlank(bookingRequest.getType()))
                booking.setType(bookingRequest.getType());

            if (!StringUtil.isBlank(bookingRequest.getStartDate()))
                booking.setStartDate(bookingRequest.getStartDate());

            if (!StringUtil.isBlank(bookingRequest.getEndDate()))
                booking.setEndDate(bookingRequest.getEndDate());

            if (!StringUtil.isBlank(bookingRequest.getTutorRating()))
                booking.setTutorRating(bookingRequest.getTutorRating());

            if (!StringUtil.isBlank(bookingRequest.getPlatform()))
                booking.setPlatform(bookingRequest.getPlatform());

            if (booking.isPending() == false)
                booking.setPending(true);

            booking.setTimeUpdated(LocalDateTime.now());
            return bookingRepository.save(booking);
        }
    }

}
