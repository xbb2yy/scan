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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {


    protected static Map<String, Province> provinceMap = new HashMap<>();
    protected static Map<String, City> cityMap = new HashMap<>();
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private JSONObject proGroupNum;

    @FXML
    private ListView listView;
    @FXML
    private ChoiceBox<Province> box1;
    @FXML
    private ChoiceBox<City> box2;
    @FXML
    private ListView all;
    @FXML
    private ListView aaa;
    @FXML
    private ListView aaaa;
    @FXML
    private RadioButton aaaBtn;

    // 任务是否启动
    private static volatile boolean start = true;

    private final String uri = "https://m.10010.com/king/kingNumCard/init?product=4&channel=1306";


    public void initialize(URL location, ResourceBundle resources) {

        // 初始化ListView
        ObservableList<String> items = FXCollections.observableArrayList(
                "大王卡", "米粉卡", "星粉卡");
        listView.setItems(items);

        // 初始化ChoiceBox
        HttpGet httpGet = new HttpGet(uri);
        try (CloseableHttpResponse response = httpclient.execute(httpGet)){
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void search() {
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
            try(CloseableHttpResponse r = httpclient.execute(get)) {
                String string = EntityUtils.toString(r.getEntity());
                StringBuilder sb = new StringBuilder(string);
                sb.delete(0, 20);
                sb.deleteCharAt(sb.length() - 1);
                System.out.println(sb);
                JSONArray numArray = JSONObject.parseObject(sb.toString()).getJSONArray("numArray");
                numArray.forEach(n -> {
                    if (n.toString().length() == 11) {
                        Platform.runLater(() -> all.getItems().add(n.toString()));
                    }

                    // aaa
                    if(aaaBtn.isSelected()) {
                        Pattern pattern = Pattern.compile("([\\d])\\1{2,}");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> aaa.getItems().add(n.toString()));
                        }
                    }

                    // aaaa
                    Pattern pattern1 = Pattern.compile("([\\d])\\1{3,}");
                    Matcher matcher1 = pattern1.matcher(n.toString());
                    if (matcher1.find()) {
                        Platform.runLater(() -> aaaa.getItems().add(n.toString()));
                    }
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }, 1, 5, TimeUnit.SECONDS);

    }

    @FXML
    private void stop() {
        start = false;
    }

    @FXML
    private void clear(MouseEvent event) {
        System.out.println(event.toString());
        Label label = (Label) event.getSource();
        Node node = label.getLabelFor();

    }

    @FXML
    private void clearAll(MouseEvent mouseEvent) {
        all.getItems().clear();
    }

    @FXML
    public void applyWangka(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("http://www.baidu.com"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
