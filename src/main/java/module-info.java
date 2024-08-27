module csc180.townsend.ethan.finalcsc180 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.jsoup;

    opens csc180.townsend.ethan.finalcsc180.Controller to javafx.fxml;
    exports csc180.townsend.ethan.finalcsc180;
}