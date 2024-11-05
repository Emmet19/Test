import java.util.Date;

public class Loan {
    private String barcode;
    private String userId;
    private Date issueDate;
    private Date dueDate;
    private int numRenews;

    public Loan(String barcode, String userId, Date issueDate, Date dueDate, int numRenews) {
        this.barcode = barcode;
        this.userId = userId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.numRenews = numRenews;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getUserId() {
        return userId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public int getNumRenews() {
        return numRenews;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setNumRenews(int numRenews) {
        this.numRenews = numRenews;
    }
}
