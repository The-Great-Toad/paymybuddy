package openclassroom.p6.paymybuddy.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "transactions")
public class Transaction {

    public static final double FEE_RATE = 0.005;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    private String senderEmail;

    private String receiverEmail;

    private String description;

    private double amount;

    private double fee = amount * FEE_RATE;

    private LocalDateTime date;

}
