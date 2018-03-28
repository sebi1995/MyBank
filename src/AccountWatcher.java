import java.util.List;

public class AccountWatcher extends Thread {

    List<Account> accounts;
    int size;

    AccountWatcher(List<Account> accounts) {
        this.accounts = accounts;
        this.size = accounts.size();
    }

    @Override
    public void run() {
        while (true) {
            if (this.size < this.accounts.size()) {
                try {
                    Main.bankObjectCreator.newObjectFile();
                    ++this.size;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (this.size > this.accounts.size()) {
                try {
                    Main.bankObjectCreator.newObjectFile();
                    --this.size;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Bank.moneyChange){
                try {
                    Main.bankObjectCreator.newObjectFile();
                    Bank.moneyChange = false;
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                synchronized (this.accounts) {
                    try {
                        this.accounts.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
