package tiredcoder.com.match;

public class Bookingclass {
    String turfname,bookingdate,slot,paymentstatus,amount;

    public String getTurfname() {
        return turfname;
    }

    public void setTurfname(String turfname) {
        this.turfname = turfname;
    }

    public String getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(String bookingdate) {
        this.bookingdate = bookingdate;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Bookingclass(String turfname, String bookingdate, String slot, String paymentstatus, String amount) {
        this.turfname = turfname;
        this.bookingdate = bookingdate;
        this.slot = slot;
        this.paymentstatus = paymentstatus;
        this.amount = amount;
    }
    Bookingclass()
    {

    }
}
