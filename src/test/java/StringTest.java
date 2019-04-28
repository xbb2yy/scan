import org.junit.Test;

public class StringTest {

    @Test
    public void testFormat() {
        String str = "https://m.10010.com/NumApp/NumberCenter/qryNum?callback=jsonp_queryMoreNums&provinceCode=%s" +
                "&cityCode=%s&monthFeeLimit=0&groupKey=%s&searchCategory=3&net=01&amounts=200&codeTypeCode=&searchValue=&qryType=02&goodsNet=4&_=%s";
        System.out.println(String.format(str, 1, 2, 3, 4));
    }
}
