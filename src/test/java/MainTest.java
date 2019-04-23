import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {

    public static void main(String[] args) {

        Pattern p = Pattern.compile("([\\d])\\1{2,}");
        Matcher matcher = p.matcher("18865888");
        System.out.println(matcher.find());
        System.out.println(matcher.matches());
    }
}
