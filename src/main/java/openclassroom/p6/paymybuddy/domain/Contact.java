package openclassroom.p6.paymybuddy.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;
import java.util.Objects;

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

    @ManyToMany(
            mappedBy = "contacts",
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            }
    )
    private List<User> contactUsers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact addEmail(String email) {
        this.email = email;
        return this;
    }

    public List<User> getContactUsers() {
        return contactUsers;
    }

    public void setContactUsers(List<User> contactUsers) {
        this.contactUsers = contactUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id == contact.id && Objects.equals(email, contact.email) && Objects.equals(contactUsers, contact.contactUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, contactUsers);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Contact{");
        sb.append("id=").append(id);
        sb.append(", email='").append(email).append('\'' );
        sb.append(", contactUsers=").append(contactUsers);
        sb.append('}' );
        return sb.toString();
    }
}
