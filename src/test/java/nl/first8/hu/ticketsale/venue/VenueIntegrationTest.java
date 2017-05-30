package nl.first8.hu.ticketsale.venue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.first8.hu.ticketsale.registration.Account;
import nl.first8.hu.ticketsale.sales.Ticket;
import nl.first8.hu.ticketsale.sales.TicketDto;
import nl.first8.hu.ticketsale.util.TestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Jasper on 28-May-17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false)
public class VenueIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRepository testRepository;

    @Before
    public void cleanDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "audit_trail", "sale", "ticket", "account");
    }

    @Test
    @Transactional()
    public void testGetSearchResult() throws Exception{
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Artist artistGorillaz = entityManager.merge(new Artist("Gorillaz", Genre.electronica));
        Artist artistMetallica = entityManager.merge(new Artist("Metallica", Genre.metal));

        Location locationArnhem = entityManager.merge(new Location("Arnhem", new ArrayList<>()));
        Location locationAmsterdam = entityManager.merge(new Location("Amsterdam", new ArrayList<>()));

        entityManager.persist(new Concert(new Date(), artistGorillaz, locationArnhem));
        entityManager.persist(new Concert(new Date(), artistMetallica, locationAmsterdam));

        MvcResult result = mvc.perform(
                post("/venue/search")
                        .param("artistName", "Metallica")
                        .param("genre","metal")
                        .param("minDateConcert", dateFormat.format(new Date(0, 1, 1)))
                        .param("location", "Amsterdam")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        List<Concert> concerts = readConcertsReponse(result);
        assertEquals(1, concerts.size());
        assertEquals(artistMetallica, concerts.get(0));
    }

    @Test
    public void testGetTickets() throws Exception {

        Account account = testRepository.createDefaultAccount("f.dejong@first8.nl");
        Ticket ticketGorillaz = testRepository.createDefaultTicket(account, "Gorillaz", "Utrecht");
        Ticket ticketThieveryCo = testRepository.createDefaultTicket(account, "Thievery Cooperation", "Apeldoorn");


        MvcResult result = mvc.perform(
                get("/sales/ticket").param("account_id", account.getId().toString())
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        List<TicketDto> actualTickets = readTicketsResponse(result);
        assertEquals(2, actualTickets.size());
        assertEquals(ticketGorillaz.getConcert().getArtist().getName(), actualTickets.get(0).getArtist());
        assertEquals(ticketGorillaz.getConcert().getLocation().getName(), actualTickets.get(0).getLocation());
        assertEquals(ticketThieveryCo.getConcert().getArtist().getName(), actualTickets.get(1).getArtist());
        assertEquals(ticketGorillaz.getConcert().getLocation().getName(), actualTickets.get(0).getLocation());
    }

    private List<TicketDto> readTicketsResponse(MvcResult result) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<TicketDto>>() {
        });
    }

    private List<Concert> readConcertsReponse(MvcResult result) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Concert>>() {
        });
    }
}
