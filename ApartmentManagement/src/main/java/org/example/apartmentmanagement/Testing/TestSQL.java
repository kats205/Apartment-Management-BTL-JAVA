package org.example.apartmentmanagement.Testing;

import java.io.IOException;
import java.sql.SQLException;
import org.example.apartmentmanagement.DAOLayer.Connector;

public class TestSQL {
    public TestSQL() {
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        Connector connector = new Connector();
        Connector.connectDB();
    }
}