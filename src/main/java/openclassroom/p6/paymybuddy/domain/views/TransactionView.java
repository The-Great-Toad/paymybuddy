package openclassroom.p6.paymybuddy.domain.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionView {

    private String senderEmail;
    private String receiverEmail;
    private String description;
    private double amount;
    private LocalDateTime date;
}
