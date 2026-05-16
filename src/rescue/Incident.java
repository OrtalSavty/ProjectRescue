package rescue;

import java.util.ArrayList;

// מכילה את ממשק משתתפי שטח FieldParticipants
// מכילה את מחלקת מוקדן Dispatcher

// מחלקת אירועים
public class Incident {
    // תיאור האירוע
    private String type;
    //מספר סידורי של האירוע
    private int serialNumber;
    // רמת הסיווג הנדרשת לאירוע
    private ClearanceLevel clearance;
    // מספר השעות הנדרש לטפל באירוע
    private double hours;
    // רשימת כל הפרמדיקים שהוקצאו לאירוע
    private ArrayList<Responder> contributors;
    // חלק התמיכה והניהול מחר בקרה
    private DispatcherAssignment controlRoom;
    // החלק שמתבצע בשטח
    private FieldAssignment inField;

    // בנאי
    public Incident(String type, int serialNumber, ClearanceLevel clearance, double hours,
                    int numDispatchers, int room, int numMedics, int numDrones) {
        // אתחול שדות האירוע
        this.type = type;
        this.serialNumber = serialNumber;
        this.clearance = clearance;
        this.hours = hours;
        this.contributors = new ArrayList<>();

        // יצירה מפורשת של אובייקטי הניהול בתוך הבנאי
        this.controlRoom = new DispatcherAssignment(numDispatchers, room);
        this.inField = new FieldAssignment(numMedics, numDrones);
    }

    //  מתודות get
    public String getType() {
        return type;
    }
    public int getSerialNumber() {
        return serialNumber;
    }
    public ClearanceLevel getClearance() {
        return clearance;
    }
    public double getHours() {
        return hours;
    }
    public ArrayList<Responder> getContributors() {
        return contributors;
    }
    public DispatcherAssignment getControlRoom() {
        return controlRoom;
    }
    public FieldAssignment getInField() {
        return inField;
    }

    //  מתודות set
    public void setType(String type) {
        this.type = type;
    }
    public void setSerialNumber ( int serialNumber){
        this.serialNumber = serialNumber;
    }
    public void setClearance (ClearanceLevel clearance){
        this.clearance = clearance;
    }
    public void setHours ( double hours){
        this.hours = hours;
    }
    public void setContributors (ArrayList < Responder > contributors) {
        this.contributors = contributors;
    }
    public void setControlRoom (DispatcherAssignment controlRoom){
        this.controlRoom = controlRoom;
    }
    public void setInField (FieldAssignment inField){
        this.inField = inField;
    }

    // toString
    @Override
    public String toString() {
        return "Incident{" +
                "type='" + type + '\'' +
                ", serialNumber=" + serialNumber +
                ", clearance=" + clearance +
                ", hours=" + hours +
                ", contributors=" + contributors +
                ", controlRoom=" + controlRoom +
                ", inField=" + inField +
                '}';
    }

    //מאחדת רשימות
    public void setContributors() {
        this.contributors.clear();
        // הוספת כל המשיבים מהניהול ומהשטח
        this.contributors.addAll(this.controlRoom.getDispatchers());
        for (FieldParticipants p : this.inField.getFieldParticipants()) {
            this.contributors.add((Responder) p);
        }
    }

    // ממלאת את רשימת contributors
     public void assignAll(){
        // קריאה למתודות שמבצעות את השיבוץ בפועל בניהול ובשטח
         this.controlRoom.assignAllDispatchers(this.hours, this.clearance);
         this.inField.assignAllFieldParticipants(this.hours, this.clearance);
         // מילוי רשימת ה-contributors לאחר שההקצאה הסתיימה
         this.setContributors();
     }

    // equals
    @Override
    public boolean equals(Object obj) {
        // בדיקה אם האובייקט שווה לעצמו
        if (this == obj) {
            return true;
        }
        //  בדיקה אם האובייקט השני הוא null או שייך למחלקה אחרת
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Incident other = (Incident) obj;
        //  השוואת ה-type וה-serialNumber כפי שנדרש בדרישות הפרויקט
        return this.serialNumber == other.serialNumber && this.type.equals(other.type);
    }


}