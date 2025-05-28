package com.example.demo5.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControlPanelView extends VBox {

    private final TextField kuzeyAracSayisi = new TextField("5");
    private final TextField guneyAracSayisi = new TextField("5");
    private final TextField doguAracSayisi = new TextField("5");
    private final TextField batiAracSayisi = new TextField("5");

    private final Button baslatButon = new Button("Başlat");
    private final Button duraklatButon = new Button("Duraklat");
    private final Button durdurButon = new Button("Durdur");

    public ControlPanelView() {
        setSpacing(10);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-radius: 5px;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Kuzey Araç Sayısı:"), 0, 0);
        grid.add(kuzeyAracSayisi, 1, 0);

        grid.add(new Label("Güney Araç Sayısı:"), 0, 1);
        grid.add(guneyAracSayisi, 1, 1);

        grid.add(new Label("Doğu Araç Sayısı:"), 0, 2);
        grid.add(doguAracSayisi, 1, 2);

        grid.add(new Label("Batı Araç Sayısı:"), 0, 3);
        grid.add(batiAracSayisi, 1, 3);

        HBox buttonsBox = new HBox(10, baslatButon, duraklatButon, durdurButon);
        buttonsBox.setAlignment(Pos.CENTER);

        getChildren().addAll(grid, buttonsBox);
    }

    public int getKuzeyAracSayisi() {
        return Integer.parseInt(kuzeyAracSayisi.getText());
    }

    public int getGuneyAracSayisi() {
        return Integer.parseInt(guneyAracSayisi.getText());
    }

    public int getDoguAracSayisi() {
        return Integer.parseInt(doguAracSayisi.getText());
    }

    public int getBatiAracSayisi() {
        return Integer.parseInt(batiAracSayisi.getText());
    }

    public void setOnStart(Runnable handler) {
        baslatButon.setOnAction(e -> handler.run());
    }

    public void setOnPause(Runnable handler) {
        duraklatButon.setOnAction(e -> handler.run());
    }

    public void setOnStop(Runnable handler) {
        durdurButon.setOnAction(e -> handler.run());
    }
}
