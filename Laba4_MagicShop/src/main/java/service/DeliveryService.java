
package service;

import dao.DeliveryDAO;
import dao.WoodDAO;
import dao.CoreDAO;
import model.*;
import java.time.LocalDate;
import java.time.Month;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 *
 * @author elenagoncarova
 */
public class DeliveryService {
    private final DeliveryDAO deliveryDAO;
    private final WoodDAO woodDAO;
    private final CoreDAO coreDAO;

    public DeliveryService(DeliveryDAO deliveryDAO, WoodDAO woodDAO, CoreDAO coreDAO) {
        this.deliveryDAO = deliveryDAO;
        this.woodDAO = woodDAO;
        this.coreDAO = coreDAO;
    }

    public Delivery registerDelivery(List<Component> components) throws SQLException {
        if (components == null || components.isEmpty()) {
            throw new IllegalArgumentException("Поставка не может быть пустой");
        }

        LocalDate deliveryDate = LocalDate.now();
        Delivery delivery = new Delivery(deliveryDate, new ArrayList<>());
        deliveryDAO.add(delivery);

        boolean isSummer = isSummerDelivery(deliveryDate);
        
        for (Component component : components) {
            if (component instanceof Wood && isSummer) {
                addComponentWithCheck(delivery, component);
                addComponentWithCheck(delivery, component);
            } else {
                addComponentWithCheck(delivery, component);
            }
        }

        return delivery;
    }

    private void addComponentWithCheck(Delivery delivery, Component component) throws SQLException {
        boolean alreadyExists = deliveryDAO.getComponents(delivery.getId()).stream()
            .anyMatch(c -> c.getType().equals(component.getType()));
        
        if (!alreadyExists) {
            deliveryDAO.addComponent(delivery.getId(), component);
           
            if (component instanceof Wood) {
                woodDAO.add((Wood) component);
            } else {
                coreDAO.add((Core) component);
            }
        }
    }

    private boolean isSummerDelivery(LocalDate date) {
        return date.getMonth().getValue() >= Month.JUNE.getValue() && 
               date.getMonth().getValue() <= Month.AUGUST.getValue();
    }

    public Map<String, List<Delivery>> getDeliveriesByMonth(LocalDate start, LocalDate end) throws SQLException {
        return deliveryDAO.getAll().stream()
            .filter(d -> !d.getDeliveryDate().isBefore(start) && 
                        !d.getDeliveryDate().isAfter(end))
            .collect(Collectors.groupingBy(
                d -> d.getDeliveryDate().getMonth().toString()
            ));
    }

    public String generateDeliveryReport() throws SQLException {
        StringBuilder report = new StringBuilder();
        report.append("=== Отчет по поставкам ===\n");
        
        deliveryDAO.getAll().forEach(d -> {
            report.append("Поставка #").append(d.getId())
                 .append(" от ").append(d.getDeliveryDate())
                 .append("\nКомпоненты:\n");
            
            try {
                deliveryDAO.getComponents(d.getId()).forEach(c -> 
                    report.append("- ").append(c.getType()).append("\n"));
            } catch (SQLException e) {
                report.append("Ошибка загрузки компонентов\n");
            }
            
            report.append("\n");
        });
        
        return report.toString();
    }    
}
