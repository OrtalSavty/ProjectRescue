package rescue;

// מחלקת בן של  חילןץ Responder

// מחלקת בן אדם
public abstract class Human extends Responder{
    // שם האדם
    private String name;
    // מספר שעות העבודה שעשה בשבוע
     private double workHours;
    //משכורית חודשית
    private double salary;

    // בנאי רגיל
    public Human(int id, boolean busy, rescue.ClearanceLevel clearance, String name, double workHours, double salary)
            throws IncidentException{
        super(id, busy, clearance);
        this.name = name;
        this.workHours = 0;
        this.salary = 0;
        // אם התעודת זהות לא תקינה נזרוק שגיאה
        if (String.valueOf(id).length() != 9){
            throw new IncidentException("Invalid ID: Drone ID must be exactly 9 digits.");
        }
    }

    // בנאי מאתחל
    public Human(String name) {
        super(0, false, ClearanceLevel.LOW);
        this.name = name;
        this.workHours = 0; 
        this.salary = 0;
    }

    //  מתודות get
    public String getName() {
        return name;
    }
    public double getWorkHours() {
        return workHours;
    }
    public double getSalary() {
        return salary;
    }

    //  מתודות set
    public void setName(String name) {
        this.name = name;
    }
    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }
    public void setSalary(double salary) {
        // בדיקה שהשכר החדש אכן גבוה מהנוכחי
        if (salary > this.salary) {
            this.salary = salary;
        } else {
            throw new IllegalArgumentException("Won't accept a demotion!");
        }
    }
    // toString
    @Override
    public String toString() {
        return "Human{" +
                "name='" + name + '\'' +
                ", workHours=" + workHours +
                ", salary=" + salary +
                ", id=" + id +
                ", busy=" + busy +
                ", clearance=" + clearance +
                '}';
    }

    // מאפסת את מספר השעות השבועיות
    public void setZeroWorkHours() {
        this.workHours = 0;
    }

    // מוסיפה את המספר השעות שהתקבל
    @Override
    public void didWork(double hours) {
        this.workHours += hours;
    }

    // מתודה שמוסיפה את המספר השעות שהתקבל
    // מתודה ללא מימוש שכל מי שיורש ממחלקה זו יהיה חייב לממש
    public abstract boolean canWorkHours(double hours) ;

}
