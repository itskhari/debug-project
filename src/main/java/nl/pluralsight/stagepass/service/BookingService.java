package nl.pluralsight.stagepass.service;

import nl.pluralsight.stagepass.exception.InsufficientSeatsException;
import nl.pluralsight.stagepass.model.Booking;
import nl.pluralsight.stagepass.model.Concert;
import nl.pluralsight.stagepass.repository.BookingRepository;
import nl.pluralsight.stagepass.repository.ConcertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ConcertRepository concertRepository;

    public BookingService(BookingRepository bookingRepository, ConcertRepository concertRepository) {
        this.bookingRepository = bookingRepository;
        this.concertRepository = concertRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByConcert(Long concertId) {
        return bookingRepository.findByConcertId(concertId);
        // changed findAll to findByConcertId
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        Concert concert = concertRepository.findById(booking.getConcert().getId())
                .orElseThrow(() -> new RuntimeException("Concert not found"));

        // feature 1 - seat checking
        if (concert.getAvailableSeats() < booking.getNumberOfTickets()) {
            throw new InsufficientSeatsException("Not enough seats available");
        }

        // Compute total price
        BigDecimal total = concert.getTicketPrice().multiply(BigDecimal.valueOf(booking.getNumberOfTickets()));
        // big decimal was originally setting this value to zero, so I looked up how to incorporate ticketprice * numoftickets
        // but in BigDecimal format

        // Set booking date and concert reference
        booking.setBookingDate(LocalDate.now());
        booking.setConcert(concert);

        concert.setAvailableSeats(concert.getAvailableSeats() - booking.getNumberOfTickets());
        concertRepository.save(concert);
        // just made it to where available seats are subtracted by tickets bought

        return bookingRepository.save(booking);
    }

    public boolean cancelBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
