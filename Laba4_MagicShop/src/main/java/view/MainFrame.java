package view;

import dao.*;
import model.*;
import service.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

    private JTextArea wandsArea;
    private JTextArea componentsArea;
    private JTextArea wizardsArea;
    private WandDAO wandDAO;
    private WoodDAO woodDAO;
    private CoreDAO coreDAO;
    private WizardDAO wizardDAO;
    private DeliveryDAO deliveryDAO;

    public MainFrame() {
        setTitle("Учетная система магазина волшебных палочек :3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        
        try {
            Connection conn = DBUtils.getConnection();
            this.wandDAO = new WandDAOImpl(conn);
            this.woodDAO = new WoodDAO(conn);
            this.coreDAO = new CoreDAO(conn);
            this.wizardDAO = new WizardDAO(conn);
            this.deliveryDAO = new DeliveryDAO(conn);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка подключения к базе", "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(0, 3));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JButton addWandButton = new JButton("Добавить палочку");
        JButton recordSaleButton = new JButton("Оформить покупку");
        JButton manageDeliveryButton = new JButton("Управлять поставками");

        JButton addWizardButton = new JButton("Добавить волшебника");

        buttonPanel.add(addWandButton);
        buttonPanel.add(recordSaleButton);
        buttonPanel.add(manageDeliveryButton);

        buttonPanel.add(addWizardButton);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        JButton resetButton = new JButton("Сбросить базу данных (-о-)");
        rightButtonPanel.add(resetButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(rightButtonPanel, BorderLayout.SOUTH);

        JPanel displayPanel = new JPanel(new GridLayout(1, 3, 10, 0));

        JPanel wandsPanel = new JPanel(new BorderLayout());
        wandsPanel.add(new JLabel("Палочки:"), BorderLayout.NORTH);
        wandsArea = new JTextArea();
        wandsArea.setEditable(false);
        JScrollPane wandsScrollPane = new JScrollPane(wandsArea);
        wandsPanel.add(wandsScrollPane, BorderLayout.CENTER);
        displayPanel.add(wandsPanel);

        JPanel componentsPanel = new JPanel(new BorderLayout());
        componentsPanel.add(new JLabel("Компоненты:"), BorderLayout.NORTH);
        componentsArea = new JTextArea();
        componentsArea.setEditable(false);
        JScrollPane componentsScrollPane = new JScrollPane(componentsArea);
        componentsPanel.add(componentsScrollPane, BorderLayout.CENTER);
        displayPanel.add(componentsPanel);

        JPanel wizardsPanel = new JPanel(new BorderLayout());
        wizardsPanel.add(new JLabel("Волшебники:"), BorderLayout.NORTH);
        wizardsArea = new JTextArea();
        wizardsArea.setEditable(false);
        JScrollPane wizardsScrollPane = new JScrollPane(wizardsArea);
        wizardsPanel.add(wizardsScrollPane, BorderLayout.CENTER);
        displayPanel.add(wizardsPanel);

        mainPanel.add(displayPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        addWandButton.addActionListener(e -> {
            new AddWandDialog(this, new WandService(wandDAO, woodDAO, coreDAO),
            woodDAO,
            coreDAO).setVisible(true);
            refreshAll();
        });
        
        recordSaleButton.addActionListener(e -> {
            new RecordSaleDialog(this, wandDAO, wizardDAO);
            refreshAll();
        });
        
        manageDeliveryButton.addActionListener(e -> {
            new DeliveryDialog(this, deliveryDAO, woodDAO, coreDAO).setVisible(true);
            refreshAll();
        });

        addWizardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wizardName = JOptionPane.showInputDialog(MainFrame.this, "Введите имя волшебника:");
                if (wizardName != null && !wizardName.trim().isEmpty()) {
                    Wizard wizard = new Wizard(wizardName);
                    try {
                        wizardDAO.add(wizard);
                        JOptionPane.showMessageDialog(MainFrame.this, "Волшебник успешно добавлен!");
                        MainFrame.this.refreshAll();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Ошибка добавления волшебника" + ": " + ex.getMessage(),
                                "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Вы уверены, что хотите НАВСЕГДА УДАЛИТЬ ВСЕ ДАННЫЕ?",
                        "Подтвердить сброс", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    DBUtils.resetDatabase();
                    JOptionPane.showMessageDialog(MainFrame.this, "База данных была сброшена.");
                    MainFrame.this.refreshAll();
                }
            }
        });

        refreshAll();
    }

    private void refreshAll() {
        refreshWands();
        refreshComponents();
        refreshWizards();
    }

    private void refreshWands() {
        try {
            StringBuilder sb = new StringBuilder();
            wandDAO.getAllWands().stream()
                .filter(wand -> !wand.isSold())
                .forEach(wand -> sb.append(String.format("ID: %d | Wood: %d | Core: %d | Price: %.2f%n",
                    wand.getId(), wand.getWoodId(), wand.getCoreId(), wand.getPrice())));
            wandsArea.setText(sb.toString());
        } catch (SQLException ex) {
            showError("Ошибка загрузки палочек", ex);
        }
    }

    private void refreshComponents() {
        try {
            StringBuilder sb = new StringBuilder("Woods:\n");
            woodDAO.getAll().forEach(wood -> 
                sb.append(String.format("ID: %d | Type: %s%n", wood.getId(), wood.getType())));
            
            sb.append("\nCores:\n");
            coreDAO.getAll().forEach(core -> 
                sb.append(String.format("ID: %d | Type: %s%n", core.getId(), core.getType())));
            
            componentsArea.setText(sb.toString());
        } catch (SQLException ex) {
            showError("Ошибка загрузки компонентов", ex);
        }
    }

    private void refreshWizards() {
        try {
            StringBuilder sb = new StringBuilder();
            wizardDAO.getAll().forEach(wizard -> 
                sb.append(String.format("ID: %d | Name: %s%n", 
                    wizard.getId(), wizard.getName())));
            wizardsArea.setText(sb.toString());
        } catch (SQLException ex) {
            showError("Ошибка загрузки волшебников", ex);
        }
    }

    private void showError(String message, Exception ex) {
        JOptionPane.showMessageDialog(this, 
            message + ": " + ex.getMessage(), 
            "Ошибка", 
            JOptionPane.ERROR_MESSAGE);
    }
}