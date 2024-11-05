import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Library {

    private List[Item] items;
    private List[User] users;
    private List[Loan] loans;

    public Library(List[Item] items, List[User] users, List[Loan] loans) {
        this.items = items;
        this.users = users;
        this.loans = loans;
    }

    // Read items from CSV
    private List[Item] readItemsFromCSV(String filename) {
        List[Item] itemList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    // Skip the header line
                    headerSkipped = true;
                    continue;
                }
                String[] data = line.split("\t");
                if (data.length >= 6) {
                    itemList.add(new Item(data[0], data[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    // Read users from CSV
    private List[User] readUsersFromCSV(String filename) {
        List[User] userList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    // Skip the header line
                    headerSkipped = true;
                    continue;
                }
                String[] data = line.split("\t");
                if (data.length >= 4) {
                    userList.add(new User(data[0], data[1] + " " + data[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

    // Issue a loan
    public void issueLoan(String barcode, String userId, Date issueDate, Date dueDate, int numRenews) {
        // Check if the user exists
        User user = getUserById(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        // Find the item by barcode
        Item item = getItemByBarcode(barcode);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        // Check the item type and set due date accordingly
        if (item instanceof Book) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(issueDate);
            cal.add(Calendar.WEEK_OF_YEAR, 4); // Set due date 4 weeks from issue date for books
            dueDate = cal.getTime();
        } else if (item instanceof Multimedia) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(issueDate);
            cal.add(Calendar.WEEK_OF_YEAR, 1); // Set due date 1 week from issue date for multimedia
            dueDate = cal.getTime();
        }

        // Create and add a new loan
        loans.add(new Loan(barcode, userId, issueDate, dueDate, numRenews));
        System.out.println("Loan created successfully.");
    }

    // Renew a loan
    public void renewLoan(String barcode) {
        // Find the loan by barcode
        Loan loan = getLoanByBarcode(barcode);
        if (loan == null) {
            System.out.println("Loan not found.");
            return;
        }

        // Check if the item can be renewed
        Item item = getItemByBarcode(barcode);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        // Check renewal limits
        if (item instanceof Book && loan.getNumRenews() >= 3) {
            System.out.println("Book cannot be renewed more than three times.");
            return;

        }
    }

    // Record return of an item on loan
    public void recordReturn(String barcode) {
        // Find and remove the loan by barcode
        Loan loan = getLoanByBarcode(barcode);
        if (loan == null) {
            System.out.println("Loan not found.");
            return;
        }

        loans.remove(loan);
        System.out.println("Loan returned successfully.");
    }

    // View all items currently on loan
    public void viewLoans() {
        System.out.println("Items currently on loan:");
        for (Loan loan : loans) {
            System.out.println("Barcode: " + loan.getBarcode() + ", User ID: " + loan.getUserId() + ", Due Date: " + loan.getDueDate());
        }
    }

    // Search for an item by barcode
    public Item searchItemByBarcode(String barcode) {
        for (Item item : items) {
            if (item.getBarcode().equals(barcode)) {
                return item;
            }
        }
        return null; // Item not found
    }

    // Generate a report
    public void generateReport() {
        int totalBooks = 0;
        int totalMultimedia = 0;
        int renewedMoreThanOnce = 0;

        for (Loan loan : loans) {
            Item item = getItemByBarcode(loan.getBarcode());
            if (item instanceof Book) {
                totalBooks++;
            } else if (item instanceof Multimedia) {
                totalMultimedia++;
            }

            if (loan.getNumRenews() > 1) {
                renewedMoreThanOnce++;
            }
        }

        double percentageRenewedMoreThanOnce = (double) renewedMoreThanOnce / loans.size() * 100;

        System.out.println("Library Report:");
        System.out.println("Total number of book loans: " + totalBooks);
        System.out.println("Total number of multimedia loans: " + totalMultimedia);
        System.out.printf("Percentage of loans renewed more than once: %.2f%%\n", percentageRenewedMoreThanOnce);
    }

    // Write loans to CSV file
    public void writeLoansToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Loan loan : loans) {
                writer.println(loan.getBarcode() + "," + loan.getUserId() + "," + loan.getIssueDate() + "," + loan.getDueDate() + "," + loan.getNumRenews());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper methods to find item, user, loan by barcode or ID
    private Item getItemByBarcode(String barcode) {
        for (Item item : items) {
            if (item.getBarcode().equals(barcode)) {
                return item;
            }
        }
        return null;
    }

    private User getUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    private Loan getLoanByBarcode(String barcode) {
        for (Loan loan : loans) {
            if (loan.getBarcode().equals(barcode)) {
                return loan;
            }
        }
        return null;
    }
}
