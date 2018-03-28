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

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public boolean newAccount(String name) {
        if (!accountExists(name)){
            synchronized (this.accounts) {
                this.accounts.add(new Account(name));
                this.accounts.notify();
            }
            return true;
        }
        return false;
    }

    public Account getAccount(String name) {
        if (this.accounts != null && this.accounts.size() > 0) {
            for (Account account : this.accounts) {
                if (account.getName().equals(name)) {
                    return account;
                }
            }
        }
        return null;
    }

    public boolean deleteAccount(String name) {
        if (this.accounts != null && this.accounts.size() > 0) {
            for (Account account : this.accounts) {
                if (account.getName().equals(name)) {
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

    public boolean addMoney(Account account, int money) {
        if (money > 0) {
            synchronized (this.accounts) {
                account.setBalance(account.getBalance() + money);
                moneyChange = true;
                this.totalBankMoney += money;
                this.accounts.notify();
            }
            return true;
        }
        return false;
    }

    public boolean removeMoney(Account account, int money) {
        if (money > 0) {
            double balance = account.getBalance();
            if (balance == 0) {
                return false;
            } else if (balance - money < 0) {
                return false;
            } else {
                synchronized (this.accounts) {
                    account.setBalance(balance - money);
                    moneyChange = true;
                    this.totalBankMoney -= money;
                    this.accounts.notify();
                }
                return true;
            }
        } else return false;
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
    private boolean accountExists(String name) {
        if (this.accounts != null && this.accounts.size() > 0) {
            for (Account account : accounts) {
                if (account.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {

        if (this.accounts != null && this.accounts.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Total money: £").append(totalBankMoney).append("\n");

            for (Account account : this.accounts) {
                stringBuilder.append("Name: ").append(account.getName()).append("\n");
            }

            return stringBuilder.toString();
        }
        return "Total money: £" + totalBankMoney + "\nNo accounts.\n";
    }
}
