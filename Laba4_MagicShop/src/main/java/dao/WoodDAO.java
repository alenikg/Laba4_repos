
package dao;

import model.Wood;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elenagoncarova
 */
public class WoodDAO {
    private final Connection connection;

    public WoodDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(Wood wood) throws SQLException {
        String sql = "INSERT INTO woods (type) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, wood.getType());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    wood.setId(rs.getInt(1));
                }
            }
        }
    }

    public Wood getById(int id) throws SQLException {
        String sql = "SELECT * FROM woods WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? new Wood(rs.getInt("id"), rs.getString("type")) : null;
        }
    }

    public List<Wood> getAll() throws SQLException {
        List<Wood> woods = new ArrayList<>();
        String sql = "SELECT * FROM woods";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                woods.add(new Wood(rs.getInt("id"), rs.getString("type")));
            }
        }
        return woods;
    }

    public void update(Wood wood) throws SQLException {
        String sql = "UPDATE woods SET type = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, wood.getType());
            stmt.setInt(2, wood.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM woods WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }    
}
