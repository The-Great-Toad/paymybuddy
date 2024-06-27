package openclassroom.p6.paymybuddy.constante;

public class Queries {

    public static final String GET_USER_CONTACTS = "SELECT * FROM contact c WHERE c.email IN (SELECT contact_id FROM user_contact WHERE user_id = ?1)";
}
