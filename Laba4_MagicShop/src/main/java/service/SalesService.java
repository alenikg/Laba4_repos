
package service;

import dao.WandDAO;
import dao.WizardDAO;
import java.sql.Connection;
import model.Wand;
import model.Wizard;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author elenagoncarova
 */
public class SalesService {
    private final WandDAO wandDAO;
    private final WizardDAO wizardDAO;

    public SalesService(WandDAO wandDAO, WizardDAO wizardDAO) {
        this.wandDAO = wandDAO;
        this.wizardDAO = wizardDAO;
    }

    public void sellWand(int wandId, int wizardId) throws SQLException {
            Wand wand = wandDAO.getWandById(wandId);
            Wizard wizard = wizardDAO.getById(wizardId);

            if (wand == null) {
                throw new IllegalArgumentException("Палочка с ID " + wandId + " не найдена");
            }
            if (wizard == null) {
                throw new IllegalArgumentException("Волшебник с ID " + wizardId + " не найден");
            }
            if (wand.isSold()) {
                throw new IllegalStateException("Палочка уже продана");
            }

            wand.setSold(true);
            wandDAO.updateWand(wand);
            
            wizardDAO.deleteWizard(wizardId);
    }

    public Map<String, Object> getSalesStats(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Wand> allWands = wandDAO.getAllWands();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSales", allWands.stream()
            .filter(Wand::isSold)
            .count());
        
        stats.put("totalRevenue", allWands.stream()
            .filter(Wand::isSold)
            .mapToDouble(Wand::getPrice)
            .sum());
        
        stats.put("avgPrice", allWands.stream()
            .filter(Wand::isSold)
            .mapToDouble(Wand::getPrice)
            .average()
            .orElse(0.0));
        
        return stats;
    }
}
