module com.projet.da50.projet_da50 {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires java.sql;

    opens com.projet.da50.projet_da50.model to org.hibernate.orm.core;
    opens com.projet.da50.projet_da50 to javafx.fxml;

    exports com.projet.da50.projet_da50;
}