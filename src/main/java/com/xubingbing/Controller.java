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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xubingbing.Patterns.abcdabcd;

public class Controller implements Initializable {

    protected static Map<String, Province> provinceMap = new HashMap<>();
    protected static Map<String, City> cityMap = new HashMap<>();
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private JSONObject proGroupNum;

    @FXML
    private ChoiceBox<Province> box1;
    @FXML
    private ChoiceBox<City> box2;
    @FXML
    private ListView listView, all, aaa, aaa2, aaa3, aaa4, aaa5, aaa6, aaa7, aaa8, aaa9, aaa10, aaa11, aaa12;
    @FXML
    private RadioButton aaaBtn, aaaBtn2, aaaBtn3, aaaBtn4, aaaBtn5, aaaBtn6, aaaBtn7, aaaBtn8, aaaBtn9, aaaBtn10,
            aaaBtn11, aaaBtn12;
    @FXML
    private TextField exclude, custom;

    // 任务是否启动
    private static volatile boolean start = true;
    private final String uri = "https://m.10010.com/king/kingNumCard/init?product=4&channel=1306";


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
                numArray.forEach(n -> {
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
                    if (aaaBtn.isSelected()) {
                        Pattern pattern = Pattern.compile("([\\d])\\1{2,}");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa.getItems().add(n.toString());
                                aaa.scrollTo(aaa.getItems().size());
                            });

                        }
                    }

                    // 4A+
                    if (aaaBtn2.isSelected()) {
                        Pattern pattern = Pattern.compile("([\\d])\\1{3,}");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa2.getItems().add(n.toString());
                                aaa2.scrollTo(aaa2.getItems().size());
                            });
                        }
                    }

                    // AABBCC
                    if (aaaBtn3.isSelected()) {
                        Pattern pattern = Pattern.compile("(\\d)\\1((?!\\1)\\d)\\2((?!\\2)(?!\\1)\\d)\\3");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa3.getItems().add(n.toString());
                                aaa3.scrollTo(aaa3.getItems().size());
                            });
                        }
                    }

                    // AABB
                    if (aaaBtn4.isSelected()) {
                        Pattern pattern = Pattern.compile("(\\d)\\1{1}((?!\\1)\\d)\\2{1}");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa4.getItems().add(n.toString());
                                aaa4.scrollTo(aaa4.getItems().size());
                            });
                        }
                    }

                    // ABCDE
                    if (aaaBtn5.isSelected()) {
                        Pattern pattern = Pattern.compile("(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){4}\\d");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa5.getItems().add(n.toString());
                                aaa5.scrollTo(aaa5.getItems().size());
                            });
                        }
                    }

                    // ABCCBA
                    if (aaaBtn6.isSelected()) {
                        Pattern pattern = Pattern.compile("(\\d)((?!\\2)\\d)((?!\\2)(?!\\3)\\d)\\3\\2\\1");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa6.getItems().add(n.toString());
                                aaa6.scrollTo(aaa6.getItems().size());
                            });
                        }
                    }

                    // EDCBA
                    if (aaaBtn7.isSelected()) {
                        Pattern pattern = Pattern.compile("^\\d(?:(?:0(?=9)|9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){3,})\\d");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa7.getItems().add(n.toString());
                                aaa7.scrollTo(aaa7.getItems().size());
                            });
                        }
                    }

                    // ABCD
                    if (aaaBtn8.isSelected()) {
                        Pattern pattern = Pattern.compile("(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){3}\\d");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa8.getItems().add(n.toString());
                                aaa8.scrollTo(aaa8.getItems().size());
                            });
                        }
                    }

                    // ABCDABCD
                    if (aaaBtn9.isSelected()) {
                        Matcher matcher = abcdabcd.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa9.getItems().add(n.toString());
                                aaa9.scrollTo(aaa9.getItems().size());
                            });
                        }
                    }
                    // 只出现三个不同数字
                    if (aaaBtn10.isSelected()) {
                        Pattern pattern = Pattern.compile("^(?=(\\d)\\1*(\\d)(?:\\1|\\2)*(\\d)(?:\\1|\\2|\\3)*$)\\d{11}$");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa10.getItems().add(n.toString());
                                aaa10.scrollTo(aaa10.getItems().size());
                            });
                        }
                    }

                    // 相同数字超过5次
                    if (aaaBtn11.isSelected()) {
                        Pattern pattern = Pattern.compile("^(?=\\d*(\\d)\\d*(?:\\1\\d*){4})\\d{11}$\n");
                        Matcher matcher = pattern.matcher(n.toString());
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                aaa11.getItems().add(n.toString());
                                aaa11.scrollTo(aaa11.getItems().size());
                            });
                        }
                    }
                    // Custom
                    if (aaaBtn12.isSelected()) {
                        String str = custom.getText();
                        if (null == str || str.trim() =="" || str.length() == 0) {
                            return;
                        }
                        try {
                            Pattern pattern = Pattern.compile(str);
                            Matcher matcher = pattern.matcher(n.toString());
                            if (matcher.find()) {
                                Platform.runLater(() -> {
                                    aaa12.getItems().add(n.toString());
                                    aaa12.scrollTo(aaa12.getItems().size());
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
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

}
