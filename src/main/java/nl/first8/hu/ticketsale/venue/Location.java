package nl.first8.hu.ticketsale.venue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class Location implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "location_name")
    private String name;

    @OneToMany(mappedBy = "location")
    private List<Concert> concerts;

    public Location(String name, List<Concert> concerts) {
        this.name = name;
        this.concerts = concerts;
    }

    public String toString(){
        return id + name;
    }
}
