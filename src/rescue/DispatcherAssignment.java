package rescue;

import java.util.ArrayList;

// מה קורה בחדר הבקה (הניהול ותמיכה)
public class DispatcherAssignment {
    // מספר המוקדנים שנדרשים לאירוע
    private int numDispatchers;
    // רשימת המוקדנים שהוקצו לאירוע
    private ArrayList<Dispatcher>dispatchers;
    // מספר החדר שבו יעבדו המוקדנים
    private int room;

    // בנאי
    public DispatcherAssignment(int numDispatchers, int room) {
        this.numDispatchers = numDispatchers;
        this.room = room;
        this.dispatchers = new ArrayList<>();
    }

    //  מתודות get
    public int getNumDispatchers() {
        return numDispatchers;
    }
    public ArrayList<Dispatcher> getDispatchers() {
        return dispatchers;
    }
    public int getRoom() {
        return room;
    }

    //  מתודות set
    public void setNumDispatchers(int numDispatchers) {
        this.numDispatchers = numDispatchers;
    }
    public void setDispatchers(ArrayList<Dispatcher> dispatchers) {
        this.dispatchers = dispatchers;
    }
    public void setRoom(int room) {
        this.room = room;
    }

    // toString
    @Override
    public String toString() {
        return "DispatcherAssignment{" +
                "numDispatchers=" + numDispatchers +
                ", dispatchers=" + dispatchers +
                ", room=" + room +
                '}';
    }

    //  מקצה מוקדנים לאירוע
    public void assignAllDispatchers (double hours,  ClearanceLevel clearance){
        // יצירת רשימה של Dispatcher
        ArrayList<Dispatcher> available = new ArrayList<>();
        // מיון הרשימה לפי ניסיון
        for (Responder r : NationalRescueAuthority.responders){
            // נפריד מהרשימה רק את המוקדנים
            if (r instanceof Dispatcher){
                Dispatcher d = (Dispatcher) r;
                // בדיקה: לא עסוק, סיווג מתאים ויכול לעבוד את השעות
                if (!d.isBusy() && d.getClearance().ordinal() >= clearance.ordinal() && d.canWorkHours(hours)) {
                    available.add(d);
                }
            }
        }
        // זורק חריגה אם אין מספיק מוקדנים
        if (available.size()<this.numDispatchers){
            throw new AssignDispatcherException ("Not enough dispatcher");
        }
        //  מיון בועות: מיון לפי ניסיון
        for (int i = 0; i < available.size() - 1; i++) {
            for (int j = 0; j < available.size() - i - 1; j++) {
                // משתמשים ב-compareTo
                if (available.get(j).compareTo(available.get(j + 1)) > 0) {
                    Dispatcher temp = available.get(j);
                    available.set(j, available.get(j + 1));
                    available.set(j + 1, temp);
                }
            }
        }
        // הגדרת מצביעים
        int left = 0;
        int right = available.size()-1;
        //אם מבקשים מספר מוקדנים זוגי
        if (this.numDispatchers % 2 == 0){
            //  רצה על גודל המוקדנים שצריך חלקי 2 כי בכל הרצה אני מוסיפה 2 מוקדנים אחד מהסוף אחד מההתחלה
            for (int i=0; i<this.numDispatchers/2; i++){
                enroll(available.get(left++),hours);
                enroll(available.get(right--),hours);
            }
        }
        // אם מבקשים מספר מוקדנים אי זוגי
        else {
            for (int i=0; i<this.numDispatchers / 2; i++){
                enroll(available.get(left++),hours);
                enroll(available.get(right--),hours);
            } enroll(available.get(right), hours);
        }
    }
    // מתודת עזר כדי לרשום ולעדכן מוקדן
    private void enroll(Dispatcher d, double hours) {
        d.setBusy(true);
        d.didWork(hours);
        this.dispatchers.add(d);
    }
}
