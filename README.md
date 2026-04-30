# 📱 Habit Tracker App

A full-stack habit tracking system built with **ASP.NET Core Web API**, **Android (Kotlin)**, and **Azure Cloud**.
The application allows users to create habits, track daily completion, and monitor progress.

---

## 🚀 Features

* ✅ Create habits
* 📅 Track daily completion
* 📊 View progress (completed vs total)
* 🗑️ Delete habits
* ☁️ Cloud-based API and database
* 🔁 CI/CD with GitHub Actions
* 🧪 End-to-end testing using Espresso

---

## 🧱 System Architecture

```plaintext
Android App (Kotlin)
        ↓
ASP.NET Core Web API (Azure App Service)
        ↓
Azure SQL Database
```

---

## 🔧 Technologies Used

### Backend

* ASP.NET Core Web API
* Entity Framework Core
* SQL Server

### Frontend (Android)

* Kotlin
* RecyclerView
* Retrofit

### Cloud & DevOps

* Microsoft Azure (App Service + SQL Database)
* GitHub Actions (CI/CD)

### Testing

* Espresso (Android UI testing)

---

## 🌐 API Endpoints

| Method | Endpoint           | Description            |
| ------ | ------------------ | ---------------------- |
| GET    | `/api/habits`      | Get all habits         |
| POST   | `/api/habits`      | Create a new habit     |
| DELETE | `/api/habits/{id}` | Delete a habit         |
| GET    | `/api/habitlogs`   | Get habit logs         |
| POST   | `/api/habitlogs`   | Track habit completion |

---

## 🔗 API URL

```plaintext
https://habittrack-api-b7atdygbd0dhc5dq.uksouth-01.azurewebsites.net/
```

Swagger UI:

```plaintext
https://habittrack-api-b7atdygbd0dhc5dq.uksouth-01.azurewebsites.net/swagger
```

---

## 📱 Android App

### Features

* Displays list of habits
* Checkbox to mark habits complete
* Add new habits
* Delete habits
* Shows daily progress

### API Integration

Uses Retrofit to connect to the Azure-hosted API.

---

## 🗄️ Database Schema

### Habits Table

* Id (Primary Key)
* Name
* Description

### HabitLogs Table

* Id (Primary Key)
* Date
* Completed
* HabitId (Foreign Key)

### Relationship

* One Habit → Many HabitLogs

---

## ☁️ Deployment

The API is deployed using **Azure App Service**, and the database is hosted on **Azure SQL Database**.

### CI/CD Pipeline

* GitHub Actions automatically builds and deploys the API
* Triggered on push to the `main` branch

---

## 🧪 Testing

End-to-end testing is implemented using **Espresso**.

### Tests include:

* App launch test
* UI visibility test
* User interaction (checkbox click)
<img width="940" height="229" alt="image" src="https://github.com/user-attachments/assets/91b160fc-d134-4ab9-863b-a7bb5c10fac7" />

Example:

```kotlin
onView(withId(R.id.recyclerView))
    .check(matches(isDisplayed()))
```

---

## 📸 Screenshots

<img width="940" height="535" alt="image" src="https://github.com/user-attachments/assets/9683d19a-603a-4c17-a0cf-912cb326f0f5" />
<img width="940" height="539" alt="image" src="https://github.com/user-attachments/assets/052cd4c5-ee0c-463c-b02b-36ff37d2a636" />
<img width="940" height="199" alt="image" src="https://github.com/user-attachments/assets/76898d3a-7e32-4a97-bf73-fbb7a9f5294e" />
<img width="940" height="294" alt="image" src="https://github.com/user-attachments/assets/3b9b614d-3298-4d1e-a33e-9282536e47e6" />
<img width="940" height="376" alt="image" src="https://github.com/user-attachments/assets/ffd4a6de-9624-4d9b-ac3d-d33f6827495a" />


* App UI
* Swagger API
* Azure deployment
* Database setup
* GitHub Actions pipeline

---

## 👤 Author

* Name: Tobiloba

---

## 📌 Notes

* The system is fully cloud-based
* Uses RESTful API design
* Demonstrates full-stack development and DevOps integration

---
