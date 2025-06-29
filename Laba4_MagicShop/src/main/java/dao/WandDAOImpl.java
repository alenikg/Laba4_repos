package dao;

import model.Wand;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WandDAOImpl implements WandDAO {
    private final Connection connection;

    public WandDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addWand(Wand wand) throws SQLException {
        String sql = "INSERT INTO wands (wood_id, core_id, is_sold, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, wand.getWoodId());
            stmt.setInt(2, wand.getCoreId());
            stmt.setBoolean(3, wand.isSold());
            stmt.setDouble(4, wand.getPrice());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 1) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        wand.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    @Override
    public Wand getWandById(int id) throws SQLException {
        String sql = "SELECT * FROM wands WHERE id = ?";
        Wand wand = null;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    wand = new Wand(
                        rs.getInt("id"),
                        rs.getInt("wood_id"),
                        rs.getInt("core_id"),
                        rs.getBoolean("is_sold"),
                        rs.getDouble("price")
                    );
                }
            }
        }
        return wand;
    }

    @Override
    public List<Wand> getAllWands() throws SQLException {
        List<Wand> wands = new ArrayList<>();
        String sql = "SELECT * FROM wands";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                wands.add(new Wand(
                    rs.getInt("id"),
                    rs.getInt("wood_id"),
                    rs.getInt("core_id"),
                    rs.getBoolean("is_sold"),
                    rs.getDouble("price")
                ));
            }
        }
        return wands;
    }

    @Override
    public List<Wand> getUnsoldWands() throws SQLException {
        List<Wand> wands = new ArrayList<>();
        String sql = "SELECT * FROM wands WHERE is_sold = false";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                wands.add(new Wand(
                    rs.getInt("id"),
                    rs.getInt("wood_id"),
                    rs.getInt("core_id"),
                    false,
                    rs.getDouble("price")
                ));
            }
        }
        return wands;
    }

    @Override
    public void updateWand(Wand wand) throws SQLException {
        String sql = "UPDATE wands SET wood_id = ?, core_id = ?, is_sold = ?, price = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wand.getWoodId());
            stmt.setInt(2, wand.getCoreId());
            stmt.setBoolean(3, wand.isSold());
            stmt.setDouble(4, wand.getPrice());
            stmt.setInt(5, wand.getId());
            
            stmt.executeUpdate();
        }
    }

    @Override
    public void markAsSold(int wandId) throws SQLException {
        String sql = "UPDATE wands SET is_sold = true WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wandId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteWand(int id) throws SQLException {
        String sql = "DELETE FROM wands WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}