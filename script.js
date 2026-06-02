// ====================================
// DATA MANAGEMENT
// ====================================

// Simulated data structure to mirror Java backend
const systemData = {
    dataVersion: 1,
    config: {
        medicMaxWork: 45,
        dispatcherMaxWork: 40,
        minSalaryDispatcher: 6000,
        minSalaryMedic: 7000,
        droneWorkNoCharge: 8
    },
    responders: [],
    incidents: [],
    activities: []
};

// ====================================
// INITIALIZATION
// ====================================

document.addEventListener('DOMContentLoaded', async function() {
    // Initialize event listeners
    setupTabNavigation();
    setupFormHandlers();
    
    // Load initial data
    loadDataFromStorage();
    await loadSampleDataIfEmpty();
    updateSystemConfig();
    updateDashboard();
    renderRespondersList();
    renderIncidentsList();
    
    // Display welcome message
    addActivity('System initialized and ready for operations');
});

// ====================================
// TAB NAVIGATION
// ====================================

function setupTabNavigation() {
    const tabButtons = document.querySelectorAll('.tab-button');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            const tabName = this.getAttribute('data-tab');
            switchTab(tabName);
        });
    });
}

function switchTab(tabName) {
    // Remove active class from all tabs
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    
    document.querySelectorAll('.tab-button').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Add active class to selected tab
    document.getElementById(tabName).classList.add('active');
    document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');
}

// ====================================
// DASHBOARD MANAGEMENT
// ====================================

function updateDashboard() {
    updateStats();
    updateSystemStatus();
    updateActivitiesList();
}

function updateStats() {
    // Count responder types
    const dispatchers = systemData.responders.filter(r => r.type === 'dispatcher').length;
    const medics = systemData.responders.filter(r => r.type === 'medic').length;
    const drones = systemData.responders.filter(r => r.type === 'drone').length;
    
    document.getElementById('activeResponders').textContent = systemData.responders.length;
    document.getElementById('activeIncidents').textContent = systemData.incidents.length;
    document.getElementById('dispatcherCount').textContent = dispatchers;
    document.getElementById('medicCount').textContent = medics;
    document.getElementById('droneCount').textContent = drones;
}

function updateSystemStatus() {
    document.getElementById('dispatcherMaxHours').textContent = systemData.config.dispatcherMaxWork;
    document.getElementById('medicMaxHours').textContent = systemData.config.medicMaxWork;
    document.getElementById('minDispatcherSalary').textContent = systemData.config.minSalaryDispatcher;
    document.getElementById('minMedicSalary').textContent = systemData.config.minSalaryMedic;
    document.getElementById('droneWorkNoCharge').textContent = systemData.config.droneWorkNoCharge;
}

function updateActivitiesList() {
    const activitiesList = document.getElementById('activitiesList');
    
    if (systemData.activities.length === 0) {
        activitiesList.innerHTML = '<p class="empty-message">No activities yet</p>';
        return;
    }
    
    // Show last 10 activities
    const recentActivities = systemData.activities.slice(-10).reverse();
    activitiesList.innerHTML = recentActivities.map(activity => `
        <div class="activity-item">
            ${activity.message}
            <div class="time">${activity.time}</div>
        </div>
    `).join('');
}

function addActivity(message) {
    const time = new Date().toLocaleTimeString();
    systemData.activities.push({
        message: message,
        time: time
    });
    updateActivitiesList();
    saveDataToStorage();
}

// ====================================
// STORAGE MANAGEMENT (localStorage)
// ====================================

function saveDataToStorage() {
    localStorage.setItem('systemData', JSON.stringify(systemData));
}

function loadDataFromStorage() {
    const savedData = localStorage.getItem('systemData');
    if (savedData) {
        try {
            const parsed = JSON.parse(savedData);
            const parsedVersion = typeof parsed.dataVersion === 'number' ? parsed.dataVersion : 0;
            systemData.dataVersion = parsedVersion;
            systemData.responders = parsed.responders || [];
            systemData.incidents = parsed.incidents || [];
            systemData.activities = parsed.activities || [];
            systemData.config = parsed.config || systemData.config;
        } catch (e) {
            console.error('Error loading data from storage:', e);
        }
    }
}

