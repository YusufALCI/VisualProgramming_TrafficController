package com.example.demo2;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Random;

public class TrafficView {

    private Group root;

    // Trafik ışıkları (dikdörtgenler)
    private Rectangle kuzeyIsik, guneyIsik, batiIsık, doguIsik;

    // Sayaçlar (kalan süre)
    private Text kuzeySayac, guneySayac, batiSayac, doguSayac;

    // Kullanıcıdan araç sayısı alınan metin kutuları
    private TextField kuzeyAracSayisi, guneyAracSayisi, batiAracSayisi, doguAracSayisi;

    // Başlat, duraklat, durdur,random araba  butonları
    private Button baslatButon, duraklatButon, durdurButon, rastgeleButon;

    public TrafficView(Group root) {
        this.root = root;
        arayuzOlustur();
    }

    private void arayuzOlustur() {
        // Yolları çiz
        yolCiz();

        // Trafik ışıklarını oluştur ve root'a ekle
        kuzeyIsik = isikOlustur(590, 220);
        guneyIsik = isikOlustur(590, 560);
        batiIsık = isikOlustur(420, 390);
        doguIsik = isikOlustur(760, 390);

        root.getChildren().addAll(kuzeyIsik, guneyIsik, batiIsık, doguIsik);



        // Sayaç metinlerini oluştur ve root'a ekle
        kuzeySayac = sayacOlustur(585, 210);  //595  210
        guneySayac = sayacOlustur(585, 605);  //595  590
        batiSayac = sayacOlustur(390, 410);  //410  395
        doguSayac = sayacOlustur(790, 410);  //790  395


        root.getChildren().addAll(kuzeySayac, guneySayac, batiSayac, doguSayac);

        // Kullanıcıdan araç sayısı almak için TextField ve label ekle
        aracSayiKontrolPanel();

        // Butonları ekle
        butonAyar();
    }

    private void yolCiz() {
        double dikeyYolX = 450;
        double yatayYolY = 250;
        double roadWidth = 300;
        double laneWidth = roadWidth / 6;

        DropShadow roadShadow = new DropShadow();
        roadShadow.setOffsetX(10);               // Yatay kayma
        roadShadow.setOffsetY(10);               // Dikey kayma
        roadShadow.setRadius(20);                // Blur yarıçapı (daha geniş)
        roadShadow.setSpread(0.3);               // Gölgede doluluk oranı (%30 daha dolu)
        roadShadow.setColor(Color.rgb(0, 0, 0, 0.7)); // Daha koyu ve daha opak


        // Dikey yol (Kuzey-Güney)
        Rectangle dikeyYol = new Rectangle(dikeyYolX, 0, roadWidth, 800);
        dikeyYol.setFill(Color.DARKSLATEGRAY);
        root.getChildren().add(dikeyYol);

        // Yatay yol (Batı-Doğu)
        Rectangle yatayYol = new Rectangle(0, yatayYolY, 1750, roadWidth);
        yatayYol.setFill(Color.DARKSLATEGRAY);
        root.getChildren().add(yatayYol);

        // Dikey şerit çizgileri
        for (int i = 1; i < 6; i++) {
            Line laneLine = new Line(dikeyYolX + i * laneWidth, 0, dikeyYolX + i * laneWidth, 1000);
            laneLine.setStroke(Color.WHITE);
            laneLine.setStrokeWidth(i == 3 ? 4 : 2); // Ortadaki çizgi kalın ve düz
            if (i != 3) laneLine.getStrokeDashArray().addAll(20.0, 20.0); // Diğerleri kesikli
            root.getChildren().add(laneLine);
        }

        // Yatay şerit çizgileri
        for (int i = 1; i < 6; i++) {
            Line laneLine = new Line(0, yatayYolY + i * laneWidth, 1750, yatayYolY + i * laneWidth);
            laneLine.setStroke(Color.WHITE);
            laneLine.setStrokeWidth(i == 3 ? 4 : 2); // Ortadaki çizgi kalın ve düz
            if (i != 3) laneLine.getStrokeDashArray().addAll(20.0, 20.0); // Diğerleri kesikli
            root.getChildren().add(laneLine);
        }
    }



    private Rectangle isikOlustur(double x, double y) {
        Rectangle light = new Rectangle(x, y, 20, 20);
        light.setArcWidth(5);
        light.setArcHeight(5);
        light.setStroke(Color.BLACK);
        light.setStrokeWidth(1);
        light.setFill(Color.RED); // Başlangıçta kırmızı
        return light;
    }

    private Text sayacOlustur(double x, double y) {
        Text text = new Text(x, y, "10");
        text.setFill(Color.LEMONCHIFFON  );
        text.setFont(Font.font("Arial",FontWeight.BOLD,22));
        return text;
    }

