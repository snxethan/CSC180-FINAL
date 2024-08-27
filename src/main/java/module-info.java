module csc180.townsend.ethan.finalcsc180 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.jsoup;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;

    opens csc180.townsend.ethan.finalcsc180.Controller to javafx.fxml;
    exports csc180.townsend.ethan.finalcsc180;
}