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
    @Column
    private Integer id;

    @Column(unique = true)
//    @Email
    private String email;

    @ManyToMany(
            mappedBy = "contacts",
            cascade = {
//                    CascadeType.MERGE,
                    CascadeType.PERSIST
            },
            fetch = FetchType.EAGER
    )
    private List<User> contactUsers = new ArrayList<>();

    public void addUser(User user) {
        contactUsers.add(user);
//        user.addContact(this);
    }

    public void removeUser(User user) {
        contactUsers.remove(user);
//        user.removeContact(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Contact{");
        sb.append("id=").append(id);
        sb.append(", email='").append(email).append('\'');
        sb.append(", contactUserIds=").append(contactUsers.stream().map(User::getId));
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact contact)) return false;
        return Objects.equals(id, contact.id) && Objects.equals(email, contact.email) && Objects.equals(contactUsers, contact.contactUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, contactUsers);
    }
}
