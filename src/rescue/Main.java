package rescue;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n-----Starting Project Rescue Tests-----");

        //  אתחול משתני המערכת הסטטיים של הרשות
        System.out.println("Initializing National Authority Parameters:");
        NationalRescueAuthority.medicMaxWork = 45;
        NationalRescueAuthority.dispatcherMaxWork = 40;
        NationalRescueAuthority.minSalaryDispatcher = 6000.0;
        NationalRescueAuthority.minSalaryMedic = 7000.0;
        NationalRescueAuthority.droneWorkNoCharge = 8;
        System.out.println("System parameters initialized.\n");

        //  יצירה והוספה של כוח אדם וכלים
        System.out.println("-----Creating Responders & Testing Constructors/Methods-----");
        createResponders();
        System.out.println("Total Responders ready: " + NationalRescueAuthority.responders.size() + "\n");

        //  בדיקות שוויון, השוואה ומניעת כפילויות
        System.out.println("-----Testing Equals, CompareTo & Duplications-----");
        testEqualityAndSorting();

        //  יצירת אירוע חילוץ מורכב
        System.out.println("\n-----Full Scenario: Incident Creation and Assignment-----");
        Incident bigFire = new Incident(
                "Forest Fire", 1001, ClearanceLevel.HIGH, 3.5, // סוג, מספר סידורי, סיווג, שעות
                3, 101, // 3 מוקדנים, חדר 101
                5, 2 // 5 פרמדיקים, 2 רחפנים
        );
        NationalRescueAuthority.addIncident(bigFire);
        System.out.println("Created Incident: " + bigFire.getType() + " (Hours required: " + bigFire.getHours() + ")");

        // ניסיון הקצאה (בדיקת החריגות)
        try {
            System.out.println("Attempting to assign teams to the incident:");
            bigFire.assignAll();
            System.out.println("Assignment Successful!");

            System.out.println("\n-----Assigned Team members-----");
            for(Responder r : bigFire.getContributors()) {
                System.out.println("- " + r.toString());

                if(r instanceof FieldParticipants) {
                    ((FieldParticipants) r).shareLocation();
                }
            }
        } catch (AssignDispatcherException | AssignFieldException e) {
            System.out.println("Assignment Failed: " + e.getMessage());
        }

        //  בדיקת ניהול זמן (חריגה מהשעות)
        System.out.println("\nTesting Work Limits & Week Reset");
        checkWorkLimits(bigFire);

        System.out.println("All Tests Completed Successfully");
    }

    //  מתודות עזר לבניית תרחיש הבדיקה

    private static void createResponders() {
        try {
            // יצירת מוקדנים (בדיקת הבנאים)
            Dispatcher d1 = new Dispatcher(111111111, false, ClearanceLevel.HIGH, "Shir", 0, 7000, 5, DrinkPreference.COFFEE);
            Dispatcher d2 = new Dispatcher("Ron", 2, DrinkPreference.WATER); // בנאי מקוצר
            d2.setId(222222222); d2.setClearance(ClearanceLevel.MEDIUM); d2.setSalary(6500); // שימוש ב-Setters
            Dispatcher d3 = new Dispatcher(333333333, false, ClearanceLevel.HIGH, "Yael", 0, 8000, 10, DrinkPreference.ENERGY_DRINK);
            Dispatcher d4 = new Dispatcher(444444444, false, ClearanceLevel.HIGH, "Dan", 0, 7500, 8, DrinkPreference.COFFEE);

            // יצירת פרמדיקים (כדי שהשיבוץ יעבוד צריך: 2 PARAMEDIC, 1 NAVIGATOR)
            Medic m1 = new Medic(555555555, false, ClearanceLevel.HIGH, "Ilan", 0, 9000, Specialization.PARAMEDIC);
            Medic m2 = new Medic(666666666, false, ClearanceLevel.HIGH, "Maya", 0, 9000, Specialization.PARAMEDIC);
            Medic m3 = new Medic(777777777, false, ClearanceLevel.HIGH, "Ortal", 0, 8500, Specialization.NAVIGATOR);
            Medic m4 = new Medic(888888888, false, ClearanceLevel.MEDIUM, "Avi", 0, 7500, Specialization.SEARCHER);
            Medic m5 = new Medic(999999999, false, ClearanceLevel.HIGH, "Gal", 0, 7000, Specialization.TRAINEE);
            Medic m6 = new Medic(123456789, false, ClearanceLevel.HIGH, "Dana", 0, 7000, Specialization.TRAINEE);

            // יצירת רחפנים
            Drone dr1 = new Drone(10001, false, ClearanceLevel.HIGH, 8.0, 0);
            Drone dr2 = new Drone(10002, false, ClearanceLevel.HIGH, 7.5, 2);
            Drone dr3 = new Drone(10003, false, ClearanceLevel.HIGH, 8.0, 1);

            // הוספה לרשות
            Responder[] allResponders = {d1, d2, d3, d4, m1, m2, m3, m4, m5, m6, dr1, dr2, dr3};
            for(Responder r : allResponders) {
                NationalRescueAuthority.addResponder(r);
            }

            // טסט לחריגת שכר מוקדן
            try {
                System.out.println("Testing invalid salary decrease for Medic:");
                m1.setSalary(2000); // אמור לזרוק חריגה
            } catch (IllegalArgumentException e) {
                System.out.println("Expected Exception caught: " + e.getMessage());
            }

            // טסט לחריגת ID רחפן
            try {
                System.out.println("Testing invalid Drone ID:");
                Drone invalidDrone = new Drone(123, false, ClearanceLevel.LOW, 8, 0);
            } catch (IncidentException e) {
                System.out.println("Expected Exception caught: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error during creation: " + e.getMessage());
        }
    }

    private static void testEqualityAndSorting() {
        Dispatcher d1 = (Dispatcher) NationalRescueAuthority.responders.get(0); // Shir, exp 5
        Dispatcher dupD1 = new Dispatcher(111111111, false, ClearanceLevel.HIGH, "Shir Duplicate", 0, 7000, 5, DrinkPreference.COFFEE);
        // אמור להחזיר true
        System.out.println("d1 equals dupD1 (same ID)? " + d1.equals(dupD1));
        boolean added = NationalRescueAuthority.addResponder(dupD1);
        // אמור להחזיר false
        System.out.println("Was duplicate added to system? " + added);

        Dispatcher d2 = (Dispatcher) NationalRescueAuthority.responders.get(2);
        // אמור להחזיר מספר שלילי
        System.out.println("CompareTo Test (Shir 5 vs Yael 10): " + d1.compareTo(d2));
    }

    private static void checkWorkLimits(Incident incident) {
        // ניקח פרמדיק שעבד ונוסיף לו המון שעות כדי לראות אם הוא נחסם
        Medic workingMedic = null;
        for(Responder r : incident.getContributors()) {
            if (r instanceof Medic) {
                workingMedic = (Medic) r;
                break;
            }
        }

        if(workingMedic != null) {
            System.out.println(workingMedic.getName() + " current hours: " + workingMedic.getWorkHours());
            System.out.println("Can they work an additional 50 hours? " + workingMedic.canWorkHours(50.0));

            System.out.println("\nResetting week:");
            NationalRescueAuthority.newWeek();
            System.out.println("Are they busy now? " + workingMedic.isBusy());
            System.out.println(workingMedic.getName() + " hours after reset: " + workingMedic.getWorkHours());
        }
    }
}