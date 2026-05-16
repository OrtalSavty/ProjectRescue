package rescue;

// מחלקת אבא של Human ו- Drone

// מחלקת חילוץ
public abstract class Responder {
    // תעודת זהות
    protected int id;
    // האם נמצא באירוע
    protected boolean busy;
    //רמת סיווג שצריך לאירוע
    protected ClearanceLevel clearance;

    // בנאי
    public Responder(int id, boolean busy, rescue.ClearanceLevel clearance) {
        this.id = id;
        this.busy = busy;
        this.clearance = clearance;
    }

    //  מתודות get
    public int getId() {
        return id;
    }
    public boolean isBusy() {
        return busy;
    }
    public ClearanceLevel getClearance() {
        return clearance;
    }

    //  מתודות set
    public void setId(int id) {
        this.id = id;
    }
    public void setBusy(boolean busy) {
        this.busy = busy;
    }
    public void setClearance(ClearanceLevel clearance) {
        this.clearance = clearance;
    }

    // toString
    @Override
    public String toString() {
        return "Responder{" +
                "id=" + id +
                ", busy=" + busy +
                ", clearance=" + clearance +
                '}';
    }

    // מתודה שבודקת עם העובד עומד בתנאים כדי לעבוד באירוע מסויים
    public boolean canWork (Incident in) {
        // נבדוק שהפרמטר שקיבלנו תקין ולא ריק
        if (in == null) {
            throw new IllegalArgumentException("Incident cannot be null");
        }
        //  האם העובד עסוק כרגע אם כן נחזיר שהוא לא יכול לעבוד
        if (this.busy) {return false;
        }
        // האם הוא יכול לעבוד את כמות השעות הנדרשת
        // משתמשים במתודה canWorkHours ובשעות של האירוע
        if (!this.canWorkHours(in.getHours())) {
            return false;
        }

        // האם רמת הסיווג שלו מספיקה
        // משווים את ה-ordinal (הערך המספרי של המיקום ב-Enum)
        // השתמשתי ב-ordinal (אינדקס של הרמות שכתבתי ב-enum. למשל: הכי נמוך זה 0) במקום לעשות 3 תנאים ארוכים
        if (this.clearance.ordinal() < in.getClearance().ordinal()) {
            return false;
        }
        // אם עברנו את כל הבדיקות נחזיר אמת שהפרמדיק יכול לעבוד באירוע זה
        return true;
    }

     // מתודה שמוסיפה את המספר השעות שהתקבל
     // מתודה ללא מימוש שכל מי שיורש ממחלקה זו יהיה חייב לממש
     public abstract void didWork(double hours) ;

    // מתודה שבודקת אם ה-Responder יכול לעבוד כמות שעות כזו
    // מתודה ללא מימוש שכל מי שיורש ממחלקה זו יהיה חייב לממש
    public abstract boolean canWorkHours(double hours);

    // עידכון שעות העבודה
    public void work(Incident in) {
        this.setBusy(true);
        this.didWork(in.getHours());
    }

    // equals שמשווה לפי תעודת זהות
    @Override
    public boolean equals(Object obj) {
        // בדיקה אם האובייקט שווה   לעצמו
        if (this == obj) {
            return true;
        }
        //  בדיקה אם האובייקט השני הוא null או שייך למחלקה אחרת
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        //  המרת האובייקט לטיפוס Responder (Casting)
        Responder other = (Responder) obj;
        //  השוואת ה-ID כפי שנדרש בדרישות הפרויקט
        return this.id == other.id;
    }
}
