
package model;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author elenagoncarova
 */
public class Delivery {
    private int id;
    private LocalDate deliveryDate;
    private List<Component> components;

    public Delivery(LocalDate deliveryDate, List<Component> components) {
        this.deliveryDate = deliveryDate;
        this.components = components;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }  
    
    @Override
    public String toString() {
        return "Delivery{" +
                "id= " + id +
                ", deliveryDate= " + deliveryDate + 
                ", components= " + components + '\'' +
                '}';
    }   
}
