package tiredcoder.com.match;

public class timeslot  {
    int selected;
    String time;
    timeslot()
    {
    }
    public timeslot(int selected, String time) {
        this.selected = selected;
        this.time = time;
    }

    public void setSelected(int selected) {

        this.selected = selected;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSelected() {

        return selected;
    }

    public String getTime() {
        return time;
    }
}
