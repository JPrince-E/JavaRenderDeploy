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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {
    BookingService bookingService;

    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping("")
    public ResponseEntity<Response> createBooking(@CurrentUser UserPrincipal userPrincipal,
                                                  @Validated @RequestBody BookingRequest bookingRequest)
            throws NotAcceptableException, ConflictException, NotFoundException {
        Booking booking = bookingService.createBooking(userPrincipal.getId(), bookingRequest);
        Response response = new Response(true, true, "Booking Created successfully", booking);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@CurrentUser UserPrincipal userPrincipal,
                                           @PathVariable(value = "bookingId") final String bookingId) {
        Booking booking = bookingService.cancelBooking(userPrincipal.getId(), bookingId);
        Response response = new Response(true, true, "Booking cancelled successfully", booking);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{bookingId}")
    public ResponseEntity<?> deleteBooking(@CurrentUser UserPrincipal userPrincipal,
                                           @PathVariable(value = "bookingId") final String bookingId) {
        boolean isDeleted = bookingService.deleteBooking(bookingId);
        Response response = new Response(true, true, "Booking Deleted successfully", isDeleted);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/conclude/{bookingId}")
    public ResponseEntity<?> concludeBooking(@CurrentUser UserPrincipal userPrincipal,
                                           @Validated @RequestBody BookingRequest bookingRequest,
                                           @PathVariable(value = "bookingId") final String bookingId) {
        Booking booking = bookingService.concludeBooking(userPrincipal.getId(), bookingId, bookingRequest);
        Response response = new Response(true, true, "Booking Updated successfully", booking);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
