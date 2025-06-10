# Supermarket Management System – Inventory & Suppliers Module

📦 This project is a partial system for managing **inventory** and **suppliers** in a supermarket, as part of a larger enterprise system.

---

## 👥 Team Members

- Almog Saadi – 318673571 
- Shay Silversmith – 206579674  
- Elad Tussi – 207827304  
- Tal Cohen – 319089769

---

## 🧾 Overview

The system supports full management of:
- Products and stock
- Items, expiration dates, and defects
- Suppliers and contact persons
- Agreements with delivery days
- Orders management
- Reports generation (inventory, expired, defective)
- Promotions and supplier discounts

---

## 🛠️ Technologies Used

- Language: **Java**
- IDE: **IntelliJ IDEA Ultimate**
- Database: **PostgreSQL**
- Data Access: **DAO pattern**
- Utilities: `java.time`, `Scanner`, `Collections`, `UUID`

---

## 🚀 Run Instructions

1. **Ensure PostgreSQL is installed and running.**
2. **Place the `adss2025_v03.jar` file in your working directory.**
3. **Run the system via CLI with:**

```
java -jar adss2025_v03.jar CLI
```

4. **When prompted:**
   - Type `Y` to load sample data from the database.
   - Type `N` to start with an empty system.

5. **Login with a valid username from the DB.**
   - No real authentication mechanism (no roles enforced yet).

---

## 📁 Project Structure

```
src/
├── Domain/               ← Business logic: products, items, orders, agreements
├── Presentation/         ← CLI menus
├── dao/                  ← Database access layer (PostgreSQL)
├── Main.java             ← Main entry point
```

---

## 🧩 Main Features

### Inventory Module:
- Add/remove products and items
- Mark items as defective or expired
- Update prices, discounts, promotions
- Show detailed item information
- Generate category/expiration/defect reports

### Supplier Module:
- Manage suppliers and contacts
- Create and update agreements
- Define delivery days
- Place orders
- View and update existing agreements

---

## 🧪 Sample Data

- Available via `"Y"` prompt on startup
- Includes sample products, suppliers, and agreements

---

## 🔐 Access Control

Currently, **no access control** is enforced. All users have access to all features.

---

## 🌐 GitHub Repository

🔗 https://github.com/ADSSGroupH/ADSS_Group_H

---
---

## 🧭 Usage Instructions (Step-by-Step)

### Main Menu

After successful login, the following options are displayed:

```
1. Manage Suppliers
2. Manage Agreements
3. Manage Orders
4. Manage Product
5. Manage Item
6. Manage Discount
7. Manage Report
0. Exit
```

Each menu leads to additional sub-menus, described below.

---

### 1. Manage Suppliers

**Options:**
- `1. Show all suppliers` – Lists all suppliers with full details.
- `2. Add new supplier` – Prompts for:
  - Supplier ID (String)
  - Name (String)
  - Delivery Address (String)
  - Bank Account (String)
  - Payment Method (Choose from list)
  - Contact People (multiple entries: name, phone, email)
- `3. Select supplier to view/edit` – Allows updating name, address, bank account, payment method, and contact list.
- `4. Add agreement` – Redirects to add agreements to an existing supplier.
- `5. View products in supplier agreements` – Displays items by supplier.

---

### 2. Manage Agreements

**Options:**
- `1. Show all agreements` – Lists agreements for each supplier.
- `2. Add new agreement` – Prompts for:
  - Agreement ID
  - Supplier ID
  - Delivery Days (e.g., MONDAY, WEDNESDAY)
  - Products (ID, Name, Catalog Number, Price, Discount, Min Qty)
- `3. Edit existing agreement` – Allows updating prices, discount, or removing products.

---

### 3. Manage Orders

**Options:**
- `1. Show all orders` – Displays every placed order.
- `2. Place new order` – Prompts for:
  - Supplier ID
  - Products and quantities to order
  - Order ID
- `3. View order details` – By Order ID
- `4. Update order` – Change items or status (PENDING/READY/COLLECTED)
- `5. Show orders by supplier` – Filter by supplier ID
- `6. Create order by best price` – (Disabled / not implemented)
- `7. Add a periodic order` – (Disabled / not implemented)

---

### 4. Manage Product

**Options:**
- `1. List all products in stock`
- `2. Add product`
- `3. Remove product`
- `4. Update minimum quantity`
- `5. Show product price`
- `6. Update product price`
- `7. View suppliers for product`

---

### 5. Manage Item

**Options:**
- `1. Add item` – Requires entering:
  - Item ID, Name, Product Name (must exist)
  - Classification: ID, Category, Subcategory, Size
  - Location (Store or Warehouse)
  - Expiration Date (yyyy-MM-dd)
- `2. Remove item`
- `3. Show item details`
- `4. Mark item as defective`
- `5. Mark item as expired`

