package com.xubingbing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xubingbing.Patterns.*;

public class Controller implements Initializable {

    private final static Logger LOG = LoggerFactory.getLogger(Controller.class);
    static Map<String, Province> provinceMap = new HashMap<>();
    static Map<Province, List<City>> provinceCity = new HashMap<>();
    static Map<String, City> cityMap = new HashMap<>();
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private JSONObject proGroupNum;
    private LinkedHashSet<String> allNums = new LinkedHashSet<>();
    private final String queryUrlFormat = "https://m.10010.com/NumApp/NumberCenter/qryNum?callback=jsonp_queryMoreNums&provinceCode=%s" +
            "&cityCode=%s&monthFeeLimit=0&groupKey=%s&searchCategory=3&net=01&amounts=200&codeTypeCode=&searchValue=&qryType=02&goodsNet=4&_=%s";

    private Random rand = new Random();
    @FXML
    private ChoiceBox<Province> box1;
    @FXML
    private ChoiceBox<City> box2;
    @FXML
    private ListView<String> listView;
    @FXML
    private ListView all, aaa, aaa2, aaa3, aaa4, aaa5, aaa6, aaa7, aaa8, aaa9, aaa10, aaa11, aaa12;
    @FXML
    private RadioButton aaaBtn, aaaBtn2, aaaBtn3, aaaBtn4, aaaBtn5, aaaBtn6, aaaBtn7, aaaBtn8, aaaBtn9, aaaBtn10,
            aaaBtn11, aaaBtn12;
    @FXML
    private TextField exclude, custom;

    // 任务是否启动
    private static volatile boolean start = true;
    private static volatile boolean allCity = true;
    private final String wang = "https://m.10010.com/king/kingNumCard/init?product=4&channel=1306";
    private final String mi = "https://m.10010.com/king/kingNumCard/newmiinit?product=1";
    private final String ali = "https://m.10010.com/king/kingNumCard/alibaoinit?product=1";


