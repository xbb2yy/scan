import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

public class JsonTest {
    public static final String str = "{\"code\":\"M0\",\"featureNameList\":[\"\"],\"numArray\":[16643791351,0,0,0,1,0,0,0,0,0,0,0,16643939273,0,0,0,1,0,0,0,0,0,0,0,16604431897,0,0,0,1,0,0,0,0,0,0,0,16643937655,0,0,0,1,0,0,0,0,0,0,0,16643791392,0,0,0,1,0,0,0,0,0,0,0,16643791087,0,0,0,1,0,0,0,0,0,0,0,16643933262,0,0,0,1,0,0,0,0,0,0,0,16643519731,0,0,0,1,0,0,0,0,0,0,0,16643549387,0,0,0,1,0,0,0,0,0,0,0,16604431653,0,0,0,1,0,0,0,0,0,0,0,16643538576,0,0,0,1,0,0,0,0,0,0,0,16643938190,0,0,0,1,0,0,0,0,0,0,0,16643538739,0,0,0,1,0,0,0,0,0,0,0,17549685095,0,0,0,1,0,0,0,0,0,0,0,16643937922,0,0,0,1,0,0,0,0,0,0,0,16643418302,0,0,0,1,0,0,0,0,0,0,0,16643933134,0,0,0,1,0,0,1,0,0,0,0,16643578752,0,0,0,1,0,0,0,0,0,0,0,16643933298,0,0,0,1,0,0,0,0,0,0,0,16643937961,0,0,0,1,0,0,0,0,0,0,0,16643938316,0,0,0,1,0,0,0,0,0,0,0,18544129210,0,0,0,1,0,0,0,0,0,0,0,17543817357,0,0,0,1,0,0,0,0,0,0,0,16643519730,0,0,0,1,0,0,0,0,0,0,0,17549685976,0,0,0,1,0,0,0,0,0,0,0,17543817013,0,0,0,1,0,0,0,0,0,0,0,16643938752,0,0,0,1,0,0,0,0,0,0,0,16643417105,0,0,0,1,0,0,0,0,0,0,0,16643938213,0,0,0,1,0,0,0,0,0,0,0,16643939755,0,0,0,1,0,0,0,0,0,0,0,16643939631,0,0,0,1,0,0,0,0,0,0,0,16643552679,0,0,0,1,0,0,0,0,0,0,0,16643519715,0,0,0,1,0,0,0,0,0,0,0,16643938083,0,0,0,1,0,0,0,0,0,0,0,16604423015,0,0,0,1,0,0,0,0,0,0,0,17543816952,0,0,0,1,0,0,0,0,0,0,0,16643932671,0,0,0,1,0,0,0,0,0,0,0,16643938523,0,0,0,1,0,0,0,0,0,0,0,16643937912,0,0,0,1,0,0,0,0,0,0,0,16643938058,0,0,0,1,0,0,0,0,0,0,0,16643938958,0,0,0,1,0,0,0,0,0,0,0,16643932170,0,0,0,1,0,0,0,0,0,0,0,16643938235,0,0,0,1,0,0,0,0,0,0,0,17543817006,0,0,0,1,0,0,0,0,0,0,0,16643531731,0,0,0,1,0,0,0,0,0,0,0,16643579053,0,0,0,1,0,0,0,0,0,0,0,16643938197,0,0,0,1,0,0,0,0,0,0,0,16643937750,0,0,0,1,0,0,0,0,0,0,0,16643937636,0,0,0,1,0,0,0,0,0,0,0,16643791022,0,0,0,1,0,0,0,0,0,0,0,16643938716,0,0,0,1,0,0,0,0,0,0,0,17543816957,0,0,0,1,0,0,0,0,0,0,0,16643531381,0,0,0,1,0,0,0,0,0,0,0,16643938131,0,0,0,1,0,0,0,0,0,0,0,16643531805,0,0,0,1,0,0,0,0,0,0,0,16643549578,0,0,0,1,0,0,0,0,0,0,0,18544129178,0,0,0,1,0,0,0,0,0,0,0,16643549852,0,0,0,1,0,0,0,0,0,0,0,16643791076,0,0,0,1,0,0,0,0,0,0,0,16643791598,0,0,0,1,0,0,0,0,0,0,0,16643418193,0,0,0,1,0,0,0,0,0,0,0,16643791179,0,0,0,1,0,0,0,0,0,0,0,16643938531,0,0,0,1,0,0,0,0,0,0,0,16643519635,0,0,0,1,0,0,0,0,0,0,0,16643938963,0,0,0,1,0,0,0,0,0,0,0,16643443172,0,0,0,1,0,0,0,0,0,0,0,16643932892,0,0,0,1,0,0,0,0,0,0,0,16643443185,0,0,0,1,0,0,0,0,0,0,0,16643939583,0,0,0,1,0,0,0,0,0,0,0,16643939675,0,0,0,1,0,0,0,0,0,0,0,16643791535,0,0,0,1,0,0,0,0,0,0,0,16643791670,0,0,0,1,0,0,0,0,0,0,0,17543817101,0,0,0,1,0,0,0,0,0,0,0,16643538952,0,0,0,1,0,0,0,0,0,0,0,16643932352,0,0,0,1,0,0,0,0,0,0,0,16643931935,0,0,0,1,0,0,0,0,0,0,0,16643938125,0,0,0,1,0,0,0,0,0,0,0,17543817016,0,0,0,1,0,0,0,0,0,0,0,16643791509,0,0,0,1,0,0,0,0,0,0,0,16643416972,0,0,0,1,0,0,0,0,0,0,0,18544128609,0,0,0,1,0,0,0,0,0,0,0,16643552752,0,0,0,1,0,0,0,0,0,0,0,16643938907,0,0,0,1,0,0,0,0,0,0,0,16643933125,0,0,0,1,0,0,0,0,0,0,0,16643938731,0,0,0,1,0,0,0,0,0,0,0,16643932853,0,0,0,1,0,0,0,0,0,0,0,16643932563,0,0,0,1,0,0,0,0,0,0,0,16643791205,0,0,0,1,0,0,0,0,0,0,0,18544129251,0,0,0,1,0,0,0,0,0,0,0,16643549792,0,0,0,1,0,0,0,0,0,0,0,16643939215,0,0,0,1,0,0,0,0,0,0,0,16643791281,0,0,0,1,0,0,0,0,0,0,0,16643549753,0,0,0,1,0,0,0,0,0,0,0,16643538759,0,0,0,1,0,0,0,0,0,0,0,13196048312,0,0,0,1,0,0,0,0,0,0,0,17543817320,0,0,0,1,0,0,0,0,0,0,0,16643939230,0,0,0,1,0,0,0,0,0,0,0,16643932670,0,0,0,1,0,0,0,0,0,0,0,17543816892,0,0,0,1,0,0,0,0,0,0,0,17543817363,0,0,0,1,0,0,0,0,0,0,0],\"numRetailList\":[\"\"],\"provinceShowHuiTag\":\"0\",\"splitLen\":\"12\",\"uuid\":\"d2071553-dfdd-4e0b-b91c-0844e02ef8d7\"}";
    public static final String str1 = "{\"code\":\"M1\",\"numArray\":[],\"provinceShowHuiTag\":\"0\",\"splitLen\":\"12\",\"uuid\":\"b4779ec9-5455-464d-a98e-68b101bc4b82\"}";
    public static void main(String[] args) {
        JSONObject object = JSON.parseObject(str);
        String code = object.getString("code");
        System.out.println(code);
    }

    @Test
    public void test1() {
        JSONObject object = JSON.parseObject(str1);
        String code = object.getString("code");
        System.out.println(code);
    }
}