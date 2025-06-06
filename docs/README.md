
# SuperLi – Supermarket Management System
submited by: 
Tal Yitzhaky 321423477
Yarden Shwarz 212396444
Hila Pelet 318938099
Ido Assaf 211856679
## 1. Introduction
Welcome to the "SuperLi" Supermarket computerized management system. This manual provides
step-by-step guidance for managing both employee shifts and transportation tasks, suitable for all
authorized users.

## 2. Logging into the System
### Initial Setup:
The system starts with basic data only:
- Branch information (ID: 1, Name: example, Address: example, Employees: the manager)
- Manager login credentials (Username: 123456789, Password: 123)

When entering the system, you will be asked whether to reset sample data or not:
- If you choose "no" – sample branches, employees, shifts, roles, trucks, shipment areas, sites, 
  transportations, item documents and items will be loaded.
- If you choose "yes" – the system will continue with no data stored except the systemManager.

Login process:
- Enter your username (national ID) and password on the login screen.
- Press Enter.
- A menu will be shown based on your role:
  - HR Manager → HR menu  
  - Transportation Manager → Transportation menu  
  - Driver → Driver menu  
  - Regular Employee → Employee menu

## 3. Users and Roles
- Default login: Manager (ID: 123456789, password: 123)
- The system allows adding, removing, viewing and updating users, roles, and shift assignments.
- In addition, the system allows managing transportations.
- **Role IDs must be unique!**

## 4. HR Management Module

### HR Manager Menu
1. View Shift Swap Requests  
2. Approve or Reject a Swap Request  
3. Add New Employee  
4. Delete Employee  
5. Create New Shift  
6. Delete Shift  
7. Update Employee Info  
8. Find Shift by ID  
9. View All Shifts for This Week  
10. Add New Role  
11. Get All Employees Qualified for a Specific Role  
12. Create Shift Assignment  
13. Find Employees by Availability  
14. Cancel an Employee's Assignment to a Shift  
15. View a Specific Shift Detail  
16. View All Roles in the System  
17. Exit

> Note: When adding the role "Shift Manager", make sure the role ID is set to 1.

### Employee Menu
1. Submit Shift Preferences  
2. View Weekly Assignments (My Shifts)  
3. View Full Weekly Assignment Report  
4. Submit Shift Swap Request  
5. View My Personal Details  
6. View a Specific Shift Detail (for Shift Managers only)  
7. Exit

## 5. Transportation Management Module

### Transportation Manager Menu
1. Add Truck  
2. Create Shipment Area  
3. Change Shipment Area  
4. Make Transportation  
5. Change Date  
6. Change Time  
7. Change Truck  
8. Change Driver  
9. Change Area IDs  
10. Change Origin Site  
11. Change Success Status  
12. Add Items  
13. Remove Items  
14. View Document  
15. View All Transportations  
16. View All Trucks  
17. View All Drivers  
18. Report Success  
19. Add Site  
20. Remove Truck  
21. Logout  
0. Exit

### Driver Menu
1. Report Accident  
2. View Transportation Document  
3. View Items List  
4. Logout  
0. Exit

### Notes:
- Inputs are prompted via the console.  
- IDs must be integers.  
- License types: A–E  
- Items are added after transportation is created.  
- Errors prompt the user to re-enter correct data.

## 6. Glossary & System Assumptions

- **Transportation** – Item shipment from one site to others  
- **Site** – A supermarket or supplier location  
- **Item** – A product unit  
- **Items Document** – List of items per destination  
- **Shipment Area** – A region that contains multiple sites  
- **HR Manager** – A user with permissions to manage employees, shifts, roles, and branches  
- **Shift Manager** – An employee assigned managerial responsibilities during a specific shift  
- **Employee** – A user assigned to a role and shift based on availability and qualification  
- **Shift** – A defined time unit (e.g., morning, evening, night) with employee assignments  
- **Branch** – A physical supermarket location where operational and shift activity occurs


## 8. Sample Data Inserted (Simplified Tables)

### roles
|   id | name                   | is_archived   |
|-----:|:-----------------------|:--------------|
|    1 | shift manager          | false         |
|    2 | cashier                | false         |
|    3 | stocker                | false         |
|    4 | driver                 | false         |
|    5 | butcher                | false         |
|    6 | transportation manager | false         |
|    7 | warehouse              | false         |

