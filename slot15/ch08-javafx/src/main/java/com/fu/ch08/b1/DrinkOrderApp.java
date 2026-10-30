package com.fu.ch08.b1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class DrinkOrderApp extends Application {

    // Bảng giá — LinkedHashMap giữ đúng thứ tự khi đổ vào ComboBox
    private static final Map<String, Integer> MENU = new LinkedHashMap<>();
    static {
        MENU.put("Cà phê sữa", 25_000);
        MENU.put("Trà sữa", 30_000);
        MENU.put("Nước cam", 28_000);
        MENU.put("Sinh tố bơ", 35_000);
    }
    private static final int TOPPING_PRICE = 5_000;

    // TODO 1: Khai báo control là field
    private final TextField tfName = new TextField();
    private final ComboBox<String> cbDrink = new ComboBox<>();
    private final ToggleGroup tgSize = new ToggleGroup();
    private final RadioButton rbS = new RadioButton("S");
    private final RadioButton rbM = new RadioButton("M (+5.000)");
    private final RadioButton rbL = new RadioButton("L (+10.000)");
    private final CheckBox chkPearl = new CheckBox("Trân châu (+5.000)");
    private final CheckBox chkJelly = new CheckBox("Thạch (+5.000)");
    private final CheckBox chkLessSugar = new CheckBox("Ít đường");
    private final TextField tfQty = new TextField("1");
    private final Button btnCalc = new Button("Tính tiền");
    private final Button btnClear = new Button("Làm lại");
    private final Label lblResult = new Label("Chưa có đơn hàng");
    private final TextArea taLog = new TextArea();

    @Override
    public void start(Stage stage) {
        // TODO 2: Cấu hình control
        tfName.setPromptText("Nhập tên khách hàng");
        cbDrink.getItems().addAll(MENU.keySet());
        cbDrink.setPromptText("-- Chọn đồ uống --");
        cbDrink.setMaxWidth(Double.MAX_VALUE);

        rbS.setToggleGroup(tgSize);
        rbM.setToggleGroup(tgSize);
        rbL.setToggleGroup(tgSize);
        rbS.setUserData(0);                 // userData: gắn dữ liệu tùy ý vào control (ở đây: phụ thu size)
        rbM.setUserData(5_000);
        rbL.setUserData(10_000);
        tgSize.selectToggle(rbM);

        tfQty.setPrefColumnCount(4);
        taLog.setEditable(false);
        taLog.setPrefRowCount(8);
        taLog.setTooltip(new Tooltip("Double-click để xóa nhật ký"));
        btnCalc.setDefaultButton(true);     // Enter = bấm "Tính tiền"

        // TODO 3: Xếp layout (B2 sẽ học kỹ hơn)
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("Tên khách:"), tfName);
        form.addRow(1, new Label("Đồ uống:"), cbDrink);
        form.addRow(2, new Label("Size:"), new HBox(15, rbS, rbM, rbL));
        form.addRow(3, new Label("Tùy chọn:"), new HBox(15, chkPearl, chkJelly, chkLessSugar));
        form.addRow(4, new Label("Số lượng:"), tfQty);

        HBox buttons = new HBox(10, btnCalc, btnClear);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        Label title = new Label("ĐẶT ĐỒ UỐNG");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        VBox root = new VBox(12, title, form, buttons, lblResult, new Label("Nhật ký sự kiện:"), taLog);
        root.setPadding(new Insets(16));

        Scene scene = new Scene(root, 540, 580);
        stage.setScene(scene);
        stage.setTitle("B1 - Controls & Sự kiện");
        stage.show();
        tfName.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
