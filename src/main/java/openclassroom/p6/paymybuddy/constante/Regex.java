package openclassroom.p6.paymybuddy.constante;

public class Regex {

    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
    public static final String NAME = "^[A-Za-z]+$";

    private void RegexValidation() {}
}
