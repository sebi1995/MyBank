import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bank implements Serializable {

    private List<Account> accounts;
    public final long serialVersionUID = 4157938097088885077L;

    Bank() {
        accounts = new ArrayList<>();
        Main.bankObjectCreator.setObject(this);
    }

    public void newAccount(String name) {
        synchronized (accounts) {
            accounts.add(new Account(name));
            accounts.notify();
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public boolean deleteAccount(String name) {
        if (accounts != null && accounts.size() > 0) {
            for (Account account : accounts) {
                if (account.getName().equals(name)) {
                    synchronized (accounts) {
                        accounts.remove(account);
                        accounts.notify();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public Account getAccount(String name) {
        if (accounts != null && accounts.size() > 0) {
            for (Account account : accounts) {
                if (account.getName().equals(name)) {
                    return account;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (accounts != null && accounts.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Account account : accounts) {
                stringBuilder.append("Name: ").append(account.getName()).append("\n");
            }
            return stringBuilder.toString();
        }
        return "No accounts.";
    }
}
