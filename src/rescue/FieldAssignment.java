package rescue;
import java.util.ArrayList;

// הביצוע הפיזי בשטח
public class FieldAssignment {
    // מספר הפרמידיקים שנדרש לאירוע
    private int numMedics;
    // מספר הרחפנים שנדרש לאירוע
    private int numDrones;
    // רשימת כל המשתתפים שהוקצו לחלק השטח של האירוע
    private  ArrayList<FieldParticipants> fieldParticipants;

    //  בנאי רגיל
    public FieldAssignment(int numMedics, int numDrones, ArrayList<rescue.FieldParticipants> fieldParticipants) {
        this.numMedics = numMedics;
        this.numDrones = numDrones;
        this.fieldParticipants = new ArrayList<>();
    }


    // בנאי מאתחל
    public FieldAssignment(int numMedics, int numDrones) {
        this.numMedics = numMedics;
        this.numDrones = numDrones;
        this.fieldParticipants = new ArrayList<>();
    }

    //  מתודות get
    public int getNumMedics() {
        return numMedics;
    }
    public int getNumDrones() {
        return numDrones;
    }
    public ArrayList<FieldParticipants> getFieldParticipants() {
        return fieldParticipants;
    }

    //  מתודות set
    public void setNumMedics(int numMedics) {
        this.numMedics = numMedics;
    }
    public void setNumDrones(int numDrones) {
        this.numDrones = numDrones;
    }
    public void setFieldParticipants(ArrayList<FieldParticipants> fieldParticipants) {
        this.fieldParticipants = fieldParticipants;
    }

    // toString
    @Override
    public String toString() {
        return "FieldAssignment{" +
                "numMedics=" + numMedics +
                ", numDrones=" + numDrones +
                ", fieldParticipants=" + fieldParticipants +
                '}';
    }

    public void assignAllFieldParticipants(double hours, ClearanceLevel clearance) throws AssignFieldException {        // יצירת רשימות חדשות
        // שתי רשימות חדשות אחת לרחפנים ואחת לפרמדיקים
        ArrayList<Medic> availableMedics = new ArrayList<>();
        ArrayList<Drone> availableDrones = new ArrayList<>();
        // עוברים על כל אנשי מחלקת החילוץ
        for (Responder r : NationalRescueAuthority.responders) {
            // בדיקה: לא עסוק, סיווג מתאים ויכול לעבוד את השעות
            if (!r.isBusy() && r.getClearance().ordinal() >= clearance.ordinal() && r.canWorkHours(hours)) {
                // אם הפרמדיק עבר את הבדיקה נוסיף לרשימה
                if (r instanceof Medic) {
                    availableMedics.add((Medic) r);
                    // אם הרחפן עבר את הבדיקה נוסיף לרשימה
                } else if (r instanceof Drone) {
                    availableDrones.add((Drone) r);
                }
            }
        }
        // רשימה לכל סוג של פרמדיק
        ArrayList<Medic> paramedics = new ArrayList<>();
        ArrayList<Medic> navigators = new ArrayList<>();
        ArrayList<Medic> searchers = new ArrayList<>();
        ArrayList<Medic> trainees = new ArrayList<>();
        // מעבר על מי שפנוי והשמה שלו ברשימה הנכונה לפי ההתמחות
        for (Medic m : availableMedics) {
            // הוספת החובש לרשימה המתאימה לפי ההתמחות שלו
            if (m.getSpecialization() == Specialization.PARAMEDIC){
                paramedics.add(m);
            }
            else if (m.getSpecialization() == Specialization.NAVIGATOR){
                navigators.add(m);
            }
            else if (m.getSpecialization() == Specialization.SEARCHER) {
                searchers.add(m);
            }
            else if (m.getSpecialization() == Specialization.TRAINEE){
                trainees.add(m);
            }
        }
        // בדיקה שיש לפחות 2 פרמדיקים
        if (paramedics.size() < 2 || navigators.size() < 1) {
            throw new AssignFieldException("Not enough medics");
        }
        // הקצאת חובשי החובה
        enroll(paramedics.remove(0), hours);
        enroll(paramedics.remove(0), hours);
        enroll(navigators.remove(0), hours);
        // מילוי שאר המכסה
        int remaining = this.numMedics - 3;
        for (int i = 0; i < remaining; i++) {
            // שמירה על יחס 1:1 בין Searcher ל-Trainee עם עדיפות ל-Trainee
            if (trainees.size() > 0 && (trainees.size() >= searchers.size() || searchers.size() == 0)) {
                enroll(trainees.remove(0), hours);
            } else if (searchers.size() > 0) {
                enroll(searchers.remove(0), hours);
            } else {
                // אם נגמרו שניהם, משלימים ממי שנשאר (פרמדיקים או נווטים)
                if (paramedics.size() > 0) {
                    enroll(paramedics.remove(0), hours);
                }
                else if (navigators.size() > 0) {
                    enroll(navigators.remove(0), hours);
                }
            }
        }
        // אם אין מספיק פרמדיקים נזרוק שגיאה
        int assignedMedicsCount = 0;
        for (FieldParticipants p : fieldParticipants) {
            if (p instanceof Medic) assignedMedicsCount++;
        }
        if (assignedMedicsCount < this.numMedics) {
            throw new AssignFieldException("Not enough medics");
        }
        // בדיקה אם יש בכלל מספיק רחפנים לפני שמתחילים
        if (availableDrones.size() < numDrones) {
            throw new AssignFieldException("Not enough field forces");
        }
        // מיון לפי תקלות מהקטן לגדול
        availableDrones.sort((d1, d2) -> Integer.compare(d1.getReportedFailures(), d2.getReportedFailures()));
        // השערת חצי הרחפנים שהכי טובים
        int limit = (int) Math.ceil(availableDrones.size() / 2.0);
        ArrayList<Drone> topDrones = new ArrayList<>(availableDrones.subList(0, limit));
        // מיון לפי השעות עבודה
        topDrones.sort((d1, d2) -> Double.compare(d1.getWorkNoCharge(), d2.getWorkNoCharge()));
        //הקצאה בפועל של הכמות הנדרשת
        // בגלל המיון, הראשונים ברשימה הם אלו עם המינימום שעות שעדיין מספיק
        if (topDrones.size() < numDrones) {
            throw new AssignFieldException("Not enough field forces");
        }
        for (int i = 0; i < numDrones; i++) {
            enroll(topDrones.get(i), hours);
        }
    }

    // מתודת עזר
    private void enroll(FieldParticipants p, double hours) {
        if (p instanceof Responder) {
            Responder r = (Responder) p;
            // מעדכנים שהוא הפך לעסוק
            r.setBusy(true);
            // מעדכנים את שעות העבודה שלו
            r.didWork(hours);
        }
        //  מוסיפים אותו לרשימה הסופית של המשתתפים באירוע
        this.fieldParticipants.add(p);
    }


}
