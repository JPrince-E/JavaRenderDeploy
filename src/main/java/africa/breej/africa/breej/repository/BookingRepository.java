package africa.breej.africa.breej.repository;

import africa.breej.africa.breej.model.booking.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String>, CustomBookingRepository {
    Optional<Booking> findByIdAndDeleted(String id, boolean deleted);

    List<Booking> findAllByUserIdAndDeleted(String userId, boolean deleted);

//    Optional<Booking> findByUserIdAndNameAndDeleted(String userId,String name, boolean deleted);

}
