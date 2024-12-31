module com.projet.da.projet_da {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires java.persistence;
    requires jbcrypt;
    requires com.sun.jna.platform;
    requires java.desktop;

    opens com.projet.da50.projet_da50.model to org.hibernate.orm.core;
    opens com.projet.da50.projet_da50.view to javafx.graphics;
    opens com.projet.da50.projet_da50.view.components to javafx.graphics;

    exports com.projet.da50.projet_da50;
    opens com.projet.da50.projet_da50.view.MyAccount to javafx.graphics;
}