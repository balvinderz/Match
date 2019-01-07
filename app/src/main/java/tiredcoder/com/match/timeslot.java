package tiredcoder.com.match;

public class timeslot  {
    int available;
    String time;
    timeslot()
    {
    }
    public timeslot(int available, String time) {
        this.available = available;
        this.time = time;
    }

    public void setAvailable(int selected) {

        this.available = available;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAvailable() {

        return available;
    }

    public String getTime() {
        return time;
    }
}
