module yang.bao.yang_bank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.persistence;
    requires org.hibernate.orm.core;
//    requires mysql.connector.java;
    requires static lombok;

    opens yang.bao.yang_bank to javafx.fxml;
    opens yang.bao.yang_bank.entity to org.hibernate.orm.core;
    opens yang.bao.yang_bank.controllers to javafx.fxml;
    exports yang.bao.yang_bank;
}