async function loadSampleDataIfEmpty() {
    const shouldLoadInitialSample = systemData.responders.length === 0 && systemData.incidents.length === 0;
    let sampleData;

    try {
        const response = await fetch('data/sampleData.json');
        if (response.ok) {
            sampleData = await response.json();
            const sampleVersion = sampleData.dataVersion || 1;
            const storedVersion = systemData.dataVersion || 0;
            const shouldRefreshSample = shouldLoadInitialSample || storedVersion < sampleVersion;

            if (shouldRefreshSample) {
                systemData.dataVersion = sampleVersion;
                systemData.responders = sampleData.responders || [];
                systemData.incidents = sampleData.incidents || [];
                systemData.activities = sampleData.activities || [];
                systemData.config = sampleData.config || systemData.config;
                addActivity('Loaded sample data from data/sampleData.json');
                saveDataToStorage();
            }

            return;
        }
    } catch (e) {
        console.warn('Failed to load external sample data:', e);
    }

    if (shouldLoadInitialSample) {
        systemData.dataVersion = 1;
        systemData.responders = createSampleResponders();
        systemData.incidents = createSampleIncidents();
        addActivity('Loaded fallback sample data with 20 medics, 10 drones, and full support staff.');
        saveDataToStorage();
    }
}

function createSampleResponders() {
    const dispatchers = [
        { name: 'Dov Cohen', id: 101001001, type: 'dispatcher', salary: 7500, clearanceLevel: 'HIGH', busy: false, currentHours: 0 },
        { name: 'Eli Barak', id: 101001002, type: 'dispatcher', salary: 7300, clearanceLevel: 'MEDIUM', busy: false, currentHours: 0 },
        { name: 'Yael Levi', id: 101001003, type: 'dispatcher', salary: 7600, clearanceLevel: 'HIGH', busy: false, currentHours: 0 },
        { name: 'Shira Tal', id: 101001004, type: 'dispatcher', salary: 7200, clearanceLevel: 'MEDIUM', busy: false, currentHours: 0 },
        { name: 'Ron Ashkenazi', id: 101001005, type: 'dispatcher', salary: 7800, clearanceLevel: 'HIGH', busy: false, currentHours: 0 },
        { name: 'Noam Azulay', id: 101001006, type: 'dispatcher', salary: 7400, clearanceLevel: 'MEDIUM', busy: false, currentHours: 0 },
        { name: 'Ari Shapira', id: 101001007, type: 'dispatcher', salary: 7600, clearanceLevel: 'HIGH', busy: false, currentHours: 0 },
        { name: 'Noa Golan', id: 101001008, type: 'dispatcher', salary: 7550, clearanceLevel: 'MEDIUM', busy: false, currentHours: 0 }
    ];

    const medicNames = [
        'Liat Moshe','Idan Shalev','Maya Peretz','Gal Amir','Shani Zaken',
        'Hadas Tzur','Tal Hefetz','Moran Avraham','Nadav Ben-Ami','Adi Eyal',
        'Rotem Levi','Yaara Gold','Omer Katz','Michal Ben-David','Eran Bar-On',
        'Avigail Polak','Tom Sella','Roni Dahan','Eitan Amir','Lian Sharir',
        'Shachar Cohen','Rivka Alon','Ariel Gavriel','Einav Raz','Galit Neeman',
        'Oren Shachar','Tali Klein','Noga Paz'
    ];
    const medicSpecializations = ['INTENSIVE_CARE','EMERGENCY','TRAUMA','PEDIATRIC'];
    const medics = medicNames.map((name, index) => ({
        name,
        id: 202000000 + index + 1,
        type: 'medic',
        salary: 8200,
        specialization: medicSpecializations[index % medicSpecializations.length],
        busy: false,
        currentHours: 0
    }));

    const drones = Array.from({ length: 14 }, (_, i) => ({
        name: `Drone-${i + 1}`,
        id: 30300000 + i + 1,
        type: 'drone',
        model: `X-${100 + i}`,
        batteryLevel: 100,
        busy: false,
        currentHours: 0
    }));

    return [...dispatchers, ...medics, ...drones];
}

function createSampleIncidents() {
    return [
        {
            type: 'Mountain Rescue',
            serialNumber: 5001,
            clearanceLevel: 'HIGH',
            hours: 4.5,
            numDispatchers: 3,
            room: 201,
            numMedics: 6,
            numDrones: 2,
            assignedResponders: []
        },
        {
            type: 'Flood Response',
            serialNumber: 5002,
            clearanceLevel: 'MEDIUM',
            hours: 3.0,
            numDispatchers: 2,
            room: 202,
            numMedics: 5,
            numDrones: 2,
            assignedResponders: []
        }
    ];
}

