# ProjectRescue 🚨


---

## Project Overview

A system for managing, coordinating, and executing emergency events across the country. Every participant holds the title of **Responder**, and missions are carried out by field personnel and control room staff working together.

---

## Core Topics

`Inheritance` · `Interface` · `Polymorphism` · `Abstract class` · `Enum` · `Static method` · `Abstract method` · `equals` · `hashCode` · `Comparable` · `Comparator` · `Exceptions`

---

## Project Structure

All classes must be placed under the package:
```
rescue
```

### Class Hierarchy

```
Responder (abstract)
├── Human (abstract)
│   ├── Medic
│   └── Dispatcher  (implements Comparable<Dispatcher>)
└── Drone

FieldParticipant (interface)
├── MedicParticipant
└── DroneParticipant

Incident
├── DispatcherAssignment
└── FieldAssignment

NationalRescueAuthority
```

---

## Classes

### `NationalRescueAuthority`
The national rescue authority — manages all Responders and Incidents.

| Field | Type | Description |
|---|---|---|
| `medicMaxWork` | int | Max weekly work hours for a Medic |
| `dispatcherMaxWork` | int | Max weekly work hours for a Dispatcher |
| `minSalaryDispatcher` | double | Minimum monthly salary for Dispatchers |
| `minSalaryMedic` | double | Minimum monthly salary for Medics |
| `droneWorkNoCharge` | int | Hours a Drone can work after a full charge |
| `responders` | ArrayList | All registered Responders |
| `incidents` | ArrayList | All currently active Incidents |

**Methods:** `newWeek()`, `addResponder()`, `removeResponder()`, `addIncident()`, `removeIncident()`

---

### `Responder` *(abstract)*
Base class for all responders.

| Field | Type | Description |
|---|---|---|
| `id` | int | 9 digits for humans, 5 digits for drones |
| `clearance` | ClearanceLevel | LOW / MEDIUM / HIGH |
| `busy` | boolean | Whether currently assigned to an incident |

**Methods:**
- `canWork(Incident in)` — checks availability, clearance level, and work hour capacity
- `work(Incident in)` — marks as busy and calls `didWork()`
- `didWork(double hours)` *(abstract)* — implemented in `Human` and `Drone`
- `canWorkHours(double hours)` — checks if the responder can handle this many hours
- `equals(Object obj)` — based on `id`

**Clearance rules:**
| Responder clearance | Can work on |
|---|---|
| LOW | LOW incidents only |
| MEDIUM | LOW and MEDIUM incidents |
| HIGH | LOW, MEDIUM, and HIGH incidents |

---

### `Human` *(abstract)* — extends `Responder`

| Field | Type | Description |
|---|---|---|
| `name` | String | Person's name |
| `workHours` | double | Hours worked this week |
| `salary` | double | Monthly salary |

**Methods:**
- `Human(String name)` — initializes name; `workHours` defaults to 0, `salary` defaults to 0
- `setSalary(double salary)` — only updates if new salary is **higher** than current; otherwise throws `IllegalArgumentException("Won't accept a demotion!")`
- `setZeroWorkHours()` — resets weekly work hours to 0
- `didWork(double hours)` — adds hours to `workHours`
- `canWorkHours(double hours)` *(abstract)* — implemented in `Medic` and `Dispatcher`

---

### `Medic` — extends `Human`

| Field | Type | Description |
|---|---|---|
| `specialization` | Specialization | PARAMEDIC / NAVIGATOR / SEARCHER / TRAINEE |

**Methods:**
- `Medic(String name, Specialization specialization)` — calls `setSalary()` internally
- `setSalary(double salary)` — throws `IllegalArgumentException("Unacceptably low salary!")` if below `minSalaryMedic`; otherwise delegates to `Human.setSalary()`
- `canWorkHours(double hours)` — checks that `workHours + hours` doesn't exceed the Medic's weekly limit
- `retreatToAmbulance()` — prints `Medic (Medic's name):` then `Returning to ambulance:`
- `collectEvidence()` — prints `Medic (Medic's name):` then `Important evidence collected`
- `shareLocation()` — prints `Medic (Medic's name):` then `I'm sending my exact location now!`

---