    public void initialize(URL location, ResourceBundle resources) {

        LOG.info("应用启动");
        // 初始化ListView
        ObservableList<String> items = FXCollections.observableArrayList(
                "大王卡", "米粉卡", "阿里宝卡");
        listView.setItems(items);
        listView.getSelectionModel().selectFirst();

        // 初始化ChoiceBox
        HttpGet httpGet = new HttpGet(wang);
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            String s = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(s);

            // 初始化省份
            String provinceData = jsonObject.getString("provinceData");
            box1.setConverter(new ProvinceConverter());
            List<Province> provinces = JSON.parseArray(provinceData, Province.class);
            provinces.forEach(p -> {
                provinceMap.put(p.getPROVINCE_NAME(), p);
                JSONObject cityData = jsonObject.getJSONObject("cityData");
                List<City> cities = JSON.parseArray(cityData.getString(p.getPROVINCE_CODE().toString()), City.class);
                provinceCity.put(p, cities);
            });

            LOG.info(JSON.toJSONString(provinceCity));
            ObservableList<Province> province = FXCollections.observableArrayList(provinces);
            box1.setItems(province);
            box1.getSelectionModel().selectFirst();

            // 初始化城市
            box2.setConverter(new CityConverter());
            ChangeListener<Province> changeListener = (ObservableValue<? extends Province> observable, Province oldValue,
                                                       Province newValue) -> {
                Province p = provinceMap.get(newValue.getPROVINCE_NAME());
                JSONObject cityData = jsonObject.getJSONObject("cityData");
                List<City> cities = JSON.parseArray(cityData.getString(p.getPROVINCE_CODE().toString()), City.class);
                cities.forEach(c -> {
                    cityMap.put(c.getCITY_NAME(), c);
                });
                box2.setItems(FXCollections.observableArrayList(cities));
                box2.getSelectionModel().selectFirst();
            };

            box1.getSelectionModel().selectedItemProperty().addListener(changeListener);
            Integer code = box1.getSelectionModel().selectedItemProperty().getValue().getPROVINCE_CODE();
            JSONObject cityData = jsonObject.getJSONObject("cityData");
            List<City> cities = JSON.parseArray(cityData.getString(code.toString()), City.class);
            cities.forEach(c -> {
                cityMap.put(c.getCITY_NAME(), c);
            });
            box2.setItems(FXCollections.observableArrayList(cities));
            box2.getSelectionModel().selectFirst();

            // 初始化提示
            Tooltip tip = new Tooltip("输入数字,多个用空格分隔");
            exclude.setTooltip(tip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void search() throws Exception{
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        String groupUrl = wang;
        switch (selectedItem) {
            case "大王卡":
                groupUrl = wang;
                break;
            case "阿里宝卡":
                groupUrl = ali;
                break;
            case "米粉卡":
                groupUrl = mi;
                break;
            default:
        }

        HttpGet httpGet = new HttpGet(groupUrl);
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            String s = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(s);
            // 请求需要Groupkey
            proGroupNum = jsonObject.getJSONObject("proGroupNum");
        }

        start = true;

        ReadOnlyObjectProperty<Province> property = box1.getSelectionModel().selectedItemProperty();
        Integer provinceCode = property.getValue().getPROVINCE_CODE();
        Integer cityCode = box2.getSelectionModel().selectedItemProperty().getValue().getCITY_CODE();
        String queryUrl = String.format(queryUrlFormat, provinceCode, cityCode, proGroupNum.getString(provinceCode.toString()),
                System.currentTimeMillis());
        LOG.info(queryUrl);
        HttpGet get = new HttpGet(queryUrl);
        get.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N)" +
                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36");
        get.addHeader("Referer", "https://m.10010.com/queen/tencent/tencent-pc-fill.html" +
                "?product=4&channel=1306");

        LOG.info("搜索{},省份:{}，城市:{}启动", selectedItem,property.get().getPROVINCE_NAME(), box2.getSelectionModel()
                .selectedItemProperty().get().getCITY_NAME());

        service.scheduleWithFixedDelay(() -> {
            while (!start) {
                return;
            }
            final City c ;
            // 全国搜索
            if (allCity) {
                Set<Map.Entry<Province, List<City>>> entries = provinceCity.entrySet();
                List<Map.Entry<Province, List<City>>> list = new ArrayList<>(entries);
                Map.Entry<Province, List<City>> provinceListEntry = list.get(rand.nextInt(list.size()));
                Integer province = provinceListEntry.getKey().getPROVINCE_CODE();
                List<City> cities = provinceListEntry.getValue();
                c = cities.get(rand.nextInt(cities.size()));
                Integer city = c.getCITY_CODE();
                String url = String.format(queryUrlFormat, province, city, proGroupNum.getString(province.toString()),
                        System.currentTimeMillis());
                try {
                    get.setURI(new URI(url));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                LOG.info(get.getURI().toString());

            } else {
                c = new City();
            }

            try (CloseableHttpResponse r = httpclient.execute(get)) {
                String string = EntityUtils.toString(r.getEntity());
                StringBuilder sb = new StringBuilder(string);
                sb.delete(0, 20);
                sb.deleteCharAt(sb.length() - 1);
                LOG.info("响应数据:{}", sb);
                JSONArray numArray = JSONObject.parseObject(sb.toString()).getJSONArray("numArray");
                numArray.forEach(n -> {
                    if (n.toString().length() == 11) {
                        allNums.add(n.toString());
                    }
                    String num = n.toString();
                    String excludeText = exclude.getText();
                    String[] s = excludeText.split(" ");
                    for (String s1 : s) {
                        boolean numeric = Util.isNumeric(s1);
                        if (numeric) {
                            System.out.println("不包含:" + excludeText);
                            if (num.contains(s1)) {
                                return;
                            }
                        }
                    }
                    if (all.getItems().contains(n.toString())) {
                        return;
                    }
                    if (n.toString().length() == 11) {
                        Platform.runLater(() -> {
                            all.getItems().add(n.toString());
                            all.scrollTo(all.getItems().size());
                        });
                    }

                    // AAA
                    process(aaaBtn, a3, aaa, num, c);
                    // 4A+
                    process(aaaBtn2, a4p, aaa2, num, c);
                    // AABBCC
                    process(aaaBtn3, aabbcc, aaa3, num, c);
                    // AABB
                    process(aaaBtn4, aabb, aaa4, num, c);
                    // ABCDE
                    process(aaaBtn5, abcde, aaa5, num, c);
                    // ABCCBA
                    process(aaaBtn6, abccba, aaa6, num, c);
                    // EDCBA
                    process(aaaBtn7, edcba, aaa7, num, c);
                    // ABCD
                    process(aaaBtn8, abcd, aaa8, num, c);
                    // ABCDABCD
                    process(aaaBtn9, abcdabcd, aaa9, num, c);
                    // 只出现三个不同数字
                    process(aaaBtn10, less3, aaa10, num, c);
                    // 同一数字超过5次
                    process(aaaBtn11, one5s, aaa11, num, c);
                    // Custom
                    if (aaaBtn12.isSelected()) {
                        String str = custom.getText();
                        if (null == str || str.trim() == "" || str.length() == 0) {
                            return;
                        }
                        try {
                            Pattern pattern = Pattern.compile(str);
                            process(aaaBtn12, pattern, aaa12, num, c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                LOG.info("已经扫描号码总数为:{}", allNums.size());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);

    }

    @FXML
    private void stop() {
        start = false;
    }

    @FXML
    private void clear(MouseEvent event) {
        Label label = (Label) event.getSource();
        Parent parent = label.getParent().getParent();
        ObservableList<Node> childrenUnmodifiable = parent.getChildrenUnmodifiable();
        for (Node node : childrenUnmodifiable) {
            if (node instanceof ListView) {
                ((ListView) node).getItems().clear();
            }
        }
    }

    @FXML
    private void clearAll() {
        all.getItems().clear();
    }

    @FXML
    public void applyWangKa(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("http://www.baidu.com"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void process(RadioButton btn, Pattern p, ListView listView, String string, City c) {
        if (btn.isSelected()) {
            Matcher matcher = p.matcher(string);
            if (matcher.find()) {
                Platform.runLater(() -> {
                    listView.getItems().add(string + c.getCITY_NAME());
                    listView.scrollTo(listView.getItems().size());
                });

            }
        }
    }


    @FXML
    public void desc(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("1.找到心怡的号码后可以去申请地址，搜索号码后四位.\n2.每天搜索达到一定数量后，可能搜不出任何结果，等一天再试." +
                "\n3.大王卡支持全国发货,所以在任何地区找到的靓号,都可以申请");
        alert.showAndWait();
    }
}
