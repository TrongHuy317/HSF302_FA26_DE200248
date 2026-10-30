package com.fu.ch08.b2;

import com.fu.ch08.common.AlertUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginLayoutApp extends Application {

    private static final Map<String, String> ACCOUNTS = Map.of(
            "admin", "Admin@123",
            "student", "Student@123");

    private static final Border DEBUG_BORDER = new Border(new BorderStroke(
            Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, new BorderWidths(1)));

    private Scene scene;
    private String rememberedUser;                              // "Ghi nhớ đăng nhập"
    private final List<Region> containers = new ArrayList<>();  // để bật/tắt viền debug

    @Override
    public void start(Stage stage) {
        scene = new Scene(buildLoginView(), 680, 480);
        stage.setScene(scene);
        stage.setTitle("B2 - Layout & Form đăng nhập");
        stage.setMinWidth(480);
        stage.setMinHeight(400);
        stage.show();
    }

    // TODO 1: Khung chính — BorderPane 3 vùng top / center / bottom
    private Parent buildLoginView() {
        containers.clear();
        BorderPane root = new BorderPane();
        root.setTop(buildHeader());
        root.setCenter(buildCenter());
        root.setBottom(buildFooter());
        return track(root);
    }

    private Node buildHeader() {
        return new Region();
    }

    private Node buildCenter() {
        return new Region();
    }

    private Node buildFooter() {
        return new Region();
    }

    private Parent buildHomeView(String user) {
        return new Region();
    }

    private <T extends Region> T track(T region) {
        containers.add(region);
        return region;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
