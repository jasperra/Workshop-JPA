package nl.first8.hu.ticketsale.venue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jasper on 28-May-17.
 */
@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;

    public List<Concert> searchConcerts(String artistName, String genre, String minDateConcert, String location) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;

        try {
            date = dateFormat.parse(minDateConcert);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return venueRepository.searchOnCriteria(artistName, Genre.valueOf(genre), date, location);
    }
}
