package tiredcoder.com.match;

public class Bookingclass {
    String turfname,bookingdate,slot,paymentstatus,amount;
    String time,name,number;
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

    public Bookingclass(String turfname, String bookingdate, String slot, String paymentstatus, String amount, String time, String name, String number) {
        this.turfname = turfname;
        this.bookingdate = bookingdate;
        this.slot = slot;
        this.paymentstatus = paymentstatus;
        this.amount = amount;
        this.time = time;
        this.name = name;
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    Bookingclass()
    {

    }
}
