
package service;

import dao.WandDAO;
import dao.WoodDAO;
import dao.CoreDAO;
import model.Wand;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author elenagoncarova
 */
public class WandService {
    private final WandDAO wandDAO;
    private final WoodDAO woodDAO;
    private final CoreDAO coreDAO;

    public WandService(WandDAO wandDAO, WoodDAO woodDAO, CoreDAO coreDAO) {
        this.wandDAO = wandDAO;
        this.woodDAO = woodDAO;
        this.coreDAO = coreDAO;
    }

    public void createWand(int woodId, int coreId, double price) throws SQLException, IllegalArgumentException {
        if (woodDAO.getById(woodId) == null || coreDAO.getById(coreId) == null) {
            throw new IllegalArgumentException("Неверные ID компонентов");
        }
        
        Wand wand = new Wand(0, woodId, coreId, false, price);
        wandDAO.addWand(wand);
    }


    public List<Wand> getAvailableWands() throws SQLException {
        return wandDAO.getAllWands().stream()
            .filter(w -> !w.isSold())
            .toList();
    }

    public void updateWandPrice(int wandId, double newPrice) throws SQLException {
        if (newPrice <= 0) {
            throw new IllegalArgumentException("Цена должна быть положительной");
        }
        
        Wand wand = wandDAO.getWandById(wandId);
        if (wand != null) {
            wand.setPrice(newPrice);
            wandDAO.updateWand(wand);
        }
    }
}
