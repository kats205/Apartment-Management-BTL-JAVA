package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.DAO.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractDAO<T, K> implements GenericDAO<T, K>{
    protected Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    @Override
    public void delete(K id) {
        String sql = "DELETE FROM " + getTableName() + "WHERE " + getIdName() + " = ?";
        try{
            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            setID(stmt,id);
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    protected abstract String getTableName();
    protected abstract String getIdName();
    protected void setID(PreparedStatement stmt, K id) throws SQLException{
        if(id instanceof String){
            stmt.setString(1, (String)id);
        }
        if(id instanceof Integer){
            stmt.setInt(1, (Integer)id);
        }
        else{
            throw new IllegalArgumentException("Unsupported id type: " + id.getClass().getName());
        }
    }
}
