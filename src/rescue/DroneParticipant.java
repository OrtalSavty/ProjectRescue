package rescue;

// חוזה לרחפנים
// אומר שרחפן מסויים משתתף בשטח אבל גם יודע לחזור לעמימדת טעינה
public interface DroneParticipant extends FieldParticipants{
    void retreatToChargingStation();
}
