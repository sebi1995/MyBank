import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {

    private String userName;
    private String accountName;
    private ArrayList<Account> innerAccounts;
    private double balance;
    protected final long serialVersionUID = 4157938097088885077L;

    Account(String name) {
        this.innerAccounts = new ArrayList<>();
        this.innerAccounts.add(this);
        this.userName = name;
        this.accountName = "Main";
        this.balance = 0;
    }

    private Account() {

    }

    protected boolean newInnerAccount(String accountName) {
        if (!innerAccountExists(accountName)) {
            Account account = new Account();
            account.setAccountName(accountName);
            account.setBalance(0);
            innerAccounts.add(account);
            return true;
        }
        return false;
    }

    protected String getInnerAccountsToString() {
        StringBuilder sb = new StringBuilder();
        for (Account account : innerAccounts) {
            sb.append(" - ").append(account.getAccountName()).append("\n");
        }
        return sb.toString();
    }

    protected Account getInnerAccount(String name) {
        for (Account account : innerAccounts) {
            if (account.getAccountName().equals(name)) {
                return account;
            }
        }
        return null;
    }

    protected double getInnerAccountBalance(String accountName) {
        for (Account account : innerAccounts) {
            if (account.getAccountName().equals(accountName)){
                return account.getBalance();
            }
        }
        return 0.0;
    }

    protected double getTotalAccountBalance() {
        double ret = 0L;
        for (Account account : innerAccounts) {
            ret += account.getBalance();
        }
        return ret;
    }

    protected void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    protected String getAccountName() {
        return accountName;
    }

    protected String getUserName() {
        return userName;
    }

    protected double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    private boolean innerAccountExists(String accountName) {
        for (Account account : innerAccounts) {
            if (account.getAccountName().equals(accountName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Name: " + userName;
    }

    public boolean deleteInnerAccount(String input) {
        if (innerAccountExists(input)) {
            innerAccounts.remove(getInnerAccount(input));
            return true;
        }
        return false;
    }
}
