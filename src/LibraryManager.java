import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

    public class LibraryManager {
        private List<Item> items;
        private List<User> users;


        private List<Loan> loans;

        public LibraryManager() {
            items = new ArrayList<Item>();
            users = new ArrayList<User>();
            loans = new ArrayList<Loan>();
        }

        public void issueLoan(String barcode, String userID, Date issueDate, Date dueDate) {
            // Check if the user exists
            boolean userExists = false;
            for (User user : users) {
                if (user.getUserID().equals(userID)) {
                    userExists = true;
                    break;
                }
            }

            if (!userExists) {
                System.out.println("User does not exist.");
                return;
            }

            // Create new loan object and add it to loans list
            loans.add(new Loan(barcode, userID, issueDate, dueDate, 0));
        }

        public void renewLoan(String barcode, Date currentDate) {
            for (Loan loan : loans) {
                if (loan.getBarcode().equals(barcode)) {
                    // Check if the item type allows renewal
                    int maxRenews = loan.getItemType().equals("Book") ? 3 : 2;
                    if (loan.getNumRenews() < maxRenews) {
                        // Calculate new due date based on item type
                        Date newDueDate = new Date();
                        newDueDate.setTime(loan.getDueDate().getTime() + (loan.getItemType().equals("Book") ? 2 : 1) * 7 * 24 * 60 * 60 * 1000);

                        // Update loan information
                        loan.setDueDate(newDueDate);
                        loan.setNumRenews(loan.getNumRenews() + 1);

                        System.out.println("Loan renewed successfully.");
                    } else {
                        System.out.println("Loan cannot be renewed more than " + maxRenews + " times.");
                    }
                    return;
                }
            }
            System.out.println("Loan not found.");
        }

        public void recordReturn(String barcode) {
            for (Loan loan : loans) {
                if (loan.getBarcode().equals(barcode)) {
                    loans.remove(loan);
                    System.out.println("Loan record removed.");
                    return;
                }
            }
            System.out.println("Loan not found.");
        }

        public void viewItemsOnLoan() {
            System.out.println("Items currently on loan:");
            for (Loan loan : loans) {
                System.out.println("Barcode: " + loan.getBarcode() + ", User ID: " + loan.getUserID() + ", Due Date: " + loan.getDueDate());
            }
        }
        public void generateReport() {
            // Calculate total number of each type of loan
            int totalBooks = 0;
            int totalMultimedia = 0;
            for (Loan loan : loans) {
                if (loan.getItemType().equals("Book")) {
                    totalBooks++;
                } else {
                    totalMultimedia++;
                }
            }

            // Calculate percentage of items that have been renewed more than once
            int renewedMoreThanOnce = 0;
            for (Loan loan : loans) {
                if (loan.getNumRenews() > 1) {
                    renewedMoreThanOnce++;
                }
            }
            double percentageRenewedMoreThanOnce = (double) renewedMoreThanOnce / loans.size() * 100;

            // Display the report
            System.out.println("Library Name: " + name);
            System.out.println("Total Number of Book Loans: " + totalBooks);
            System.out.println("Total Number of Multimedia Loans: " + totalMultimedia);
            System.out.println("Percentage of Items Renewed More Than Once: " + percentageRenewedMoreThanOnce + "%");
        }
        public void writeLoansToCSV(String filename) {
            try (FileWriter writer = new FileWriter(filename)) {
                for (Loan loan : loans) {
                    writer.write(loan.getBarcode() + "," + loan.getUserID() + "," + loan.getIssueDate() + "," + loan.getDueDate() + "," + loan.getNumRenews() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void searchLoanByBarcode(String barcode) {
            for (Loan loan : loans) {
                if (loan.getBarcode().equals(barcode)) {
                    System.out.println("Loan Details:");
                    System.out.println("Barcode: " + loan.getBarcode());
                    System.out.println("User ID: " + loan.getUserID());
                    System.out.println("Issue Date: " + loan.getIssueDate());
                    System.out.println("Due Date: " + loan.getDueDate());
                    System.out.println("Number of Renews: " + loan.getNumRenews());
                    return;
                }
            }
            System.out.println("Loan with barcode " + barcode + " not found.");
        }
    }
