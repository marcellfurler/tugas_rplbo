package org.kehadiransiswa.managers;

import org.kehadiransiswa.data.AttendanceRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceManager {
    private Connection connection;

    public AttendanceManager(Connection connection) {
        this.connection = connection;
    }

    // Add methods for attendance recording and reporting
    public boolean recordAttendance(int classId, int userId, String status) {
        String query = "INSERT INTO attendance_records (class_id, user_id, status) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, classId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, status);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Attendance recorded successfully if rows were inserted
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
            return false;
        }
    }

    public List<AttendanceRecord> getAttendanceReport(int classId) {
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        String query = "SELECT * FROM attendance_records WHERE class_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, classId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int attendanceId = resultSet.getInt("id");
                int studentId = resultSet.getInt("user_id");
                String status = resultSet.getString("status");
                AttendanceRecord attendanceRecord = new AttendanceRecord(attendanceId, classId, studentId, status);
                attendanceRecords.add(attendanceRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query error
        }
        return attendanceRecords;
    }
}