// ====================================
// SYSTEM CONFIGURATION
// ====================================

function setupFormHandlers() {
    // Load config values into form
    document.getElementById('configDispatcherMaxWork').value = systemData.config.dispatcherMaxWork;
    document.getElementById('configMedicMaxWork').value = systemData.config.medicMaxWork;
    document.getElementById('configMinSalaryDispatcher').value = systemData.config.minSalaryDispatcher;
    document.getElementById('configMinSalaryMedic').value = systemData.config.minSalaryMedic;
    document.getElementById('configDroneWorkNoCharge').value = systemData.config.droneWorkNoCharge;
}

function updateSystemConfig(event) {
    if (event) {
        event.preventDefault();
    }
    
    systemData.config.dispatcherMaxWork = parseInt(document.getElementById('configDispatcherMaxWork').value);
    systemData.config.medicMaxWork = parseInt(document.getElementById('configMedicMaxWork').value);
    systemData.config.minSalaryDispatcher = parseFloat(document.getElementById('configMinSalaryDispatcher').value);
    systemData.config.minSalaryMedic = parseFloat(document.getElementById('configMinSalaryMedic').value);
    systemData.config.droneWorkNoCharge = parseInt(document.getElementById('configDroneWorkNoCharge').value);
    
    updateSystemStatus();
    addActivity('System configuration updated');
    showNotification('Configuration saved successfully!', 'success');
    saveDataToStorage();
}

function startNewWeek() {
    // Reset all responders' availability
    systemData.responders.forEach(responder => {
        responder.busy = false;
        responder.currentHours = 0;
    });
    
    // Clear incidents
    systemData.incidents = [];
    
    addActivity('New week started - All responders reset');
    updateDashboard();
    renderRespondersList();
    renderIncidentsList();
    showNotification('New week started! All responders have been reset.', 'success');
    saveDataToStorage();
}

function resetToSampleData() {
    localStorage.removeItem('systemData');
    showNotification('Reloading sample data from JSON...', 'success');
    setTimeout(() => window.location.reload(), 200);
}

// ====================================
// RESPONDERS MANAGEMENT
// ====================================

function showAddResponderForm() {
    document.getElementById('addResponderForm').style.display = 'block';
}

function hideAddResponderForm() {
    document.getElementById('addResponderForm').style.display = 'none';
    document.getElementById('addResponderForm').querySelector('form').reset();
    document.getElementById('typeSpecificFields').innerHTML = '';
}

function updateResponderFields() {
    const type = document.getElementById('responderType').value;
    const fieldsContainer = document.getElementById('typeSpecificFields');
    
    let htmlContent = '';
    
    if (type === 'dispatcher') {
        htmlContent = `
            <div class="form-group">
                <label>Salary (₪)</label>
                <input type="number" id="responderSalary" step="100" min="0" required>
            </div>
            <div class="form-group">
                <label>Clearance Level</label>
                <select id="responderClearance" required>
                    <option value="">Select clearance</option>
                    <option value="LOW">LOW</option>
                    <option value="MEDIUM">MEDIUM</option>
                    <option value="HIGH">HIGH</option>
                </select>
            </div>
        `;
    } else if (type === 'medic') {
        htmlContent = `
            <div class="form-group">
                <label>Salary (₪)</label>
                <input type="number" id="responderSalary" step="100" min="0" required>
            </div>
            <div class="form-group">
                <label>Specialization</label>
                <select id="responderSpecialization" required>
                    <option value="">Select specialization</option>
                    <option value="INTENSIVE_CARE">Intensive Care</option>
                    <option value="EMERGENCY">Emergency</option>
                    <option value="TRAUMA">Trauma</option>
                    <option value="PEDIATRIC">Pediatric</option>
                </select>
            </div>
        `;
    } else if (type === 'drone') {
        htmlContent = `
            <div class="form-group">
                <label>Model</label>
                <input type="text" id="responderModel" required placeholder="Enter drone model">
            </div>
            <div class="form-group">
                <label>Battery Level (%)</label>
                <input type="number" id="responderBattery" min="0" max="100" required value="100">
            </div>
        `;
    }
    
    fieldsContainer.innerHTML = htmlContent;
}

