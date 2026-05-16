package rescue;

// מחלקת בן של Human

// מחלקת מוקדן
public class Dispatcher extends Human implements Comparable<Dispatcher>{
    // מספר שנות הניסיון
    private int experience;
    // המשקה האהוב על המוקדן
    private DrinkPreference drink;

    // בנאי רגיל
    public Dispatcher(int id, boolean busy, rescue.ClearanceLevel clearance, String name, double workHours,
                      double salary, int experience, rescue.DrinkPreference drink) {
        super(id, busy, clearance, name, workHours, salary);
        this.experience = experience;
        this.drink = drink;
    }

    // בנאי מאתחל
    public Dispatcher(String name, int experience, DrinkPreference drink) {
        super(name);
        this.experience = experience;
        this.drink = drink;
    }

    //  מתודות get
    public int getExperience() {
        return experience;
    }
    public DrinkPreference getDrink() {
        return drink;
    }

    //  מתודות set
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public void setDrink(DrinkPreference drink) {
        this.drink = drink;
    }

    // toString
    @Override
    public String toString() {
        return "Dispatcher{" +
                "experience=" + experience +
                ", drink=" + drink +
                ", id=" + id +
                ", busy=" + busy +
                ", clearance=" + clearance +
                '}';
    }

    // בודקת אם השכר החדש גדול או שווה למינימום
    @Override
    public void setSalary(double salary){
        if (salary < NationalRescueAuthority.minSalaryDispatcher){
            throw new IllegalArgumentException("Unacceptably low salary!");
        } super.setSalary(salary);
    }

    // בודקת את מספר השעות עבודה אם לא עובר את המקסימום המותר
    @Override
    public boolean canWorkHours(double hours){
        if ((hours + this.getWorkHours()) <= NationalRescueAuthority.dispatcherMaxWork){
            return true;
        } return false;
    }

    // משווה בים שני אובייקטים ומשווה אותם לפי מספר שנות הניסיון
    public int compareTo(Dispatcher other){
        return Integer.compare(this.experience, other.experience);
    }

}
