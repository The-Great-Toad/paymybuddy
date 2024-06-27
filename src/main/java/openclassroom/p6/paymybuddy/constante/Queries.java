package openclassroom.p6.paymybuddy.constante;

public class Queries {

    public static final String GET_USER_CONTACTS = "SELECT * FROM contact c WHERE c.email IN (SELECT contact_id FROM user_contact WHERE user_id = ?1)";

    public static final String GET_RECENT_TRANSACTIONS = "SELECT * FROM transactions WHERE sender_email = ?1 ORDER BY date DESC LIMIT 2";
}
