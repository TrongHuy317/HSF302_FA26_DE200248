package com.fu.ch08.b3;

import com.fu.ch08.common.AlertUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public class RegisterApp extends Application {

    private static final String STRONG_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$";
    private static final String[] LEVEL_TEXT  = {"Rất yếu", "Yếu", "Trung bình", "Khá", "Mạnh"};
    private static final String[] LEVEL_COLOR = {"#d93025", "#d93025", "#f29900", "#1a73e8", "#188038"};

    private Scene scene;
    private Parent registerView;

    // ===== Control form đăng ký =====
    private final TextField tfFullName = new TextField();
    private final TextField tfEmail = new TextField();
    private final TextField tfUsername = new TextField();
    private final PasswordField pfPassword = new PasswordField();
    private final PasswordField pfConfirm = new PasswordField();
    private final ToggleGroup tgGender = new ToggleGroup();
    private final RadioButton rbMale = new RadioButton("Nam");
    private final RadioButton rbFemale = new RadioButton("Nữ");
    private final RadioButton rbOther = new RadioButton("Khác");
    private final DatePicker dpDob = new DatePicker();
    private final ComboBox<String> cbMajor = new ComboBox<>();
    private final CheckBox chkSport = new CheckBox("Thể thao");
    private final CheckBox chkMusic = new CheckBox("Âm nhạc");
    private final CheckBox chkGame = new CheckBox("Game");
    private final CheckBox chkReading = new CheckBox("Đọc sách");
    private final CheckBox chkAgree = new CheckBox("Tôi đồng ý với điều khoản sử dụng");
    private final ProgressBar pbStrength = new ProgressBar(0);
    private final Label lblStrength = new Label();
    private final Button btnRegister = new Button("Đăng ký");
    private final Button btnReset = new Button("Làm lại");

    private final Label errFullName = err(), errEmail = err(), errUsername = err(), errPassword = err(),
                        errConfirm = err(), errGender = err(), errDob = err(), errMajor = err();

    @Override
    public void start(Stage stage) {
        setupControls();
        registerEvents();
        registerView = buildRegisterView();
        scene = new Scene(registerView, 580, 660);
        stage.setScene(scene);
        stage.setTitle("B3 - Đăng ký & Đăng nhập");
        stage.show();
    }

    // TODO 2: Cấu hình control
    private void setupControls() {
        tfFullName.setPromptText("Nguyễn Văn An");
        tfEmail.setPromptText("abc@example.com");
        tfUsername.setPromptText("3–20 ký tự: a-z, 0-9, _");
        pfPassword.setPromptText("≥ 8 ký tự, có hoa, thường, số, ký tự đặc biệt");
        pfConfirm.setPromptText("Nhập lại mật khẩu");

        rbMale.setToggleGroup(tgGender);
        rbFemale.setToggleGroup(tgGender);
        rbOther.setToggleGroup(tgGender);

        dpDob.setPromptText("Chọn ngày sinh");
        dpDob.setEditable(false);
        dpDob.setMaxWidth(Double.MAX_VALUE);

        cbMajor.getItems().addAll("Kỹ thuật phần mềm", "Trí tuệ nhân tạo", "An toàn thông tin", "Thiết kế đồ họa");
        cbMajor.setPromptText("-- Chọn ngành --");
        cbMajor.setMaxWidth(Double.MAX_VALUE);

        pbStrength.setPrefWidth(140);

        btnRegister.disableProperty().bind(chkAgree.selectedProperty().not());
        btnRegister.setDefaultButton(true);
    }

    private Parent buildRegisterView() { return new StackPane(); }
    private void registerEvents() {}
    private static Label err() {
        Label l = new Label();
        l.setStyle("-fx-text-fill: #d93025; -fx-font-size: 11;");
        l.setWrapText(true);
        return l;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
