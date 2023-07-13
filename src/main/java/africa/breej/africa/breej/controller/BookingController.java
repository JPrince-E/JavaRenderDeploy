package africa.breej.africa.breej.controller;

import africa.breej.africa.breej.exception.ConflictException;
import africa.breej.africa.breej.exception.NotAcceptableException;
import africa.breej.africa.breej.exception.NotFoundException;
import africa.breej.africa.breej.model.booking.Booking;
import africa.breej.africa.breej.payload.Response;
import africa.breej.africa.breej.payload.booking.BookingRequest;
import africa.breej.africa.breej.security.CurrentUser;
import africa.breej.africa.breej.security.UserPrincipal;
import africa.breej.africa.breej.service.booking.BookingService;
import africa.breej.africa.breej.service.booking.BookingServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {
    BookingService bookingService;

    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping("")
    public ResponseEntity<Response> createBooking(@CurrentUser UserPrincipal userPrincipal, @Validated @RequestBody BookingRequest bookingRequest)
            throws NotAcceptableException, ConflictException, NotFoundException {
        Booking booking = bookingService.createBooking(userPrincipal.getId(), bookingRequest);
        Response response = new Response(true, true, "Booking Created successfully", booking);
        return ResponseEntity.ok(response);
    }

}
