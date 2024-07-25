package openclassroom.p6.paymybuddy.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Integer id;

    @Column(unique = true)
    private String email;

    @ManyToMany(
            mappedBy = "contacts",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER
    )
    private List<User> userEmails = new ArrayList<>();

    public void addUser(User user) {
        userEmails.add(user);
    }

    public void removeUser(User user) {
        userEmails.remove(user);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Contact{");
        sb.append(", email='").append(email).append('\'');
        sb.append(", userEmails=").append(userEmails.stream().map(User::getEmail));
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact contact)) return false;
        return Objects.equals(email, contact.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, userEmails);
    }
}