function addResponder(event) {
    event.preventDefault();
    
    const name = document.getElementById('responderName').value;
    const id = parseInt(document.getElementById('responderId').value);
    const type = document.getElementById('responderType').value;
    
    if (!type) {
        showNotification('Please select a responder type', 'error');
        return;
    }
    
    let responder = {
        name: name,
        id: id,
        type: type,
        busy: false,
        currentHours: 0
    };
    
    if (type === 'dispatcher') {
        responder.salary = parseFloat(document.getElementById('responderSalary').value);
        responder.clearanceLevel = document.getElementById('responderClearance').value;
    } else if (type === 'medic') {
        responder.salary = parseFloat(document.getElementById('responderSalary').value);
        responder.specialization = document.getElementById('responderSpecialization').value;
    } else if (type === 'drone') {
        responder.model = document.getElementById('responderModel').value;
        responder.batteryLevel = parseInt(document.getElementById('responderBattery').value);
    }
    
    systemData.responders.push(responder);
    
    hideAddResponderForm();
    renderRespondersList();
    updateStats();
    addActivity(`New ${type} added: ${name} (ID: ${id})`);
    saveDataToStorage();
    showNotification(`${type.charAt(0).toUpperCase() + type.slice(1)} added successfully!`, 'success');
}

function renderRespondersList() {
    const container = document.getElementById('respondersList');
    
    if (systemData.responders.length === 0) {
        container.innerHTML = '<p class="empty-message">No responders yet. Add one to get started.</p>';
        return;
    }
    
    container.innerHTML = systemData.responders.map((responder, index) => {
        const getIcon = (type) => {
            switch(type) {
                case 'dispatcher': return '👮';
                case 'medic': return '🏥';
                case 'drone': return '🛸';
                default: return '👤';
            }
        };
        
        const statusClass = responder.busy ? 'busy' : 'available';
        const statusText = responder.busy ? 'Busy' : 'Available';
        
        return `
            <div class="responder-card ${responder.type}" onclick="viewResponderDetails(${index})">
                <div class="responder-header">
                    <div>
                        <div class="responder-name">${getIcon(responder.type)} ${responder.name}</div>
                        <span class="responder-type ${responder.type}">${responder.type.toUpperCase()}</span>
                    </div>
                </div>
                <div class="responder-info">
                    <div class="responder-details">
                        <div><strong>ID:</strong> ${responder.id}</div>
                        <div><strong>Status:</strong> <span class="status-indicator ${statusClass}"><span class="status-dot"></span>${statusText}</span></div>
                        <div><strong>Hours Worked:</strong> ${responder.currentHours}</div>
                        ${responder.salary ? `<div><strong>Salary:</strong> ₪${responder.salary}</div>` : ''}
                        ${responder.clearanceLevel ? `<div><strong>Clearance:</strong> ${responder.clearanceLevel}</div>` : ''}
                        ${responder.specialization ? `<div><strong>Specialization:</strong> ${responder.specialization}</div>` : ''}
                        ${responder.model ? `<div><strong>Model:</strong> ${responder.model}</div>` : ''}
                        ${responder.batteryLevel !== undefined ? `<div><strong>Battery:</strong> ${responder.batteryLevel}%</div>` : ''}
                    </div>
                </div>
                <div class="responder-actions">
                    <button class="btn btn-secondary" onclick="removeResponder(${index}); event.stopPropagation();">Remove</button>
                </div>
            </div>
        `;
    }).join('');
}

function viewResponderDetails(index) {
    const responder = systemData.responders[index];
    const modal = document.getElementById('responderModal');
    const content = document.getElementById('responderModalContent');
    
    let detailsHtml = `
        <h3>${responder.name} - ${responder.type.toUpperCase()}</h3>
        <div style="margin-top: 20px;">
            <p><strong>ID:</strong> ${responder.id}</p>
            <p><strong>Status:</strong> ${responder.busy ? 'Busy' : 'Available'}</p>
            <p><strong>Current Hours Worked:</strong> ${responder.currentHours}</p>
    `;
    
    if (responder.salary) {
        detailsHtml += `<p><strong>Salary:</strong> ₪${responder.salary}</p>`;
    }
    if (responder.clearanceLevel) {
        detailsHtml += `<p><strong>Clearance Level:</strong> ${responder.clearanceLevel}</p>`;
    }
    if (responder.specialization) {
        detailsHtml += `<p><strong>Specialization:</strong> ${responder.specialization}</p>`;
    }
    if (responder.model) {
        detailsHtml += `<p><strong>Drone Model:</strong> ${responder.model}</p>`;
    }
    if (responder.batteryLevel !== undefined) {
        detailsHtml += `<p><strong>Battery Level:</strong> ${responder.batteryLevel}%</p>`;
    }
    
    detailsHtml += '</div>';
    
    content.innerHTML = detailsHtml;
    modal.style.display = 'block';
}

