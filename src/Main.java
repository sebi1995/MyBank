import java.io.Serializable;
import java.util.Scanner;

public class Main {

    protected static ObjectCreator<Bank> bankObjectCreator;

    public static void main(String[] args) throws Exception {
        bankObjectCreator = new ObjectCreator<>("BankClass.txt");

        Bank bank = null;
        boolean bankFileExists = bankObjectCreator.doesFileExist();
        if (bankFileExists) {
            if ((bank = bankObjectCreator.getObjectFile()) == null) {
                bank = new Bank();
            }
        } else {
            bank = new Bank();
        }

        Scanner scanner = new Scanner(System.in);
        AccountWatcher bankWatcher = new AccountWatcher(bank.getAccounts());
        bankObjectCreator.setObject(bank);
        bankWatcher.start();


        String input;
        while (true) {
            System.out.println("What do you want to do?");
            input = scanner.next();
            switch (input) {
                case "new":
                    newUser(scanner, input, bank);
                    break;
                case "get":
                    getUser();
                    break;
                case "remove":
                    System.out.println("Enter account holder name");
                    input = scanner.next();
                    if (bank.deleteAccount(input)) {
                        System.out.println("Account: " + input + ", successfully deleted!");
                    } else {
                        System.out.println("No more accounts to delete.");
                    }
                    break;
                case "getall":
                    System.out.println(bank.toString());
                    break;
            }
        }
    }

    private static void newUser(Scanner scanner, String input, Bank bank) {
        System.out.println("Enter account holder name");
        input = scanner.next();
        bank.newAccount(input);
    }

    public static void getUser(Scanner scanner, String input, Bank bank) {
        System.out.println("Enter account holder name");
        input = scanner.next();
        Account account = bank.getAccount(input);
        if (account != null) {
            while (true) {
                input = scanner.next();
                System.out.println("Hello " + account.getName() + "!");
                System.out.println("What do you want to do?");
                switch (input) {
                    case "addmoney":
                        account.addMoney();
                        break;
                    case "takemoney":
                        account.removeMoney();
                        break;
                    case "back":
                        return;
                    default:
                        System.out.println("Invalid input, try again");
                        System.out.println("Type \"addmoney\" and then the amount, ex. addmoney 50, to add 50 to your account");
                        System.out.println("Type \"takemoney\" and then the amount, ex. takemoney 50, to remove 50 from your account");
                        break;
                }

            }
        } else {
            System.out.println("Account: " + input + ", doesn't exist.");
        }
    }
}
