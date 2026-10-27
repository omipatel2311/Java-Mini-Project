package util;
import java.util.regex.Pattern;

public class Validator {

    public static boolean isValidMobile(String mobile) {
        return Pattern.matches("[6-9][0-9]{9}", mobile);
    }

    public static boolean isValidEmail(String email) {
        return Pattern.matches(
            "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}",
            email
        );
    }

    public static boolean isValidPan(String pan) {
        return Pattern.matches("[A-Z]{5}[0-9]{4}[A-Z]", pan);
    }

    public static boolean isValidIfsc(String ifsc) {
        return Pattern.matches("[A-Z]{4}0[A-Z0-9]{6}", ifsc);
    }
}
