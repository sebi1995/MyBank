import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bank implements Serializable {

    private List<Account> accounts;
    private long totalBankMoney;
    public static boolean moneyChange;
    private final ArrayList<Integer> acceptableNotes;
    public final long serialVersionUID = 4157938097088885077L;

    Bank() {
        this.accounts = new ArrayList<>();
        totalBankMoney = 0L;

        Main.bankObjectCreator.setObject(this);

        this.acceptableNotes = new ArrayList<>();
        this.acceptableNotes.add(50);
        this.acceptableNotes.add(20);
        this.acceptableNotes.add(10);
        this.acceptableNotes.add(5);
    }

    public boolean newAccount(String accountName) {
        if (!accountExists(accountName)) {
            synchronized (this.accounts) {
                this.accounts.add(new Account(accountName));
                this.accounts.notify();
            }
            return true;
        }
        return false;
    }
    public boolean deleteAccount(String accountName) {
        if (this.accounts != null && this.accounts.size() > 0) {
            for (Account account : this.accounts) {
                if (account.getUserName().equals(accountName)) {
                    synchronized (this.accounts) {
                        this.totalBankMoney -= account.getBalance();
                        this.accounts.remove(account);
                        this.accounts.notify();
                    }
                    return true;
                }
            }
        }
        return false;
    }
    public boolean addMoney(Account accountObject, double money) {
        if (money > 0) {
            synchronized (this.accounts) {
                accountObject.setBalance(accountObject.getBalance() + money);
                moneyChange = true;
                this.totalBankMoney += money;
                this.accounts.notify();
            }
            return true;
        }
        return false;
    }
    public boolean removeMoney(Account accountObject, int money) {
        if (money > 0) {
            double balance = accountObject.getBalance();
            if (balance == 0) {
                return false;
            } else if (balance - money < 0) {
                return false;
            } else {
                synchronized (this.accounts) {
                    accountObject.setBalance(balance - money);
                    moneyChange = true;
                    this.totalBankMoney -= money;
                    this.accounts.notify();
                }
                return true;
            }
        } else return false;
    }
    public boolean newInnerAccount(Account accountObject, String accountName) {
        return accountObject.newInnerAccount(accountName);
    }
    public boolean deleteInnerAccount(Account accountObject, String accountName) {
        this.totalBankMoney -= accountObject.getInnerAccount(accountName).getBalance();
        return accountObject.deleteInnerAccount(accountName);
    }
    private boolean isValidMoneyInput(int number, int position, ArrayList<Integer> integers) {
        if (number % integers.get(position) != 0 && position != 3) {
            return isValidMoneyInput(number, ++position, integers);
        }
        return number % integers.get(position) == 0;
/*
        if (isValidMoneyInput(number, 0, this.acceptableNotes)) {


        }*/
    }
    private boolean accountExists(String accountName) {
        if (this.accounts != null && this.accounts.size() > 0) {
            for (Account account : accounts) {
                if (account.getUserName().equals(accountName)) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean innerAccountExists(Account accountObject, String accountName){
        return accountObject.getInnerAccount(accountName) != null;
    }
    public double getAccountBalance(Account accountObject) {
        return accountObject.getBalance();
    }
    public double getTotalAccountBalance(Account accountObject) {
        return accountObject.getTotalAccountBalance();
    }
    public List<Account> getAccounts() {
        return this.accounts;
    }
    public Account getAccount(String accountName) {
        if (this.accounts != null && this.accounts.size() > 0) {
            for (Account account : this.accounts) {
                if (account.getUserName().equals(accountName)) {
                    return account;
                }
            }
        }
        return null;
    }
    public Account getAccountInnerAccount(Account accountObject, String accountName) {
        return accountObject.getInnerAccount(accountName);
    }
    public String getAccountInnerAccountsString(Account accountObject) {
        return accountObject.getInnerAccountsToString();
    }
    public String getAccountName(Account accountObject) {
        return accountObject.getAccountName();
    }
    public String getAccountUserName(Account accountObject) {
        return accountObject.getUserName();
    }
    @Override
    public String toString() {

        if (this.accounts != null && this.accounts.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Total money: £").append(totalBankMoney).append("\n");

            for (Account account : this.accounts) {
                stringBuilder.append("Name: ").append(account.getUserName()).append("\n");
            }

            return stringBuilder.toString();
        }
        return "Total money: £" + totalBankMoney + "\nNo accounts.\n";
    }
}
