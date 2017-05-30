package nl.first8.hu.ticketsale.venue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class VenueRepository {

    private final EntityManager entityManager;

    @Autowired
    public VenueRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<Concert> findConcertById(Long concertId) {
        try{
            return Optional.ofNullable(entityManager.find(Concert.class, concertId));
        }
        catch (NoResultException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Concert> searchOnCriteria(String artistName, Genre genre, Date minDateConcert, String location) {
        String jpql = "SELECT concert FROM Concert concert WHERE " +
                "concert.artist.name = :artistName AND " +
                "concert.location.name = :location AND " +
                "concert.artist.genre = :genre AND " +
                "concert.date >= :minDateConcert";
        TypedQuery<Concert> query = entityManager.createQuery(jpql, Concert.class);

        query.setParameter("artistName", artistName);
        query.setParameter("genre", genre);
        query.setParameter("minDateConcert", minDateConcert);
        query.setParameter("location", location);

        return query.getResultList();
    }
}
