package dao;

import model.Wizard;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WizardDAO {
    private final Connection connection;

    public WizardDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(Wizard wizard) throws SQLException {
        String sql = "INSERT INTO wizards (name) VALUES (?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, wizard.getName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    wizard.setId(rs.getInt(1));
                }
            }
        }
    }

    public Wizard getById(int id) throws SQLException {
        String sql = "SELECT * FROM wizards WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Wizard wizard = new Wizard();
                    wizard.setId(rs.getInt("id"));
                    wizard.setName(rs.getString("name"));
                    return wizard;
                }
            }
        }
        return null;
    }

    public List<Wizard> getAll() throws SQLException {
        List<Wizard> wizards = new ArrayList<>();
        String sql = "SELECT * FROM wizards";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Wizard wizard = new Wizard();
                wizard.setId(rs.getInt("id"));
                wizard.setName(rs.getString("name"));
                wizards.add(wizard);
            }
        }
        return wizards;
    }

    public void updateWizard(Wizard wizard) throws SQLException {
        String sql = "UPDATE wizards SET name = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, wizard.getName());
            stmt.setInt(2, wizard.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteWizard(int id) throws SQLException {
        String sql = "DELETE FROM wizards WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}