package tiredcoder.com.match;

public class PostClass {
    String name,mobileno,message,booking_id;
    String date;

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    PostClass()

    {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PostClass(String name, String mobileno, String message, String date) {
        this.name = name;
        this.mobileno = mobileno;
        this.message = message;
        this.date = date;
    }
}