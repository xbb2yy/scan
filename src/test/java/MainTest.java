import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {

    public static void main(String[] args) {

        Pattern p = Pattern.compile("([\\d])\\1{2,}");
        Pattern p1 = Pattern.compile("([\\d])\\1{3,}");
        Matcher matcher = p.matcher("18865888");
        System.out.println(matcher.find());
        System.out.println(matcher.matches());

        Matcher matcher1 = p1.matcher("188888");
        Matcher matcher2 = p1.matcher("1888");
        Matcher matcher3 = p1.matcher("1888346666");
        System.out.println(matcher1.find());
        System.out.println(matcher2.find());
        System.out.println(matcher3.find());
    }
}