### employees
|   id | name    | phone_number   |   branch_id |   role_ids |   salary | contract_id    |   bank_details |   is_archived | archived_at   |   is_manager | password   |
|-----:|:--------|:---------------|------------:|-----------:|---------:|:---------------|---------------:|--------------:|:--------------|-------------:|:-----------|
|  111 | Hila    | 050-0000001    |           1 |          2 |     8000 | 111-2025-06-01 |            123 |             0 | NULL          |            0 | pass1      |
|  112 | Yarden  | 050-0000002    |           1 |          2 |     7800 | 112-2025-06-01 |            456 |             0 | NULL          |            0 | pass2      |
|  113 | Charlie | 050-0000003    |           1 |          3 |     7500 | 113-2025-06-01 |            789 |             0 | NULL          |            0 | pass3      |
|  114 | Dana    | 050-0000004    |           1 |          4 |     7200 | 114-2025-06-01 |            101 |             0 | NULL          |            0 | pass4      |
|  117 | David   | 050-0000007    |           1 |          1 |     9000 | 117-2025-06-01 |            303 |             0 | NULL          |            0 | pass7      |
|  118 | Ben     | 050-0000008    |           1 |          5 |     7700 | 118-2025-06-01 |            505 |             0 | NULL          |            0 | pass8      |
|  119 | Tal     | 0500000009     |           1 |          6 |     9500 | 119-2025-06-01 |            909 |             0 | NULL          |            0 | pass9      |
|  110 | Ido     | 0500000010     |           1 |          7 |     9500 | 110-2025-06-01 |            909 |             0 | NULL          |            0 | pass10     |
|  120 | assaf   | 0500000011     |           1 |          7 |     9600 | 110-2025-06-01 |            909 |             0 | NULL          |            0 | pass11     |

### employee_contracts
| id             |   employee_id | start_date   |   free_days |   sickness_days |   monthly_work_hours | social_contributions   | advanced_study_fund   |   salary | archived_at   |   is_archived |
|:---------------|--------------:|:-------------|------------:|----------------:|---------------------:|:-----------------------|:----------------------|---------:|:--------------|--------------:|
| 111-2025-06-01 |           111 | 2025-06-01   |          12 |               5 |                  160 | Basic                  | Standard              |     8000 | NULL          |             0 |
| 112-2025-06-01 |           112 | 2025-06-01   |          12 |               5 |                  160 | Basic                  | Standard              |     7800 | NULL          |             0 |
| 113-2025-06-01 |           113 | 2025-06-01   |          10 |               4 |                  160 | Basic                  | None                  |     7500 | NULL          |             0 |
| 114-2025-06-01 |           114 | 2025-06-01   |          10 |               3 |                  160 | Basic                  | None                  |     7200 | NULL          |             0 |
| 115-2025-06-01 |           115 | 2025-06-01   |          10 |               3 |                  160 | Standard               | Standard              |     7900 | NULL          |             0 |
| 116-2025-06-01 |           116 | 2025-06-01   |          12 |               5 |                  160 | Basic                  | Standard              |     7600 | NULL          |             0 |
| 117-2025-06-01 |           117 | 2025-06-01   |          12 |               5 |                  160 | Basic                  | Standard              |     9000 | NULL          |             0 |
| 118-2025-06-01 |           118 | 2025-06-01   |          10 |               3 |                  160 | Standard               | Standard              |     7700 | NULL          |             0 |
| 119-2025-06-01 |           119 | 2025-06-01   |          12 |               5 |                  160 | Standard               | Standard              |     9500 | NULL          |             0 |
| 110-2025-06-01 |           110 | 2025-06-01   |          12 |               5 |                  160 | Standard               | Standard              |     9500 | NULL          |             0 |
| 120-2025-06-01 |           120 | 2025-06-01   |          12 |               5 |                  160 | Standard               | Standard              |     9600 | NULL          |             0 |

### shifts
| id    | date       | start_time   | end_time   | type    |   required_roles_csv | assignments_csv   |   shift_manager_id | archived_at   |   is_archived |
|:------|:-----------|:-------------|:-----------|:--------|---------------------:|:------------------|-------------------:|:--------------|--------------:|
| eve_2 | 2025-06-03 | 14:00        | 21:00      | Evening |                    4 | NULL              |                117 | NULL          |             0 |

