import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    @Test
    public void abcde() {
        Pattern pattern = Pattern.compile("(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){4}\\d");
        Matcher matcher = pattern.matcher("123456");
        System.out.println(matcher.find());

        Matcher matcher1 = pattern.matcher("13123454345");
        System.out.println(matcher1.find());

        Matcher matcher2 = pattern.matcher("122456");
        System.out.println(matcher2.find());
    }

    @Test
    public void aaabbb() {
        Pattern pattern = Pattern.compile("(\\d)\\1{2}((?!\\1)\\d)\\2{2}");
        Matcher matcher = pattern.matcher("123456");
        System.out.println(matcher.find());

        Matcher matcher1 = pattern.matcher("13123454345");
        System.out.println(matcher1.find());

        Matcher matcher2 = pattern.matcher("122456");
        System.out.println(matcher2.find());

        Matcher matcher3 = pattern.matcher("122244456");
        System.out.println(matcher3.find());

        Matcher matcher4 = pattern.matcher("22244456");
        System.out.println(matcher4.find());

        Matcher matcher5 = pattern.matcher("2222444555666");
        System.out.println(matcher5.find());
    }




    @Test
    public void test() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.shutdown();


        executorService.execute(() -> System.out.println(1));
    }
}
