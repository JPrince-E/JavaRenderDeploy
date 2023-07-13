package africa.breej.africa.breej.repository;

import africa.breej.africa.breej.model.booking.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Repository
public class CustomBookingRepositoryImpl implements CustomBookingRepository{
    private static final Logger log = LoggerFactory.getLogger(CustomBookingRepositoryImpl.class);

    private MongoTemplate mongoTemplate;

    public static final String PROJECT_HUZZ_DB_BUSINESS = "project_huzz_db_booking";

//    @Autowired
//    public CustomBookingRepositoryImpl(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }

    @Override
    public Page<Booking> findBookingByFilters(HashMap<String, Object> filters, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        filters.put("deleted", false);
        Query query = new Query();
        for (String filter : filters.keySet()) {
            if (filters.get(filter) instanceof Collection)
                query.addCriteria(Criteria.where(filter).in((Collection<?>) filters.get(filter)));
            else
                query.addCriteria(Criteria.where(filter).is(filters.get(filter)));
        }

        //apply date filters
        if (from != null && to != null) {
            query.addCriteria(Criteria.where("timeCreated").lte(to).gte(from));
        } else if (from != null) {
            query.addCriteria(Criteria.where("timeCreated").gte(from));
        } else if (to != null) {
            query.addCriteria(Criteria.where("timeCreated").lte(to));
        }
        query.with(pageable);

        List<Booking> bookingList = mongoTemplate.find(query, Booking.class);

        return PageableExecutionUtils.getPage(bookingList, pageable,
                () -> mongoTemplate.count(query, Booking.class));
    }
}