### shift_assignments
| id         |   employee_id | shift_id   |   role_id | archived_at   |   is_archived |
|:-----------|--------------:|:-----------|----------:|:--------------|--------------:|
| 111morn_02 |           111 | morn_0     |         2 | NULL          |             0 |
| 113morn_03 |           113 | morn_0     |         3 | NULL          |             0 |
| 118morn_05 |           118 | morn_0     |         5 | NULL          |             0 |
| 117morn_01 |           117 | morn_0     |         1 | NULL          |             0 |
| 114eve_04  |           114 | eve_0      |         4 | NULL          |             0 |
| 117eve_01  |           117 | eve_0      |         1 | NULL          |             0 |
| 112morn_12 |           112 | morn_1     |         2 | NULL          |             0 |
| 115morn_13 |           115 | morn_1     |         3 | NULL          |             0 |
| 118morn_15 |           118 | morn_1     |         5 | NULL          |             0 |
| 117morn_11 |           117 | morn_1     |         1 | NULL          |             0 |
| 116eve_14  |           116 | eve_1      |         4 | NULL          |             0 |
| 117eve_11  |           117 | eve_1      |         1 | NULL          |             0 |
| 110eve_16  |           110 | eve_0      |         7 | NULL          |             0 |
| 120eve_17  |           120 | eve_0      |         7 | NULL          |             0 |

### weekly_preferences
|   employee_id | preferred_shift_ids_csv   | week_start_date   | created_at   | last_modified   | status    | notes           |   employee_id_simple |
|--------------:|:--------------------------|:------------------|:-------------|:----------------|:----------|:----------------|---------------------:|
|           111 | morn_0,morn_2,morn_4      | 2025-06-01        | 2025-06-01   | 2025-06-01      | SUBMITTED | Prefer mornings |                  111 |
|           112 | morn_1,morn_3,morn_5      | 2025-06-01        | 2025-06-01   | 2025-06-01      | DRAFT     |                 |                  112 |
|           114 | eve_0,eve_2,eve_4         | 2025-06-01        | 2025-06-01   | 2025-06-01      | APPROVED  | Evening driver  |                  114 |

### EmployeeRoles
|   employee_id |   role_id |
|--------------:|----------:|
|           111 |         2 |
|           112 |         2 |
|           113 |         3 |
|           114 |         4 |
|           115 |         3 |
|           115 |         4 |
|           116 |         2 |
|           116 |         4 |
|           117 |         1 |
|           118 |         5 |
|           119 |         6 |
|           110 |         7 |
|           120 |         7 |

### branches
| id    | name    | address   | employee_ids              |
|:------|:--------|:----------|:--------------------------|
| Nike  | example | example   | 123456789,115,116,117,118 |
| Ikea  | example | example   | 111,112                   |
| Mango | example | example   | 113,120                   |
| Zara  | example | example   | 110,114                   |

### trucks
|   plateNumber | model    |   netWeight |   maxWeight | licenseType   |
|--------------:|:---------|------------:|------------:|:--------------|
|      11111111 | Audi     |         350 |         800 | C             |
|      12345678 | Mercedes |         200 |         920 | A             |

### shipmentAreas
|   id | name   |
|-----:|:-------|
|  123 | Negev  |
|  111 | Galil  |
|  222 | Center |

### sites
| name   | address    |   phoneNumber | contactPersonName   |   shipmentAreaId |
|:-------|:-----------|--------------:|:--------------------|-----------------:|
| Ikea   | Beer sheva |     086312589 | Ido                 |              111 |
| Nike   | Tel aviv   |     081111111 | Tal                 |              123 |
| Mango  | Sderot     |     032222222 | Jordi               |              222 |
| Zara   | Beer sheva |     031234567 | Hila                |              111 |

### transportations
|   id | date       | departureTime   |   truckPlateNumber | driverName   | originName   |   originShipmentAreaId |   succeeded | accident              |
|-----:|:-----------|:----------------|-------------------:|:-------------|:-------------|-----------------------:|------------:|:----------------------|
| 1234 | 2025-06-01 | 19:00           |           11111111 | Dana         | Ikea         |                    111 |           1 | No accidents reported |
| 4444 | 2025-06-01 | 11:00           |           12345678 | Eli          | Mango        |                    222 |           0 | No accidents reported |

### items
|   itemsDocumentId |   itemId | name   |   quantity |   weight |
|------------------:|---------:|:-------|-----------:|---------:|
|              4444 |        1 | milk   |          2 |        1 |
|              4444 |        2 | bread  |          3 |        2 |
|              5555 |        3 | coffee |          1 |        1 |
|              5555 |        4 | water  |          1 |        4 |

### ItemsDocuments
|   id | destinationName   |   shipmentAreaId | arrivalTime   |   transportationId |
|-----:|:------------------|-----------------:|:--------------|-------------------:|
| 4444 | Ikea              |              111 | 12:00         |               1234 |
| 5555 | Mango             |              222 | 13:00         |               4444 |

### drivers
|   employee_id | employee_name   | licenseType   |
|--------------:|:----------------|:--------------|
|           114 | Dana            | A             |
|           115 | Eli             | B             |
|           116 | Snir            | C             |
