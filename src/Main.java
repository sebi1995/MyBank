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
        if (bank.newAccount(name)) {
            System.out.println("\nAccount " + name + " has been created.\n");
        } else {
            System.out.println("\nError, account already exists, try a different name.\n");
        }
    }

    private static void accessAccount(Scanner scanner, Bank bank) {
        System.out.print("Enter account name: ");
        String input = scanner.next();
        Integer money;
        System.out.println();

        Account account = bank.getAccount(input);

        if (account != null) {

            String userName = bank.getAccountUserName(account);

            System.out.println("Hello " + userName);

            while (true) {

                System.out.println("Which account do you want to access?");
                System.out.print(bank.getAccountInnerAccountsString(account));
                System.out.println();
                System.out.print("bank > " + userName + " > ");
                input = scanner.next();

                switch (input) {
                    case "back":
                        return;

                    case "new":
                        System.out.print("Name of new account: ");
                        input = scanner.next();

                        if (bank.newInnerAccount(account, input)) {

                            System.out.println("\nAccount " + input + " successfully created!");

                        } else System.out.println("\nAccount " + input + " already exists.");

                        break;

                    case "delete":

                        System.out.print("Name of account to delete: ");
                        input = scanner.next();
                        System.out.println();

                        if (bank.innerAccountExists(account, input)) {

                            if (bank.getAccountInnerAccount(account, input).getBalance() > 0) {

                                System.out.println("You have £" + bank.getAccountBalance(bank.getAccountInnerAccount(account, input)) +
                                        " on your " + bank.getAccountName(bank.getAccountInnerAccount(account, input)) + " account.");

                                System.out.println("Please enter the name of another account to move your money to.");
                                System.out.print(bank.getAccountInnerAccountsString(account).replace(" - " + bank.getAccountName(bank.getAccountInnerAccount(account, input)) + "\n", ""));
                                System.out.println("Account name: ");

                                String moveToInstance;
                                while (true) {
                                    moveToInstance = scanner.next();

                                    if (moveToInstance.equals("back")) {
                                        break;

                                    } else if (bank.innerAccountExists(account, moveToInstance)) {

                                        bank.addMoney(bank.getAccountInnerAccount(account, moveToInstance),
                                                bank.getAccountBalance(bank.getAccountInnerAccount(account, input)));

                                        System.out.println("Deleted account " + bank.getAccountName(bank.getAccountInnerAccount(account, input)) +
                                                " and moved money to " + bank.getAccountName(bank.getAccountInnerAccount(account, moveToInstance)));

                                        bank.deleteInnerAccount(account, input);

                                        break;

                                    } else {
                                        System.out.println("Invalid account, try again or type back to cancel.");
                                    }
                                }

                            } else {
                                Account innerAccount = bank.getAccountInnerAccount(account, input);
                                if (bank.deleteInnerAccount(account, input)) {
                                    System.out.println("Account " + bank.getAccountName(innerAccount) + " has been successfully deleted!");
                                } else System.out.println("Something went wrong, try again.");
                            }

                        } else System.out.println("Account " + input + " doesn't exist.");

                        break;

                    case "balance":
                        System.out.println("\nYour total balance is: £" + bank.getTotalAccountBalance(account) + "\n");
                        break;

                    default:
                        Account temp = account;
                        if ((account = bank.getAccountInnerAccount(account, input)) != null) {

                            String accountName = bank.getAccountName(account);

                            while (true) {

                                System.out.print("bank > " + userName + " > " + accountName + " > ");
                                input = scanner.next();

                                switch (input) {
                                    case "add":
                                        System.out.print("Enter amount add: £");
                                        bank.addMoney(account, (money = scanner.nextInt()));
                                        System.out.println("\nMoney: £" + money + " successfully added to your account.");
                                        System.out.println("New balance: £" + bank.getAccountBalance(account) + "\n");
                                        break;

                                    case "remove":
                                        System.out.println("Current balance: " + bank.getAccountBalance(account));
                                        System.out.print("Enter amount to remove: £");

                                        if (bank.removeMoney(account, (money = scanner.nextInt()))) {

                                            System.out.println("\nMoney: " + money + "successfully removed from your account.");
                                            System.out.println("New balance: £ " + bank.getAccountBalance(account) + "\n");

                                        } else
                                            System.out.println("\nCan't remove £" + money + ". Not enough money in account.");

                                        break;

                                    case "balance":
                                        System.out.println("\nAccount balance: £" + bank.getAccountBalance(account) + "\n");
                                        break;
                                }
                                if (input.equals("back")) {
                                    account = temp;
                                    break;
                                }

                            }

                        } else {
                            account = temp;
                            System.out.println("\nAccount " + input + " doesn't exist, try again.");
                            System.out.println("Type back to go back.");
                            System.out.println();
                        }
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




/*

System.out.print("Enter account holder name: ");
        String input = scanner.next();
        System.out.println();

        Account account = bank.getAccount(input);

        if (account != null) {

            System.out.println("Hello " + account.getUserName() + "!");
            System.out.println("What do you want to do?");
            System.out.println();

            Integer money;
            String accountName = account.getUserName();

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

* */