package dao;

import model.Core;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoreDAO {
    private final Connection connection;

    public CoreDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(Core core) throws SQLException {
        String sql = "INSERT INTO cores (type) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, core.getType());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    core.setId(rs.getInt(1));
                }
            }
        }
    }

    public Core getById(int id) throws SQLException {
        String sql = "SELECT * FROM cores WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? new Core(rs.getInt("id"), rs.getString("type")) : null;
        }
    }

    public List<Core> getAll() throws SQLException {
        List<Core> cores = new ArrayList<>();
        String sql = "SELECT * FROM cores";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cores.add(new Core(rs.getInt("id"), rs.getString("type")));
            }
        }
        return cores;
    }

    public void update(Core core) throws SQLException {
        String sql = "UPDATE cores SET type = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, core.getType());
            stmt.setInt(2, core.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM cores WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}