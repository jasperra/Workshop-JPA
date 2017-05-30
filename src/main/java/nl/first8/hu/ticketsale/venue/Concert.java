package nl.first8.hu.ticketsale.venue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Concert implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToOne
    @JoinColumn(name="artist_id", referencedColumnName = "id")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    public Concert (Long id, Artist artist, Location location){
        this.id = id;
        this.artist = artist;
        this.location = location;
    }

    public Concert(Date date, Artist artist, Location location) {
        this.date = date;
        this.artist = artist;
        this.location = location;
    }

    public boolean equals(Concert concert){
        return this.id == concert.id || (this.date == concert.date && this.artist == concert.artist && this.location
                == concert.location);
    }
}
