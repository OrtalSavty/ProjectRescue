# National Rescue Authority - Frontend Interface

## Overview
A modern, responsive web-based user interface for the National Rescue Authority Dispatch System.

## Features

### 📊 Dashboard
- Real-time system status display
- Active responders and incidents count
- System parameters overview
- Recent activities log
- Responder summary with type breakdowns

### 👮 Responders Management
- **Add new responders** (Dispatchers, Medics, Drones)
- View responder details and status
- Track working hours
- View salary and specialization information
- Remove responders from the system

**Responder Types:**
- **Dispatcher**: Manage with clearance levels (LOW, MEDIUM, HIGH)
- **Medic**: Manage with specializations (Intensive Care, Emergency, Trauma, Pediatric)
- **Drone**: Track battery level and model

### 🚨 Incidents Management
- **Create new incidents** with detailed parameters
- Track required resources (dispatchers, medics, drones)
- Assign responders to incidents automatically
- View incident status and clearance levels
- Monitor assigned team members

**Clearance Levels:**
- LOW (Green badge)
- MEDIUM (Yellow badge)
- HIGH (Red badge)

### ⚙️ System Configuration
- Update dispatcher maximum working hours
- Update medic maximum working hours
- Set minimum salaries
- Configure drone charging requirements
- Start a new week (resets all responders)

## How to Use

### 1. Opening the Application
Simply open the `index.html` file in a modern web browser (Chrome, Firefox, Edge, Safari).

### 2. Initial Setup
First, configure your system parameters:
1. Navigate to **"System Config"** tab
2. Set the working hours, salaries, and drone parameters
3. Click **"Save Configuration"**

### 3. Adding Responders
1. Go to the **"Responders"** tab
2. Click **"+ Add Responder"**
3. Fill in the required information based on responder type
4. Click **"Add"**

### 4. Managing Incidents
1. Go to the **"Incidents"** tab
2. Click **"+ Create Incident"**
3. Enter incident details (type, serial number, clearance level, required resources)
4. Click **"Create"**

### 5. Assigning Teams
1. In the Incidents tab, click **"Assign Team"** on any incident
2. The system will automatically find available responders
3. Responders will be marked as busy and their hours will be updated

### 6. Monitoring
- Check the **Dashboard** for an overview of operations
- View recent activities to track system changes
- Monitor responder availability and work hours

## File Structure

```
ProjectRescue/
├── index.html        # Main HTML interface
├── style.css         # Styling and layout
├── script.js         # Functionality and interactivity
└── README.md         # This file
```

## System Data Structure

The frontend maintains:
- **System Configuration**: Work hours, salaries, parameters
- **Responders**: All personnel and equipment
- **Incidents**: Active emergency calls
- **Activities**: Operation history log

## Features Included

✅ Responsive design (works on desktop, tablet, mobile)
✅ Tab-based navigation
✅ Modal dialogs for detailed views
✅ Form validation
✅ Real-time updates
✅ Activity logging
✅ Notification system
✅ Type-specific responder forms
✅ Automatic team assignment
✅ Status indicators
✅ Professional styling with rescue theme

## Technical Details

- **Framework**: Vanilla JavaScript (No dependencies)
- **Styling**: CSS3 with CSS Variables
- **Storage**: In-memory data (clears on page refresh)
- **Responsive**: Mobile-first design approach
- **Accessibility**: Semantic HTML structure

## Notes

- Data is stored in browser memory and will clear when you refresh the page
- To persist data across sessions, consider adding a backend database integration
- The system can be extended to connect with your Java backend via API calls

## Color Scheme

- **Primary Red**: #e74c3c (Emergency/Action)
- **Secondary Blue**: #3498db (Information)
- **Success Green**: #27ae60 (Available/Good status)
- **Warning Orange**: #f39c12 (Caution)
- **Text Dark**: #2c3e50 (Main text)
- **Light Background**: #ecf0f1 (Page background)

## Browser Support

- Chrome/Edge (Latest)
- Firefox (Latest)
- Safari (Latest)
- Mobile browsers (iOS Safari, Chrome Mobile)

## Future Enhancements

Potential features to add:
- Backend API integration with Java server
- Database persistence
- User authentication
- Real-time notifications
- GPS tracking for responders
- Incident history analytics
- Export reports
- Multi-language support
- Dark mode theme

---

**Created for Project Rescue - National Rescue Authority Management System**
