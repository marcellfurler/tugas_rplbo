package org.kehadiransiswa.managers;

import org.kehadiransiswa.data.ClassRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassManager {
    private Connection connection;

    public ClassManager(Connection connection) {
        this.connection = connection;
    }

    // Add methods for class management (schedule, cancel, update)

    public boolean scheduleClass(int courseId, String date, String time, String duration, String location){
        String query = "INSERT INTO classes (course_id, date, time, duration, location) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, time);
            preparedStatement.setString(4, duration);
            preparedStatement.setString(5, location);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Class scheduled successfully if rows were inserted
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
            return false;
        }
    }

    public boolean cancelClass(int classId) {
        String query = "DELETE FROM classes WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, classId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Class canceled successfully if rows were affected
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
            return false;
        }
    }

    public boolean updateClass(int classId, int courseId, String newDate, String newTime, String newDuration, String newLocation)  {
        String query = "UPDATE classes SET course_id = ?, date = ?, time = ?, duration = ?, location = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.setString(2, newDate);
            preparedStatement.setString(3, newTime);
            preparedStatement.setString(4, newDuration);
            preparedStatement.setString(5, newLocation);
            preparedStatement.setInt(6, classId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Class updated successfully if rows were affected
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
            return false;
        }
    }

    public List<ClassRoom> getAllClasses() {
        List<ClassRoom> classes = new ArrayList<>();
        String query = "SELECT * FROM classes";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int courseId = resultSet.getInt("course_id");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                String duration = resultSet.getString("duration");
                String location = resultSet.getString("location");
                ClassRoom scheduledClass = new ClassRoom(id, courseId, date, time, duration, location);
                classes.add(scheduledClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
        }
        return classes;
    }
}
