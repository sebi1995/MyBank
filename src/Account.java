import java.io.Serializable;

public class Account implements Serializable{

    private String name;
    private double balance;
    public final long serialVersionUID = 4157938097088885077L;

    Account(String name) {
        this.name = name;
        this.balance = 0;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name: " + name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
