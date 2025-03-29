package org.example.apartmentmanagement.Testing;

import org.example.apartmentmanagement.DAOLayer.Connector;

import java.io.IOException;
import java.sql.SQLException;

public class Testing {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        Connector connector = new Connector();
        connector.connectDB();
    }
}
