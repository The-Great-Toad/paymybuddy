package openclassroom.p6.paymybuddy.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(unique = true)
    private String email;

//    @ManyToMany(
//            mappedBy = "contacts",
//            cascade = {
//                    CascadeType.MERGE,
//                    CascadeType.PERSIST
//            },
//            fetch = FetchType.EAGER
//    )
//    private List<User> contactUsers;

}
