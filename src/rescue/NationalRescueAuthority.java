package rescue;

import java.util.ArrayList;

// רשות ההצלה הלאומית
public class NationalRescueAuthority {
    // מבפר השעות עבודה המקסימאלי של חובש
    public static int medicMaxWork;
    // מספר השעות עבודה המקסימאלי של מוקדן
    public static int dispatcherMaxWork;
    // שכר מינימום של מוקדן
    public static double minSalaryDispatcher;
    // שכר מינימום של חובש
    public static double minSalaryMedic;
    // שעות עבודה שרחפן יכול לעבוד ללא טעינה
    public static int droneWorkNoCharge;
    // רשימת כל המחלקת חילוץ
    public static ArrayList<Responder> responders = new ArrayList<>();
    // רשימת כל האירועים
    public static  ArrayList<Incident> incidents = new ArrayList<>();

    //  בנאי
    private NationalRescueAuthority(int medicMaxWork, int dispatcherMaxWork,
                                   double minSalaryDispatcher, double minSalaryMedic,
                                   int droneWorkNoCharge, ArrayList<Responder> responders, ArrayList<Incident> incidents) {
        this.medicMaxWork = medicMaxWork;
        this.dispatcherMaxWork = dispatcherMaxWork;
        this.minSalaryDispatcher = minSalaryDispatcher;
        this.minSalaryMedic = minSalaryMedic;
        this.droneWorkNoCharge = droneWorkNoCharge;
        this.responders = responders;
        this.incidents = incidents;
    }

    //  מתודות get
    public int getMedicMaxWork() {
        return medicMaxWork;
    }
    public int getDispatcherMaxWork() {
        return dispatcherMaxWork;
    }
    public double getMinSalaryDispatcher() {
        return minSalaryDispatcher;
    }
    public double getMinSalaryMedic() {
        return minSalaryMedic;
    }
    public int getDroneWorkNoCharge() {
        return droneWorkNoCharge;
    }
    public ArrayList<Responder> getResponders() {
        return responders;
    }
    public ArrayList<Incident> getIncidents() {
        return incidents;
    }

    //  מתודות set
    public void setMedicMaxWork(int medicMaxWork) {
        this.medicMaxWork = medicMaxWork;
    }
    public void setDispatcherMaxWork(int dispatcherMaxWork) {
        this.dispatcherMaxWork = dispatcherMaxWork;
    }
    public void setMinSalaryDispatcher(double minSalaryDispatcher) {
        this.minSalaryDispatcher = minSalaryDispatcher;
    }
    public void setMinSalaryMedic(double minSalaryMedic) {
        this.minSalaryMedic = minSalaryMedic;
    }
    public void setDroneWorkNoCharge(int droneWorkNoCharge) {
        this.droneWorkNoCharge = droneWorkNoCharge;
    }
    public void setResponders(ArrayList<Responder> responders) {
        this.responders = responders;
    }
    public void setIncidents(ArrayList<Incident> incidents) {
        this.incidents = incidents;
    }

    // toString
    @Override
    public String toString() {
        return "NationalRescueAuthority{" +
                "medicMaxWork=" + medicMaxWork +
                ", dispatcherMaxWork=" + dispatcherMaxWork +
                ", minSalaryDispatcher=" + minSalaryDispatcher +
                ", minSalaryMedic=" + minSalaryMedic +
                ", droneWorkNoCharge=" + droneWorkNoCharge +
                ", responders=" + responders +
                ", incidents=" + incidents +
                '}';
    }

    // מעדכנת את כל ה-Responders
    public static void newWeek() {
        for (Responder r : responders) {
            // כולם חוזרים להיות פנויים
            r.setBusy(false);
            if (r instanceof Human) {
                // איפוס שעות עבודה לאנשים
                ((Human) r).setZeroWorkHours();
            } else if (r instanceof Drone) {
                // טעינה מלאה לרחפנים
                ((Drone) r).setWorkNoCharge();
            }
        }
    }

    // מוסיפה לרשימה Responder ללא כפילויות
    public static boolean addResponder (Responder r){
        if (responders.contains(r) ){
            return false;
        } responders.add(r);
        return true;
    }

    // מסירה Responder מהרשימה
    public static boolean removeResponder (Responder r) {
        return responders.remove(r);
    }

    // מוסיפה Incident לרשימה ללא כפילויות
    public static boolean addIncident (Incident in){
        if (incidents.contains(in) ){
            return false;
        } incidents.add(in);
        return true;
    }

    // מסירה Incident מהרשימה
    public static boolean removeIncident (Incident in){
        return incidents.remove(in);
    }
}
