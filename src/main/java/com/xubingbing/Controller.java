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

    protected static Map<String, Province> provinceMap = new HashMap<>();
    protected static Map<String, City> cityMap = new HashMap<>();
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private JSONObject proGroupNum;
    private LinkedHashSet<String> allNums = new LinkedHashSet<>();

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
    private final String uri = "https://m.10010.com/king/kingNumCard/init?product=4&channel=1306";
    private final String mi = "https://m.10010.com/king/kingNumCard/newmiinit?product=0";


    public void initialize(URL location, ResourceBundle resources) {

        // 初始化ListView
        ObservableList<String> items = FXCollections.observableArrayList(
                "大王卡", "米粉卡(待开发)", "星粉卡(待开发)");
        listView.setItems(items);

        // 初始化ChoiceBox
        HttpGet httpGet = new HttpGet(uri);
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            String s = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(s);

            // 初始化省份
            String provinceData = jsonObject.getString("provinceData");
            box1.setConverter(new ProvinceConverter());
            List<Province> provinces = JSON.parseArray(provinceData, Province.class);
            provinces.forEach(p -> {
                provinceMap.put(p.getPROVINCE_NAME(), p);
            });
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

            // 请求需要Groupkey
            proGroupNum = jsonObject.getJSONObject("proGroupNum");

            // 初始化提示
            Tooltip tip = new Tooltip("输入数字,多个用空格分隔");
            exclude.setTooltip(tip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void search() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        switch (selectedItem) {
            case "大王卡":
            case "星粉卡":
            case "米粉卡":
                break;
            default:
        }

        start = true;
        ReadOnlyObjectProperty<Province> property = box1.getSelectionModel().selectedItemProperty();
        Integer provinceCode = property.getValue().getPROVINCE_CODE();
        Integer cityCode = box2.getSelectionModel().selectedItemProperty().getValue().getCITY_CODE();
        StringBuilder builder = new StringBuilder("https://m.10010.com/NumApp/NumberCenter/qryNum?callback=" +
                "jsonp_queryMoreNums&provinceCode=");
        builder.append(provinceCode).append("&cityCode=").append(cityCode).append("&monthFeeLimit=0&groupKey=")
                .append(proGroupNum.getString(provinceCode.toString())).append("&searchCategory=3&net=01" +
                "&amounts=200&codeTypeCode=&searchValue=&qryType=02&goodsNet=4&_=");
        builder.append(System.currentTimeMillis());

        System.out.println(builder.toString());
        HttpGet get = new HttpGet(builder.toString());
        get.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N)" +
                " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Mobile Safari/537.36");
        get.addHeader("Referer", "https://m.10010.com/queen/tencent/tencent-pc-fill.html" +
                "?product=4&channel=1306");

        service.scheduleWithFixedDelay(() -> {
            while (!start) {
                return;
            }
            try (CloseableHttpResponse r = httpclient.execute(get)) {
                String string = EntityUtils.toString(r.getEntity());
                StringBuilder sb = new StringBuilder(string);
                sb.delete(0, 20);
                sb.deleteCharAt(sb.length() - 1);
                System.out.println(sb);
                JSONArray numArray = JSONObject.parseObject(sb.toString()).getJSONArray("numArray");
                numArray.forEach(this::accept);
                System.out.println(allNums.size());
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

    private static void process(RadioButton btn, Pattern p, ListView listView, String string) {
        if (btn.isSelected()) {
            Matcher matcher = p.matcher(string);
            if (matcher.find()) {
                Platform.runLater(() -> {
                    listView.getItems().add(string);
                    listView.scrollTo(listView.getItems().size());
                });

            }
        }
    }

    private void accept(Object n) {
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
        process(aaaBtn, a3, aaa, num);
        // 4A+
        process(aaaBtn2, a4p, aaa2, num);
        // AABBCC
        process(aaaBtn3, aabbcc, aaa3, num);
        // AABB
        process(aaaBtn4, aabb, aaa4, num);
        // ABCDE
        process(aaaBtn5, abcde, aaa5, num);
        // ABCCBA
        process(aaaBtn6, abccba, aaa6, num);
        // EDCBA
        process(aaaBtn7, edcba, aaa7, num);
        // ABCD
        process(aaaBtn8, abcd, aaa8, num);
        // ABCDABCD
        process(aaaBtn9, abcdabcd, aaa9, num);
        // 只出现三个不同数字
        process(aaaBtn10, less3, aaa10, num);
        // 同一数字超过5次
        process(aaaBtn11, one5s, aaa11, num);
        // Custom
        if (aaaBtn12.isSelected()) {
            String str = custom.getText();
            if (null == str || str.trim() == "" || str.length() == 0) {
                return;
            }
            try {
                Pattern pattern = Pattern.compile(str);
                process(aaaBtn12, pattern, aaa12, num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void desc(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("hello");
        alert.show();
        alert.setOnCloseRequest(event ->{
            alert.close();
        });


    }
}
