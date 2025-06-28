
package dao;

import java.sql.SQLException;
import java.util.List;
import model.Wand;

/**
 *
 * @author elenagoncarova
 */
public interface WandDAO {
    void addWand(Wand wand) throws SQLException;
    Wand getWandById(int id) throws SQLException;
    List<Wand> getAllWands() throws SQLException;
    List<Wand> getUnsoldWands() throws SQLException;
    void updateWand(Wand wand) throws SQLException;
    void markAsSold(int wandId) throws SQLException;
    void deleteWand(int id) throws SQLException;
}