---

### 6. Manage Discount

**Options:**
- `1. Add supplier discount`
- `2. Remove supplier discount`
- `3. Add promotion`
- `4. Remove promotion`

---

### 7. Manage Report

**Options:**
- `1. Inventory report by category`
- `2. Defective items report`
- `3. Expired items report`

---

Input is handled with validation and helpful prompts.
Invalid input (e.g., wrong date format, missing fields) will trigger appropriate error messages.

---

## 🧾 Input Field Types Per Action

| Action        | Field                     | Type      | Notes                                 |
|:--------------|:--------------------------|:----------|:--------------------------------------|
| Add Supplier  | Supplier ID               | String    | Must be unique                        |
|               | Name                      | String    | Required                              |
|               | Delivery Address          | String    | Required                              |
|               | Bank Account              | String    | Required                              |
|               | Payment Method            | Enum      | Selected from: CASH, CREDIT, TRANSFER |
|               | Contact Name              | String    | Required                              |
|               | Phone                     | String    | Required                              |
|               | Email                     | String    | Valid email format                    |
| Add Product   | Product ID                | String    | Must be unique                        |
|               | Name                      | String    | Required                              |
|               | Cost Price                | float     | >= 0                                  |
|               | Sale Price                | float     | >= cost price                         |
|               | Manufacturer              | String    | Required                              |
|               | Min Quantity              | int       | >= 0                                  |
| Add Item      | Item ID                   | String    | Must be unique                        |
|               | Item Name                 | String    | Required                              |
|               | Product Name              | String    | Must already exist                    |
|               | Classification ID         | String    | Must be unique                        |
|               | Category                  | String    | Required                              |
|               | Subcategory               | String    | Required                              |
|               | Size                      | float     | Positive number                       |
|               | Location                  | Enum      | WareHouse or Store                    |
|               | Expiration Date           | Date      | Format: yyyy-MM-dd                    |
| Add Agreement | Agreement ID              | String    | Must be unique                        |
|               | Supports Delivery?        | Boolean   | y/n                                   |
|               | Delivery Days             | Enum List | e.g., MONDAY, TUESDAY                 |
|               | Product ID                | String    | Must exist or will be created         |
|               | Catalog Number            | String    | Required                              |
|               | Price                     | float     | >= 0                                  |
|               | Discount                  | float     | 0–100                                 |
|               | Min Quantity for Discount | int       | >= 0                                  |
| Place Order   | Order ID                  | String    | Must be unique                        |
|               | Supplier ID               | String    | Must already exist                    |
|               | Product ID                | String    | From agreement                        |
|               | Quantity                  | int       | >= 1                                  |

---

## 🗄️ Initial Database Contents

### 🛒 Initial Products

| Product ID   | Name                 |   Cost Price |   Sale Price | Manufacturer   |   Min Quantity |
|:-------------|:---------------------|-------------:|-------------:|:---------------|---------------:|
| P1           | Milk                 |            2 |          3.5 | Tnuva          |             20 |
| P2           | Bread                |            1 |          2.5 | Angel          |             15 |
| P3           | ShaBBBBBBBBBBBBBmpoo |           10 |         15   | Head&Shoulders |              0 |

### 🚚 Initial Suppliers

| Supplier ID   | Name    |   Bank Account | Payment Method   | Address                  |
|:--------------|:--------|---------------:|:-----------------|:-------------------------|
| SUP1          | Tnuva   |            111 | BANK_TRANSFER    | 12 HaSadot St., Tel Aviv |
| SUP2          | Strauss |            222 | CREDIT_CARD      | 45 Milkman Rd., Haifa    |
| SUP3          | Osem    |            333 | CASH             | 78 Dough Ave., Jerusalem |

### 📦 Initial Items

| Item ID   | Name                  | Location   | Expiration Date   | Category   | Subcategory   |   Size | Product ID   | Defective   |
|:----------|:----------------------|:-----------|:------------------|:-----------|:--------------|-------:|:-------------|:------------|
| I1        | Fresh Milk Bottle     | WareHouse  | 2025-12-01        | Dairy      | Milk          |   1    | P1           | False       |
| I2        | Family Milk Bottle    | Store      | 2025-12-10        | Dairy      | Milk          |   1.5  | P1           | False       |
| I3        | Whole Wheat Bread     | Store      | 2025-11-25        | Bakery     | Bread         |   0.5  | P2           | False       |
| I4        | Sliced Bread          | WareHouse  | 2025-11-20        | Bakery     | Bread         |   0.75 | P2           | True        |
| I5        | Anti-Dandruff Shampoo | Store      | 2026-01-15        | Hygiene    | Shampoo       |   0.3  | P3           | False       |
| I6        | Travel Shampoo        | WareHouse  | 2026-02-28        | Hygiene    | Shampoo       |   0.1  | P3           | False       |