    private void aracSayiKontrolPanel() {
        VBox inputBox = new VBox(10);
        inputBox.setLayoutX(1250);
        inputBox.setLayoutY(30);
        inputBox.setPadding(new Insets(10));
        inputBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999; -fx-border-radius: 5;");

        Label titleLabel = new Label("🚗 Araç Giriş Paneli");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        inputBox.getChildren().add(titleLabel);

        // Kuzey ve Güney yan yana
        HBox northSouthBox = new HBox(20);

        VBox northBox = new VBox(5);
        Label northLabel = new Label("Kuzey:");
        kuzeyAracSayisi = new TextField("6");
        kuzeyAracSayisi.setPrefWidth(100);
        northBox.getChildren().addAll(northLabel, kuzeyAracSayisi);

        VBox southBox = new VBox(5);
        Label southLabel = new Label("Güney:");
        guneyAracSayisi = new TextField("6");
        guneyAracSayisi.setPrefWidth(100);
        southBox.getChildren().addAll(southLabel, guneyAracSayisi);

        northSouthBox.getChildren().addAll(northBox, southBox);

        // Doğu ve Batı yan yana
        HBox eastWestBox = new HBox(20);

        VBox eastBox = new VBox(5);
        Label eastLabel = new Label("Doğu:");
        doguAracSayisi = new TextField("6");
        doguAracSayisi.setPrefWidth(100);
        eastBox.getChildren().addAll(eastLabel, doguAracSayisi);

        VBox westBox = new VBox(5);
        Label westLabel = new Label("Batı:");
        batiAracSayisi = new TextField("6");
        batiAracSayisi.setPrefWidth(100);
        westBox.getChildren().addAll(westLabel, batiAracSayisi);

        eastWestBox.getChildren().addAll(eastBox, westBox);

        inputBox.getChildren().addAll(northSouthBox, eastWestBox);

        root.getChildren().add(inputBox);
    }

    private void butonAyar() {
        VBox buttonBox = new VBox(15);  // Butonlar arasındaki boşluk
        buttonBox.setLayoutX(1120);     // inputBox'ın sağında kalsın
        buttonBox.setLayoutY(30);

        // Butonlar
        baslatButon = new Button("Başlat");
        duraklatButon = new Button("Duraklat");
        durdurButon = new Button("Durdur");

         rastgeleButon = new Button("🎲");
        rastgeleButon.setStyle("""
        -fx-background-color: linear-gradient(to bottom, #2196F3, #1976D2);
        -fx-text-fill: white;
        -fx-font-weight: bold;
        -fx-font-size: 22px;
        -fx-padding: 10 20 10 20;
        -fx-background-radius: 8;
    """);

        // Buton stilleri
        String btnStyle = """
        -fx-background-color: linear-gradient(to bottom, #4CAF50, #388E3C);
        -fx-text-fill: white;
        -fx-font-weight: bold;
        -fx-padding: 10 20 10 20;
        -fx-background-radius: 8;
    """;

        baslatButon.setStyle(btnStyle);

        duraklatButon.setStyle("""
        -fx-background-color: linear-gradient(to bottom, #FFC107, #FFA000);
        -fx-text-fill: white;
        -fx-font-weight: bold;
        -fx-padding: 10 20 10 20;
        -fx-background-radius: 8;
    """);

        durdurButon.setStyle("""
        -fx-background-color: linear-gradient(to bottom, #F44336, #D32F2F);
        -fx-text-fill: white;
        -fx-font-weight: bold;
        -fx-padding: 10 20 10 20;
        -fx-background-radius: 8;
    """);

        // Butonları VBox içine ekle
        buttonBox.getChildren().addAll(baslatButon, duraklatButon, durdurButon, rastgeleButon);

        // VBox'ı root'a ekle
        root.getChildren().add(buttonBox);

        // Rastgele araç ata işlevi
        Random random = new Random();
        rastgeleButon.setOnAction(e -> {
            kuzeyAracSayisi.setText(String.valueOf(random.nextInt(10) + 1));
            guneyAracSayisi.setText(String.valueOf(random.nextInt(10) + 1));
            doguAracSayisi.setText(String.valueOf(random.nextInt(10) + 1));
            batiAracSayisi.setText(String.valueOf(random.nextInt(10) + 1));
        });
    }


    // Getterlar - Controller için

    public Rectangle getKuzeyIsik() {
        return kuzeyIsik;
    }

    public Rectangle getGuneyIsik() {
        return guneyIsik;
    }

    public Rectangle getBatiIsık() {
        return batiIsık;
    }

    public Rectangle getDoguIsik() {
        return doguIsik;
    }

    public Text getKuzeySayac() {
        return kuzeySayac;
    }

    public Text getGuneySayac() {
        return guneySayac;
    }

    public Text getBatiSayac() {
        return batiSayac;
    }

    public Text getDoguSayac() {
        return doguSayac;
    }

    public TextField getKuzeyAracSayisi() {
        return kuzeyAracSayisi;
    }

    public TextField getGuneyAracSayisi() {
        return guneyAracSayisi;
    }

    public TextField getBatiAracSayisi() {
        return batiAracSayisi;
    }

    public TextField getDoguAracSayisi() {
        return doguAracSayisi;
    }

    public Button getBaslatButon() {
        return baslatButon;
    }

    public Button getDuraklatButon() {
        return duraklatButon;
    }

    public Button getDurdurButon() {
        return durdurButon;
    }

    public Group getRoot() {
        return root;
    }
}
