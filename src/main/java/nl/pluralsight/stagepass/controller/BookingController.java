package nl.pluralsight.stagepass.controller;

import jakarta.validation.Valid;
import nl.pluralsight.stagepass.model.Booking;
import nl.pluralsight.stagepass.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/concert/{concertId}")
    public ResponseEntity<List<Booking>> getBookingsByConcert(@PathVariable Long concertId) {
        return ResponseEntity.ok(bookingService.getBookingsByConcert(concertId));
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking created = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        // changed this piece of code to explicitly return a 201 status code instead of the default 200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        if (bookingService.cancelBooking(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
        // changed response entity to 'no content' instead of 'not found' to return 204 code
    }
}
