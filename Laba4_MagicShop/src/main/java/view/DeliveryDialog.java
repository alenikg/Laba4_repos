package view;

import dao.DeliveryDAO;
import dao.WoodDAO;
import dao.CoreDAO;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class DeliveryDialog extends JDialog {
    private final DeliveryDAO deliveryDAO;
    private final WoodDAO woodDAO;
    private final CoreDAO coreDAO;
    private DefaultListModel<String> componentListModel;
    private JList<String> componentList;

    public DeliveryDialog(JFrame parent, DeliveryDAO deliveryDAO, WoodDAO woodDAO, CoreDAO coreDAO) {
        super(parent, "Управление поставками", true);
        this.deliveryDAO = deliveryDAO;
        this.woodDAO = woodDAO;
        this.coreDAO = coreDAO;
        setSize(600, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        componentListModel = new DefaultListModel<>();
        componentList = new JList<>(componentListModel);
        JScrollPane scrollPane = new JScrollPane(componentList);
        mainPanel.add(new JLabel("Доставка компонентов:"), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton addWoodButton = new JButton("Добавить древесину");
        addWoodButton.addActionListener(e -> addComponent(true));
        buttonPanel.add(addWoodButton);

        JButton addCoreButton = new JButton("Добавить сердцевину");
        addCoreButton.addActionListener(e -> addComponent(false));
        buttonPanel.add(addCoreButton);

        JButton registerButton = new JButton("Регистрация доставки");
        registerButton.addActionListener(e -> registerDelivery());
        buttonPanel.add(registerButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void addComponent(boolean isWood) {
        String type = JOptionPane.showInputDialog(this, 
            "Добавьте " + (isWood ? "древесину: " : "сердцевину: "));
        if (type != null && !type.trim().isEmpty()) {
            componentListModel.addElement((isWood ? "[Дерево] " : "[Сердцевина] ") + type);
        }
    }

    private void registerDelivery() {
        if (componentListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Пожалуйста, добавьте хотя бы один компонент",
                "Предупреждение",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Delivery delivery = new Delivery(LocalDate.now(), new ArrayList<>());
            deliveryDAO.add(delivery);

            for (int i = 0; i < componentListModel.size(); i++) {
                String item = componentListModel.get(i);
                boolean isWood = item.startsWith("[Дерево] ");
                if (isWood) {
                    String type = item.substring(9);
                    Wood wood = new Wood(0, type);
                    woodDAO.add(wood);
                    deliveryDAO.addComponent(delivery.getId(), wood);
                } else {
                    String type = item.substring(13);
                    Core core = new Core(0, type);
                    coreDAO.add(core);
                    deliveryDAO.addComponent(delivery.getId(), core);
                }
            }

            JOptionPane.showMessageDialog(this,
                "Доставка успешно зарегистрирована!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Ошибка регистрации доставки: " + ex.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}