package nl.pluralsight.stagepass.repository;

import nl.pluralsight.stagepass.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {
    List<Concert> findByArtistId(Long artistID);
    // changed this, this is basically just select * from artistId

    List<Concert> findByDateAfterOrderByDateAsc(LocalDate date);
    // select * from concerts where date > ? order by date acs
}
