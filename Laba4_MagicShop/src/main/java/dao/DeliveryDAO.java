package dao;

import model.Delivery;
import model.Component;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDAO {
    private final Connection connection;

    public DeliveryDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(Delivery delivery) throws SQLException {
        String sql = "INSERT INTO deliveries (delivery_date) VALUES (?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, delivery.getDeliveryDate().toString());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    delivery.setId(rs.getInt(1));
                }
            }
        }
    }

    public void addComponent(int deliveryId, Component component) throws SQLException {
        String componentType = (component instanceof model.Wood) ? "wood" : "core";
        String sql = "INSERT INTO delivery_components (delivery_id, component_id, component_type) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, deliveryId);
            stmt.setInt(2, component.getId());
            stmt.setString(3, componentType);
            stmt.executeUpdate();
        }
    }

    public Delivery getById(int id) throws SQLException {
        String sql = "SELECT * FROM deliveries WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Delivery delivery = new Delivery();
                    delivery.setId(rs.getInt("id"));
                    delivery.setDeliveryDate(LocalDate.parse(rs.getString("delivery_date")));
                    return delivery;
                }
            }
        }
        return null;
    }

    public List<Delivery> getAll() throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM deliveries";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Delivery delivery = new Delivery();
                delivery.setId(rs.getInt("id"));
                delivery.setDeliveryDate(LocalDate.parse(rs.getString("delivery_date")));
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }

    public List<Component> getComponents(int deliveryId) throws SQLException {
        List<Component> components = new ArrayList<>();
        String sql = "SELECT dc.component_id, dc.component_type, " +
                     "COALESCE(w.type, c.type) as type " +
                     "FROM delivery_components dc " +
                     "LEFT JOIN woods w ON dc.component_type = 'wood' AND dc.component_id = w.id " +
                     "LEFT JOIN cores c ON dc.component_type = 'core' AND dc.component_id = c.id " +
                     "WHERE dc.delivery_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, deliveryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Component component;
                    if ("wood".equals(rs.getString("component_type"))) {
                        component = new model.Wood(
                            rs.getInt("component_id"),
                            rs.getString("type")
                        );
                    } else {
                        component = new model.Core(
                            rs.getInt("component_id"),
                            rs.getString("type")
                        );
                    }
                    components.add(component);
                }
            }
        }
        return components;
    }
}