
package view;

import dao.WandDAO;
import dao.WizardDAO;
import model.Wand;
import model.Wizard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author elenagoncarova
 */

public class RecordSaleDialog extends JDialog {
    private final WandDAO wandDAO;
    private final WizardDAO wizardDAO;
    private JComboBox<Integer> wandCombo;
    private JComboBox<Integer> wizardCombo;

    public RecordSaleDialog(JFrame parent, WandDAO wandDAO, WizardDAO wizardDAO) {
        super(parent, "Оформить покупку", true);
        this.wandDAO = wandDAO;
        this.wizardDAO = wizardDAO;
        setSize(400, 200);
        setLocationRelativeTo(parent);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        formPanel.add(new JLabel("Выберите палочку:"));
        wandCombo = new JComboBox<>();
        boolean loadWandsRes = loadAvailableWands();
        if (!loadWandsRes) {
            this.dispose();
            return;
        }
        formPanel.add(wandCombo);

        formPanel.add(new JLabel("Выберите волшебника:"));
        wizardCombo = new JComboBox<>();
        boolean loadWizardRes = loadRegisteredWizards();
        if (!loadWizardRes) {
            this.dispose();
            return;
        }
        formPanel.add(wizardCombo);

        contentPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton recordButton = new JButton("Оформить");
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordSale();
            }
        });
        buttonPanel.add(recordButton);

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(cancelButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel);
        this.setVisible(true);
    }

    private boolean loadAvailableWands() {
        try {
            List<Wand> availableWands = wandDAO.getAllWands().stream()
                .filter(wand -> !wand.isSold())
                .toList();

            if (availableWands.isEmpty()) {
                JOptionPane.showMessageDialog(this.getParent(),
                    "Доступных палочек не найдено",
                    "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }

            availableWands.forEach(wand -> wandCombo.addItem(wand.getId()));
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.getParent(),
                "Не удалось загрузить палочки: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean loadRegisteredWizards() {
        try {
            List<Wizard> wizards = wizardDAO.getAll();

            if (wizards.isEmpty()) {
                JOptionPane.showMessageDialog(this.getParent(),
                    "Зарегистрированных волшебников не найдено",
                    "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }

            wizards.forEach(wizard -> wizardCombo.addItem(wizard.getId()));
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this.getParent(),
                "Не удалось загрузить волшебника: " + ex.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void recordSale() {
        try {
            int wandId = (Integer) wandCombo.getSelectedItem();
            int wizardId = (Integer) wizardCombo.getSelectedItem();

            Wand wand = wandDAO.getWandById(wandId);
            if (wand == null) {
                throw new SQLException("Палочка не найдена");
            }

            wand.setSold(true);
            wandDAO.updateWand(wand);

            JOptionPane.showMessageDialog(this,
                "Продажа зарегистрирована успешно!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Ошибка записи продажи: " + ex.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}    

