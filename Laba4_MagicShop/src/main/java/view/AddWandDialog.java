package view;

import dao.CoreDAO;
import dao.WoodDAO;
import service.WandService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddWandDialog extends JDialog {
    private final WandService wandService;
    private final WoodDAO woodDAO;
    private final CoreDAO coreDAO;
    private JComboBox<Integer> woodCombo;
    private JComboBox<Integer> coreCombo;
    private JTextField priceField;

    public AddWandDialog(JFrame parent, WandService wandService, WoodDAO woodDAO, CoreDAO coreDAO) {
        super(parent, "Добавить новую палочку", true);
        this.wandService = wandService;
        this.woodDAO = woodDAO;
        this.coreDAO = coreDAO;
        setSize(400, 250);
        setLocationRelativeTo(parent);

        JPanel contentPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        contentPanel.add(new JLabel("Тип дерева:"));
        woodCombo = new JComboBox<>();
        loadWoodTypes();
        contentPanel.add(woodCombo);

        contentPanel.add(new JLabel("Тип сердцевины:"));
        coreCombo = new JComboBox<>();
        loadCoreTypes();
        contentPanel.add(coreCombo);

        contentPanel.add(new JLabel("Цена:"));
        priceField = new JTextField();
        contentPanel.add(priceField);
        
        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWand();
            }
        });
        contentPanel.add(addButton);
        
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        contentPanel.add(cancelButton);
        add(contentPanel);
    }

    private void loadWoodTypes() {
        try {
            woodDAO.getAll().forEach(wood -> 
                woodCombo.addItem(wood.getId()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Не удалось загрузить типы древесины: " + ex.getMessage(), 
                "Ошибка", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCoreTypes() {
        try {
            coreDAO.getAll().forEach(core -> 
                coreCombo.addItem(core.getId()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Не удалось загрузить типы сердцевины: " + ex.getMessage(), 
                "Ошибка", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addWand() {
        try {
            int woodId = (Integer) woodCombo.getSelectedItem();
            int coreId = (Integer) coreCombo.getSelectedItem();
            double price = Double.parseDouble(priceField.getText());
            
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Цена должна быть положительной", 
                    "Ошибка", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            wandService.createWand(woodId, coreId, price);
            JOptionPane.showMessageDialog(this, 
                "Палочка успешно добавлена!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Пожалуйста, введите действительную цену", 
                "Ошибка", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, 
                "Ошибка: " + ex.getMessage(), 
                "Ошибка", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}