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

        run(scanner, bank);
    }


    private static void run(Scanner scanner, Bank bank) {


        String input;
        System.out.println("What do you want to do?");
        boolean programRunning = true;

        while (programRunning) {

            System.out.print("bank > ");
            input = scanner.next();

            switch (input) {

                case "new":
                    createAccount(scanner, bank);
                    break;

                case "get":
                    accessAccount(scanner, bank);
                    break;

                case "remove":
                    deleteAccount(scanner, bank);
                    break;

                case "getall":
                    System.out.println("\n" + bank.toString());
                    break;

                case "exit":
                    System.exit(0);

                default:
                    System.out.println("ERROR: Invalid input!");
                    System.out.println("Enter \"new\" followed by a name, ex. new sebi to create a new user called sebi.");
                    System.out.println("Enter \"get\" followed by a name, ex. get sebi to get the account for the name sebi.");
                    System.out.println("Enter \"remove\" followed by a name, ex. remove sebi to delete the account with the name sebi.");
                    System.out.println("Enter \"getall\" to see all the details regarding the bank.");
                    System.out.println("Enter \"exit\" to close the program..");
                    break;
            }
        }
    }

    private static void createAccount(Scanner scanner, Bank bank) {
        System.out.print("Account holder name: ");
        String name = scanner.next();
        if (bank.newAccount(name)){
            System.out.println("\nAccount " + name + " has been created.\n");
        } else {
            System.out.println("\nError, account already exists, try a different name.\n");
        }
    }
    private static void accessAccount(Scanner scanner, Bank bank) {

        System.out.print("Enter account holder name: ");
        String input = scanner.next();
        System.out.println();

        Account account = bank.getAccount(input);

        if (account != null) {

            System.out.println("Hello " + account.getName() + "!");
            System.out.println("What do you want to do?");
            System.out.println();

            Integer money;
            String accountName = account.getName();

            while (true) {

                System.out.print("bank > account > " + accountName + " > ");
                input = scanner.next();

                switch (input) {

                    case "getbalance":
                        System.out.println();
                        System.out.println("Your total account balance is: £" + account.getBalance());
                        System.out.println();

                        break;

                    case "addmoney":
                        while (true) {
                            System.out.print("Amount to add: ");
                            money = scanner.nextInt();
                            System.out.println();

                            if (bank.addMoney(account, money)) {

                                System.out.println("Money: £" + money + " successfully added.");
                                System.out.println("Your new total balance is: £" + account.getBalance());
                                System.out.println();

                                break;

                            } else {
                                System.out.println("Invalid input, do you want to try again?");
                                System.out.println("Type \"no\" for no, or anything else to try again");
                                System.out.print("bank > account > " + accountName + " > try again? > ");

                                String cont = scanner.next();

                                System.out.println();

                                if (cont.equals("no".toUpperCase()) || cont.equals("no")) {
                                    break;
                                }
                            }
                        }
                        break;

                    case "takemoney":
                        System.out.print("Amount to remove: ");
                        money = scanner.nextInt();
                        System.out.println();

                        if (bank.removeMoney(account, money)) {

                            System.out.println("You have removed: £" + money + ".");
                            System.out.println("Your new balance is: £" + account.getBalance());

                        } else {

                            System.out.println("Invalid amount, something went wrong.");
                            System.out.println("Type \"getbalance\" to see how much money you have!");

                        }
                        System.out.println();
                        break;

                    case "back":
                        return;

                    default:
                        System.out.println();
                        System.out.println("Invalid input, try again");

                        System.out.println("Type \"getbalance\" to see your total account balance");
                        System.out.println("Type \"addmoney\" and then the amount, ex. addmoney 50, to add 50 to your account");
                        System.out.println("Type \"takemoney\" and then the amount, ex. takemoney 50, to remove 50 from your account");
                        System.out.println("Type \"back\" to go back to the bank.");
                        System.out.println();

                        break;
                }

            }
        } else {
            System.out.println("Account: " + input + ", doesn't exist.");
        }
    }
    private static void deleteAccount(Scanner scanner, Bank bank) {

        System.out.print("Account holder name to delete: ");
        String input = scanner.next();

        if (bank.deleteAccount(input)) {
            System.out.println("\nAccount: " + input + ", successfully deleted!\n");
        } else {
            System.out.println("\nNo more accounts to delete.\n");
        }
    }
}
