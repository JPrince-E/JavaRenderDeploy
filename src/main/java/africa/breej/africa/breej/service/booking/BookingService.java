package africa.breej.africa.breej.service.booking;

import africa.breej.africa.breej.model.booking.Booking;
import africa.breej.africa.breej.payload.booking.BookingRequest;

public interface BookingService {
    Booking createBooking(String userId, BookingRequest bookingRequest);
}
