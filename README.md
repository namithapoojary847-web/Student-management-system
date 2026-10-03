# Student Management System

A desktop-based Student Management System developed using **Java Swing**, **JDBC**, and **Oracle Database**. The application is designed to simplify the management of student information, courses, enrollments, marks, and attendance through a user-friendly graphical interface.

## 📌 Project Overview

The Student Management System provides separate functionalities for administrators and students.

The system allows administrators to manage student and course information, enroll students into courses, and maintain academic records. Students can log in to view their results and attendance through an interactive dashboard.

## ✨ Features

### 👨‍💼 Admin Module
- Admin login
- Add and manage student records
- Add and manage courses
- Enroll students in courses
- View student enrollments
- Manage marks and academic information
- Manage attendance records

### 👨‍🎓 Student Module
- Student login
- View academic results
- View marks
- View attendance
- Visual representation of academic performance
- Attendance visualization

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| Java | Core application development |
| Java Swing | Graphical User Interface |
| JDBC | Database connectivity |
| Oracle Database 21c XE | Data storage |
| JFreeChart | Charts and data visualization |
| Eclipse IDE | Development environment |
| SQL Developer | Database management |
| ojdbc11 | Oracle JDBC Driver |

## 🗄️ Database

The application uses **Oracle Database 21c XE**.

The database contains tables for managing:

- Students
- Courses
- Enrollments
- Subjects
- Marks
- Attendance

## 📂 Project Structure

```text
Student-Management-System/
│
├── src/
│   └── com/
│       └── student/
│           └── management/
│               ├── util/
│               ├── MainSystem.java
│               ├── SMSDashboard.java
│               ├── AddStudentForm.java
│               ├── AddCourseFormGUI.java
│               ├── EnrollStudent.java
│               ├── ViewEnrollments.java
│               ├── LoginUI.java
│               ├── TestUI.java
│               ├── StudentResultUI.java
│               └── Attendance.java
│
└── README.md
