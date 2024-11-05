import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create LibraryManager instance and populate data from CSV files
        LibraryManager libraryManager = new LibraryManager();
        libraryManager.readItemsFromCSV("ITEMS.csv");
        libraryManager.readUsersFromCSV("USERS.csv");

        // Create LibraryInterface instance and start the console interface
        LibraryInterface libraryInterface = new LibraryInterface(libraryManager);
        libraryInterface.start();

        // Write current loans to CSV file when program exits
        libraryManager.writeLoansToCSV("LOANS.csv");
    }
}


