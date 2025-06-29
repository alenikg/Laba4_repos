
package service;

import dao.WoodDAO;
import dao.CoreDAO;
import model.Component;
import java.sql.SQLException;
import java.util.List;
import model.Wood;
import model.Core;
import model.Component;
/**
 *
 * @author elenagoncarova
 */
public class InventoryService {
    private final WoodDAO woodDAO;
    private final CoreDAO coreDAO;

    public InventoryService(WoodDAO woodDAO, CoreDAO coreDAO) {
        this.woodDAO = woodDAO;
        this.coreDAO = coreDAO;
    }

    public boolean checkComponentsAvailability(int woodId, int coreId) throws SQLException {
        return woodDAO.getById(woodId) != null && coreDAO.getById(coreId) != null;
    }

    public List<? extends Component> getAllComponents(Class<? extends Component> type) throws SQLException {
        return type == Wood.class 
            ? woodDAO.getAll() 
            : coreDAO.getAll();
    }    
}
