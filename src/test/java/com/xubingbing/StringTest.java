package com.xubingbing;

import org.junit.Test;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StringTest {

    @Test
    public void testFormat() {
        String str = "https://m.10010.com/NumApp/NumberCenter/qryNum?callback=jsonp_queryMoreNums&provinceCode=%s" +
                "&cityCode=%s&monthFeeLimit=0&groupKey=%s&searchCategory=3&net=01&amounts=200&codeTypeCode=&searchValue=&qryType=02&goodsNet=4&_=%s";
        System.out.println(String.format(str, 1, 2, 3, 4));
    }

    @Test
    public void testFindAny() {
        Map<String, String> map = new HashMap<>();
        map.put("2", "3");
        map.put("1", "2");
        map.put("3", "4");

        // Optional<Map.Entry<String, String>> any = map.entrySet().parallelStream().p.findAny();
       // System.out.println(any.get().getKey());
    }


}
