import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        Matcher matcher1 = pattern.matcher("13123454345");
        System.out.println(matcher1.find());

        Matcher matcher2 = pattern.matcher("122456");
        System.out.println(matcher2.find());

        Matcher matcher3 = pattern.matcher("1265432");
        System.out.println(matcher3.find());
    }

    @Test
    public void edcba() {
        Pattern pattern = Pattern.compile("^\\d(?:(?:0(?=9)|9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){3,})\\d");
        Matcher matcher = pattern.matcher("87654");
        System.out.println(matcher.find());

        Matcher matcher1 = pattern.matcher("13123454345");
        System.out.println(matcher1.find());

        Matcher matcher2 = pattern.matcher("122456");
        System.out.println(matcher2.find());

        Matcher matcher3 = pattern.matcher("654321");
        System.out.println(matcher3.find());
    }

    @Test
    public void abcdabcd() {
        Pattern pattern = Pattern.compile("(\\d)(\\d)(\\d)(\\d)\\1\\2\\3\\4");


        Matcher matcher = pattern.matcher("153123412343");
        assertTrue(matcher.find());

        Matcher matcher1 = pattern.matcher("131234123454345");
        assertTrue(matcher1.find());

        Matcher matcher2 = pattern.matcher("122456");
        assertFalse(matcher2.find());

        Matcher matcher3 = pattern.matcher("654321");
        assertFalse(matcher3.find());
    }

    @Test
    public void unique3() {
        Pattern pattern = Pattern.compile("^(?=(\\d)\\1*(\\d)(?:\\1|\\2)*(\\d)(?:\\1|\\2|\\3)*$)\\d{11}$");


        Matcher matcher = pattern.matcher("153123412343");
        assertFalse(matcher.find());

        Matcher matcher1 = pattern.matcher("13123452345");
        assertFalse(matcher1.find());

        Matcher matcher2 = pattern.matcher("13133113133");
        assertTrue(matcher2.find());

        Matcher matcher3 = pattern.matcher("18989898989");
       assertTrue(matcher3.find());
    }

    @Test
    public void n5() {
        Pattern pattern = Pattern.compile("^(?=\\d*(\\d)\\d*(?:\\1\\d*){4})\\d{11}$");


        Matcher matcher = pattern.matcher("153123412343");
        assertFalse(matcher.find());

        Matcher matcher1 = pattern.matcher("13123452345");
        assertFalse(matcher1.find());

        Matcher matcher2 = pattern.matcher("13133113133");
        assertTrue(matcher2.find());

        Matcher matcher3 = pattern.matcher("18989898989");
        assertTrue(matcher3.find());

        Matcher matcher4 = pattern.matcher("15313332339");
        assertTrue(matcher4.find());
    }

    @Test
    public void aaabbb() {
        Pattern pattern = Pattern.compile("(\\d)\\1{2}((?!\\1)\\d)\\2{2}");
        Matcher matcher = pattern.matcher("123456");
        assertFalse(matcher.find());

        Matcher matcher1 = pattern.matcher("13123454345");
        assertFalse(matcher1.find());

        Matcher matcher2 = pattern.matcher("122456");
        assertFalse(matcher2.find());

        Matcher matcher3 = pattern.matcher("122244456");
        assertTrue(matcher3.find());

        Matcher matcher4 = pattern.matcher("22244456");
        assertTrue(matcher4.find());

        Matcher matcher5 = pattern.matcher("2222444555666");
        assertTrue(matcher5.find());
    }

    @Test(expected = Exception.class)
    public void test() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.shutdown();
        executorService.execute(() -> System.out.println(1));
    }
}