function closeResponderModal() {
    document.getElementById('responderModal').style.display = 'none';
}

function removeResponder(index) {
    const responder = systemData.responders[index];
    systemData.responders.splice(index, 1);
    renderRespondersList();
    updateStats();
    addActivity(`Responder removed: ${responder.name}`);
    saveDataToStorage();
    showNotification(`${responder.name} has been removed.`, 'success');
}

// ====================================
// INCIDENTS MANAGEMENT
// ====================================

function showAddIncidentForm() {
    document.getElementById('addIncidentForm').style.display = 'block';
}

function hideAddIncidentForm() {
    document.getElementById('addIncidentForm').style.display = 'none';
    document.getElementById('addIncidentForm').querySelector('form').reset();
}

function addIncident(event) {
    event.preventDefault();
    
    const incident = {
        type: document.getElementById('incidentType').value,
        serialNumber: parseInt(document.getElementById('incidentSerial').value),
        clearanceLevel: document.getElementById('incidentClearance').value,
        hours: parseFloat(document.getElementById('incidentHours').value),
        numDispatchers: parseInt(document.getElementById('incidentDispatchers').value),
        room: parseInt(document.getElementById('incidentRoom').value),
        numMedics: parseInt(document.getElementById('incidentMedics').value),
        numDrones: parseInt(document.getElementById('incidentDrones').value),
        assignedResponders: []
    };
    
    systemData.incidents.push(incident);
    const incidentIndex = systemData.incidents.length - 1;
    hideAddIncidentForm();
    assignResponders(incidentIndex);
    updateStats();
}

function renderIncidentsList() {
    const container = document.getElementById('incidentsList');
    
    if (systemData.incidents.length === 0) {
        container.innerHTML = '<p class="empty-message">No incidents yet. Create one to get started.</p>';
        return;
    }
    
    container.innerHTML = systemData.incidents.map((incident, index) => {
        const getClearanceClass = (level) => level.toLowerCase();
        
        return `
            <div class="incident-card" onclick="viewIncidentDetails(${index})">
                <div class="incident-header">
                    <div>
                        <div class="incident-type">🚨 ${incident.type}</div>
                        <div class="text-muted" style="font-size: 0.9em;">Serial: ${incident.serialNumber}</div>
                    </div>
                    <span class="clearance-badge ${getClearanceClass(incident.clearanceLevel)}">${incident.clearanceLevel}</span>
                </div>
                <div class="incident-info">
                    <div class="incident-detail">
                        <span class="incident-detail-label">Hours Needed</span>
                        <span class="incident-detail-value">${incident.hours}h</span>
                    </div>
                    <div class="incident-detail">
                        <span class="incident-detail-label">Dispatchers</span>
                        <span class="incident-detail-value">${incident.numDispatchers}</span>
                    </div>
                    <div class="incident-detail">
                        <span class="incident-detail-label">Medics</span>
                        <span class="incident-detail-value">${incident.numMedics}</span>
                    </div>
                    <div class="incident-detail">
                        <span class="incident-detail-label">Drones</span>
                        <span class="incident-detail-value">${incident.numDrones}</span>
                    </div>
                </div>
                <div class="incident-info" style="font-size: 0.9em; margin-top: 10px;">
                    <div style="padding: 8px; background: #ecf0f1; border-radius: 4px;">
                        <strong>Room:</strong> ${incident.room}
                    </div>
                    <div style="padding: 8px; background: #ecf0f1; border-radius: 4px;">
                        <strong>Assigned:</strong> ${incident.assignedResponders.length}
                    </div>
                </div>
                <div class="incident-actions">
                    <button class="btn btn-success" onclick="assignResponders(${index}); event.stopPropagation();">Assign Team</button>
                    <button class="btn btn-danger" onclick="removeIncident(${index}); event.stopPropagation();">Delete</button>
                </div>
            </div>
        `;
    }).join('');
}