### `Dispatcher` — extends `Human`, implements `Comparable<Dispatcher>`

| Field | Type | Description |
|---|---|---|
| `experience` | int | Years of experience |
| `drink` | DrinkPreference | WATER / COFFEE / ENERGY_DRINK |

**Methods:**
- `Dispatcher(String name, int experience, DrinkPreference drink)` — full constructor
- `setSalary(double salary)` — throws `IllegalArgumentException("Unacceptably low salary!")` if below `minSalaryDispatcher`; otherwise delegates to `Human.setSalary()`
- `canWorkHours(double hours)` — checks that `workHours + hours` doesn't exceed the Dispatcher's weekly limit
- `compareTo(Dispatcher other)` — compares by years of experience

---

### `Drone` — extends `Responder`

| Field | Type | Description |
|---|---|---|
| `workNoCharge` | double | Hours the drone can still work without recharging |
| `reportedFailures` | int | Number of reported malfunctions |

**Methods:**
- `setWorkNoCharge()` — sets `workNoCharge` to `droneWorkNoCharge` from `NationalRescueAuthority`
- `addAFailure()` — increments `reportedFailures` by 1
- `didWork(double hours)` — subtracts hours from `workNoCharge`
- `canWorkHours(double hours)` — returns `false` if `hours > workNoCharge`, otherwise `true`
- `retreatToChargingStation()` — prints `Drone (Drone's id):` then `Returning to charging station:`
- `collectEvidence()` — prints `Drone (Drone's id):` then `Important evidence collected!`
- `shareLocation()` — prints `Drone (Drone's id):` then `I'm sending my exact location now!`

---

### `Incident`

| Field | Type | Description |
|---|---|---|
| `type` | String | Description of the incident |
| `serialNumber` | int | Serial number (incidents of the same type get sequential numbers) |
| `clearance` | ClearanceLevel | Required clearance level |
| `hours` | double | Estimated hours required |
| `contributors` | ArrayList\<Responder\> | All assigned responders |
| `controlRoom` | DispatcherAssignment | Control room part of the incident |
| `inField` | FieldAssignment | Field part of the incident |

**Methods:**
- Full constructor — creates both `DispatcherAssignment` and `FieldAssignment` objects internally
- `setContributors()` — merges the lists from `controlRoom` and `inField` into `contributors`
- `assignAll()` — calls the assign methods of both `controlRoom` and `inField`
- `equals(Object obj)` — based on `type` and `serialNumber`

---

### `DispatcherAssignment`

| Field | Type | Description |
|---|---|---|
| `numDispatchers` | int | Exact number of dispatchers required |
| `dispatchers` | ArrayList\<Dispatcher\> | Assigned dispatchers |
| `room` | int | Control room number |



---

### `FieldAssignment`

| Field | Type | Description |
|---|---|---|
| `numMedics` | int | Exact number of medics required |
| `numDrones` | int | Exact number of drones required |
| `fieldParticipants` | ArrayList\<FieldParticipant\> | All assigned field participants |


---

## Interfaces

### `FieldParticipant`
| Method | Behavior |
|---|---|
| `collectEvidence()` | Print identifier line, then `Important evidence collected!` |
| `shareLocation()` | Print identifier line, then `I'm sending my exact location now!` |

### `MedicParticipant` — extends `FieldParticipant`
| Method | Behavior |
|---|---|
| `retreatToAmbulance()` | Print `Medic (Medic's name):` then `Returning to ambulance:` |

### `DroneParticipant` — extends `FieldParticipant`
| Method | Behavior |
|---|---|
| `retreatToChargingStation()` | Print `Drone (Drone's id):` then `Returning to charging station:` |

---

## Enums

| Enum | Values |
|---|---|
| `ClearanceLevel` | LOW, MEDIUM, HIGH |
| `Specialization` | PARAMEDIC, NAVIGATOR, SEARCHER, TRAINEE |
| `DrinkPreference` | WATER, COFFEE, ENERGY_DRINK |

---

## Exceptions

```
IncidentException  (custom exception)
├── AssignFieldException        → "Not enough medics"
└── AssignDispatcherException   → "Not enough dispatchers"

IllegalArgumentException  → thrown whenever an invalid parameter is passed to a method
```


