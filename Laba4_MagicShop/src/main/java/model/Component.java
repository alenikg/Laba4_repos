
package model;

/**
 *
 * @author elenagoncarova
 */
public abstract class Component {
    private int id;
    private String type;
    
    public Component() {}

    public Component(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