function viewIncidentDetails(index) {
    const incident = systemData.incidents[index];
    const modal = document.getElementById('incidentModal');
    const content = document.getElementById('incidentModalContent');
    
    let detailsHtml = `
        <h3>${incident.type} - Serial #${incident.serialNumber}</h3>
        <div style="margin-top: 20px;">
            <p><strong>Clearance Level:</strong> <span class="clearance-badge ${incident.clearanceLevel.toLowerCase()}">${incident.clearanceLevel}</span></p>
            <p><strong>Hours Required:</strong> ${incident.hours}</p>
            <p><strong>Room Number:</strong> ${incident.room}</p>
            <h4 style="margin-top: 20px;">Required Resources:</h4>
            <ul>
                <li>Dispatchers: ${incident.numDispatchers}</li>
                <li>Medics: ${incident.numMedics}</li>
                <li>Drones: ${incident.numDrones}</li>
            </ul>
            <h4 style="margin-top: 20px;">Assigned Responders (${incident.assignedResponders.length}):</h4>
            <ul>
                ${incident.assignedResponders.length > 0 
                    ? incident.assignedResponders.map(r => `<li>${r.name} (${r.type})</li>`).join('')
                    : '<li style="color: #7f8c8d;">None assigned yet</li>'
                }
            </ul>
        </div>
    `;
    
    content.innerHTML = detailsHtml;
    modal.style.display = 'block';
}

function closeIncidentModal() {
    document.getElementById('incidentModal').style.display = 'none';
}

function assignResponders(incidentIndex) {
    const incident = systemData.incidents[incidentIndex];
    
    // Simple assignment algorithm
    let assigned = [];
    
    // Assign dispatchers
    for (let i = 0; i < incident.numDispatchers && i < systemData.responders.length; i++) {
        const dispatcher = systemData.responders.find(r => 
            r.type === 'dispatcher' && !r.busy && !assigned.some(a => a.id === r.id)
        );
        if (dispatcher) {
            assigned.push(dispatcher);
            dispatcher.busy = true;
            dispatcher.currentHours += incident.hours;
        }
    }
    
    // Assign medics
    for (let i = 0; i < incident.numMedics && i < systemData.responders.length; i++) {
        const medic = systemData.responders.find(r => 
            r.type === 'medic' && !r.busy && !assigned.some(a => a.id === r.id)
        );
        if (medic) {
            assigned.push(medic);
            medic.busy = true;
            medic.currentHours += incident.hours;
        }
    }
    
    // Assign drones
    for (let i = 0; i < incident.numDrones && i < systemData.responders.length; i++) {
        const drone = systemData.responders.find(r => 
            r.type === 'drone' && !r.busy && !assigned.some(a => a.id === r.id)
        );
        if (drone) {
            assigned.push(drone);
            drone.busy = true;
            drone.currentHours += incident.hours;
        }
    }
    
    incident.assignedResponders = assigned;
    
    if (assigned.length > 0) {
        renderRespondersList();
        renderIncidentsList();
        addActivity(`Team assigned to incident: ${incident.type} (${assigned.length} members)`);
        showNotification(`Successfully assigned ${assigned.length} responders to the incident!`, 'success');
        saveDataToStorage();
        return true;
    } else {
        showNotification('Not enough available responders to assign to this incident.', 'error');
        saveDataToStorage();
        return false;
    }
}

function removeIncident(index) {
    const incident = systemData.incidents[index];
    
    // Make responders available again
    incident.assignedResponders.forEach(responder => {
        responder.busy = false;
        responder.currentHours -= incident.hours;
    });
    
    systemData.incidents.splice(index, 1);
    renderIncidentsList();
    renderRespondersList();
    updateStats();
    addActivity(`Incident deleted: ${incident.type}`);
    showNotification('Incident has been deleted.', 'success');
    saveDataToStorage();
}

// ====================================
// UTILITIES & NOTIFICATIONS
// ====================================

function showNotification(message, type = 'info') {
    // Create temporary notification
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        background: ${type === 'success' ? '#27ae60' : type === 'error' ? '#e74c3c' : '#3498db'};
        color: white;
        border-radius: 6px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
        z-index: 10000;
        animation: slideIn 0.3s ease-out;
    `;
    
    notification.textContent = message;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

// Close modals when clicking outside
window.onclick = function(event) {
    const responderModal = document.getElementById('responderModal');
    const incidentModal = document.getElementById('incidentModal');
    
    if (event.target === responderModal) {
        responderModal.style.display = 'none';
    }
    if (event.target === incidentModal) {
        incidentModal.style.display = 'none';
    }
}

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);
