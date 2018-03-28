import java.io.Serializable;

public class Account implements Serializable{

    private String name;
    public final long serialVersionUID = 4157938097088885077L;

    Account(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name: " + name;
    }
}
