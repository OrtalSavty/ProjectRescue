package rescue;

// מחלקת בן של Human
// מממשת את ממשק משתתפי שטח FieldParticipants

// מחלקת פרמדיק\חובש
public class Medic extends Human implements MedicParticipant{

    // התמחות
    private Specialization specialization;

    // בנאי רגיל
    public Medic(int id, boolean busy, rescue.ClearanceLevel clearance, String name, double workHours, double salary, Specialization specialization) {
        super(id, busy, clearance, name, workHours, salary);
        this.specialization = specialization;
    }

    // בנאי מאתחל
    public Medic(String name, Specialization specialization) {
        super(name);
        this.specialization = specialization;
        // אתחול המינימום
        this.setSalary(NationalRescueAuthority.minSalaryMedic);
    }

    //  מתודות get
    public Specialization getSpecialization() {
        return specialization;
    }

    //  מתודות set
    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    // toString
    @Override
    public String toString() {
        return "Medic{" +
                "specialization=" + specialization +
                ", id=" + id +
                ", busy=" + busy +
                ", clearance=" + clearance +
                '}';
    }

    // בודקת אם השכר החדש גדול או שווה למינימום
    @Override
    public void setSalary(double salary){
        if (salary < NationalRescueAuthority.minSalaryMedic){
            throw new IllegalArgumentException("Unacceptably low salary!");
        } super.setSalary(salary);
    }

    // בודקת את מספר השעות עבודה אם לא עובר את המקסימום המותר
    @Override
    public boolean canWorkHours(double hours){
        if ((hours + this.getWorkHours()) <= NationalRescueAuthority.medicMaxWork){
            return true;
        } return false;
    }

    // מתודת הדפסה
    @Override
    public void retreatToAmbulance(){
        System.out.println("Medic (" + this.getName() + "): Returning to ambulance");
    }

    // מתודת הדפסה
    @Override
    public void collectEvidence() {
        System.out.println("Medic (" + this.getName() + "): Important evidence collected!");
    }
    // מתודת הדפסה
    @Override
    public void shareLocation(){
        System.out.println("Medic (" + this.getName() + "): I’m sending my exact location now!");
    }
}
