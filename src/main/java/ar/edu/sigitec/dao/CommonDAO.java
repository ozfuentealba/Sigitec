package ar.edu.sigitec.dao;

import ar.edu.sigitec.db.DatabaseConnection;
import ar.edu.sigitec.model.ComboItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommonDAO {
    public List<ComboItem> list(String sql) throws SQLException {
        List<ComboItem> items = new ArrayList<>();
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                items.add(new ComboItem(rs.getInt(1), rs.getString(2)));
            }
        }
        return items;
    }
}
