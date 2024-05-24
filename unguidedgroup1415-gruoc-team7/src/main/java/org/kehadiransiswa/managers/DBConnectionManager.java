package org.kehadiransiswa.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*

The design pattern used in the DBConnectionManager class is the Singleton Pattern.

Singleton Pattern:
The Singleton Pattern ensures that a class has only one instance and provides a global point of access to that instance.
It is commonly used when you want to control access to resources, manage global state, or ensure that a class has a single point of access.

In the DBConnectionManager class:
The constructor is marked as private to prevent external instantiation of the class.
The getConnection() method is static, allowing access to the single instance of the class without needing to create an object.
The getConnection() method checks if the connection object is null. If it is null, it establishes a new database connection.
Otherwise, it returns the existing connection.
The closeConnection() method allows closing the database connection when it's no longer needed.
Here's how the Singleton Pattern is applied in the DBConnectionManager class:

Private Constructor: By making the constructor private, we prevent external classes from creating instances of DBConnectionManager.
This ensures that only one instance of DBConnectionManager exists.
Static Method for Access: The getConnection() method is static, allowing access to the single instance of DBConnectionManager
without needing to create an object. This method follows the lazy initialization approach, creating the connection only
when it is requested for the first time.
Singleton Instance: The connection variable is static, ensuring that there is only one instance
of the database connection shared across the application.

By using the Singleton Pattern, we ensure that there is only one instance of the DBConnectionManager class throughout the application,
and all parts of the application can access the same database connection. This helps in managing resources efficiently
and maintaining a consistent state across the application.
* */
public class DBConnectionManager {
    private static final String DB_URL = "jdbc:sqlite:attendance.db";
    private static Connection connection;

    private DBConnectionManager() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection error
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection closure error
            }
        }
    }

    public static void createTables() {
        // Create database tables if they don't exist
        // Implement this method to create tables for users, courses, classes, and attendance records
        String userTableSql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL,"
                + "email TEXT NOT NULL,"
                + "role TEXT NOT NULL"
                + ")";
        String courseTableSql = "CREATE TABLE IF NOT EXISTS courses ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT NOT NULL,"
                + "description TEXT"
                + ")";
        String classTableSql = "CREATE TABLE IF NOT EXISTS classes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "course_id INTEGER NOT NULL,"
                + "date TEXT NOT NULL,"
                + "time TEXT NOT NULL,"
                + "duration TEXT,"
                + "location TEXT,"
                + "FOREIGN KEY (course_id) REFERENCES courses(id)"
                + ")";
        String attendanceTableSql = "CREATE TABLE IF NOT EXISTS attendance_records ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "class_id INTEGER NOT NULL,"
                + "user_id INTEGER NOT NULL,"
                + "status TEXT,"
                + "FOREIGN KEY (class_id) REFERENCES classes(id),"
                + "FOREIGN KEY (user_id) REFERENCES users(id)"
                + ")";
        // Add more table creation statements as needed

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(userTableSql);
            stmt.execute(courseTableSql);
            stmt.execute(classTableSql);
            stmt.execute(attendanceTableSql);
            // Execute more table creation statements as needed
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle table creation error
        }
    }
}
