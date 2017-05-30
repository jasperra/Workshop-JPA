package nl.first8.hu.ticketsale.sales;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.first8.hu.ticketsale.registration.Account;

import javax.persistence.*;

/**
 * Created by Jasper on 25-May-17.
 */


@Entity
@Data
@NoArgsConstructor
public class AuditTrail {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToOne
    @JoinColumn(name = "sale_id", referencedColumnName = "id")
    private Sale sale;

    public AuditTrail (Sale sale){
        this.account = sale.getTicket().getAccount();
        this.sale = sale;
    }
}
