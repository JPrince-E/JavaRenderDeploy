package africa.breej.africa.breej.repository;

import africa.breej.africa.breej.model.booking.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.HashMap;

public interface CustomBookingRepository {
    Page<Booking> findBookingByFilters(HashMap<String, Object> filters, LocalDateTime from, LocalDateTime to, Pageable pageable);

}
