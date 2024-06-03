package openclassroom.p6.paymybuddy.constante;

public class Messages {

    // Validation
    public static final String ALPHA_CHAR_ONLY = "Please enter alphabetic characters";
    public static final String NOT_NULL = "This field can not be null";

    public static final String OLD_PWD_NOT_NULL = "Please enter your current password";
    public static final String NEW_PWD_NOT_NULL = "Please enter a new password";
    public static final String CONFIRM_PWD_NOT_NULL = "Please confirm your new password";
    public static final String PASSWORD_POLICY = "Password do not respect password policy";
    public static final String PASSWORD_NOT_MATCH = "Passwords do not match";
    public static final String PASSWORD_INVALID = "Invalid password";

    public static final String ACCOUNT_INSUFFICIENT_FUNDS = "Insufficient funds";
    public static final String ACCOUNT_MIN_DEPOSIT = "Minimum amount of €10 for deposit/withdrawal";

    // Success
    public static final String USER_INFO_SUCCESS = "You information have been updated!";
    public static final String PASSWORD_SUCCESS = "You password has been changed!";
    public static final String WITHDRAW_SUCCESS = "Your withdraw was successful";
    public static final String DEPOSIT_SUCCESS = "Your deposit was successful";

    private Messages() {}
}
