package nl.first8.hu.ticketsale.venue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Jasper on 28-May-17.
 */

@RestController
@RequestMapping("/venue")
@Transactional
public class VenueResource {

    @Autowired
    private VenueService venueService;

    @PostMapping(path = "/search")
    public ResponseEntity<List<Concert>> postSearch(
            @RequestParam("artistName") final String artistName,
            @RequestParam("genre") final String genre,
            @RequestParam("minDateConcert") final String minDateConcert,
            @RequestParam("location") final String location){
        try{
            return ResponseEntity.ok(venueService.searchConcerts(artistName, genre, minDateConcert, location));
        } catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
