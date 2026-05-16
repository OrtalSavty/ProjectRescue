package rescue;

// מחלקת בן של חילןץ Responder
// מממשת את ממשק משתתפי שטח FieldParticipants

// מחלקת רחפן
public class Drone extends Responder implements DroneParticipant{
    // מספר השעות שהרחפן יכול לעבוד ללא טעינה
    private double workNoCharge;
    //מספר התקלות שדווחו כל הרחפן
    private int reportedFailures;

    // בנאי
    public Drone(int id, boolean busy, rescue.ClearanceLevel clearance, double workNoCharge, int reportedFailures)
            throws IllegalArgumentException {
        super(id, busy, clearance);
        this.workNoCharge = workNoCharge;
        this.reportedFailures = reportedFailures;
        // אם התעודת זהות לא תקינה נזקום שגיאה
        if (String.valueOf(id).length() != 5){
            throw new IncidentException("Invalid ID: Drone ID must be exactly 5 digits.");
        }
    }

    //  מתודות get
    public double getWorkNoCharge() {
        return workNoCharge;
    }
    public int getReportedFailures() {
        return reportedFailures;
    }

    //  מתודות set
    public void setWorkNoCharge(double workNoCharge) {
        this.workNoCharge = workNoCharge;
    }
    public void setReportedFailures(int reportedFailures) {
        this.reportedFailures = reportedFailures;
    }

    // toString
    @Override
    public String toString() {
        return "Drone{" +
                "workNoCharge=" + workNoCharge +
                ", reportedFailures=" + reportedFailures +
                ", id=" + id +
                ", busy=" + busy +
                ", clearance=" + clearance +
                '}';
    }

    //מעדכנת את workNoCharge כל שיהיה שווה לערך droneWorkNoCharge
    public void setWorkNoCharge(){
        this.workNoCharge = NationalRescueAuthority.droneWorkNoCharge;    }

    //  מגדילה את reportedFailures ב-1
    public void addAFailure(){
        reportedFailures += 1;
    }

    @Override
    // מוריד את מספר השעות שבתקבלו ב- workNoCharge
    public void didWork(double hours){
        workNoCharge = (workNoCharge - hours);
    }

    @Override
    // בודקת אם לרחפן יש מספיר שעות עבודה זמינות
    public boolean canWorkHours(double hours){
        if (workNoCharge < hours){
            return false;
        } return true;
    }

    // מתודת הדפסה
    @Override
    public void retreatToChargingStation() {
        System.out.println("Drone (" + this.getId() + "): Returning to charging station");
    }

    // מתודת הדפסה
    @Override
    public void collectEvidence() {
        System.out.println("Drone (" + this.getId() + "): Important evidence collected!");
    }

    // מתודת הדפסה
    @Override
    public void shareLocation() {
        System.out.println("Drone (" + this.getId() + "): I'm sending my exact location now!");
    }

}
