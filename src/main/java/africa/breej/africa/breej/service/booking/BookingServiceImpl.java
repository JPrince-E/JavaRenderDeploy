package africa.breej.africa.breej.service.booking;

import africa.breej.africa.breej.exception.NotAcceptableException;
import africa.breej.africa.breej.exception.ResourceNotFoundException;
import africa.breej.africa.breej.model.booking.Booking;
import africa.breej.africa.breej.model.booking.BookingStatus;
import africa.breej.africa.breej.model.user.User;
import africa.breej.africa.breej.payload.booking.BookingRequest;
import africa.breej.africa.breej.repository.BookingRepository;
import africa.breej.africa.breej.service.user.UserService;
import africa.breej.africa.breej.service.user.UserServiceImpl;
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
    BookingRepository bookingRepository;

    private final ApplicationEventPublisher publisher;

    public BookingServiceImpl(UserServiceImpl userService, BookingRepository bookingRepository, ApplicationEventPublisher publisher) {
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.publisher = publisher;
    }

    @Override
    public Booking createBooking(String userId, BookingRequest bookingRequest) {
        Optional<User> userOptional = userService.fetchUserById(userId);
        Booking booking = getBookingFromBookingRequest(bookingRequest);
        if (userOptional.isPresent()) {
            boolean existingBookingStatus = bookingRequest.getBookingStatus() == BookingStatus.UNAVAILABLE;
            if (existingBookingStatus)
                throw new NotAcceptableException("Tutor Booked Already!");
            else {
                booking.setUserId(userId);
                booking.setTimeCreated(LocalDateTime.now());
            }
        } else {
            throw new ResourceNotFoundException("Booking", "userId", userId);
        }
        return bookingRepository.save(booking);
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
}
