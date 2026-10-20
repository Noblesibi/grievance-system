package com.saintgits.sgs.dao;

import com.saintgits.sgs.model.Admin;
import com.saintgits.sgs.util.DBConnection;
import com.saintgits.sgs.util.PasswordUtil;

import java.sql.*;

public class AdminDAO {

    /** Returns Admin if credentials match, else null. */
    public Admin authenticate(String username, String password) throws SQLException {
        String sql = "SELECT admin_id, username, password_hash, full_name FROM admin WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password_hash");
                    if (PasswordUtil.verify(password, hash)) {
                        Admin a = new Admin();
                        a.setAdminId(rs.getInt("admin_id"));
                        a.setUsername(rs.getString("username"));
                        a.setPasswordHash(hash);
                        a.setFullName(rs.getString("full_name"));
                        return a;
                    }
                }
            }
        }
        return null;
    }

    /** Returns live statistics for the admin dashboard. */
    public int[] getDashboardStats() throws SQLException {
        // [0]=totalStudents, [1]=totalComplaints, [2]=inProgress, [3]=resolved
        int[] stats = new int[4];
        String q1 = "SELECT COUNT(*) FROM student";
        String q2 = "SELECT COUNT(*) FROM complaint";
        String q3 = "SELECT COUNT(*) FROM complaint WHERE status = 'In Progress'";
        String q4 = "SELECT COUNT(*) FROM complaint WHERE status = 'Resolved'";
        try (Connection con = DBConnection.getConnection()) {
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(q1)) {
                if (rs.next()) stats[0] = rs.getInt(1);
            }
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(q2)) {
                if (rs.next()) stats[1] = rs.getInt(1);
            }
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(q3)) {
                if (rs.next()) stats[2] = rs.getInt(1);
            }
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(q4)) {
                if (rs.next()) stats[3] = rs.getInt(1);
            }
        }
        return stats;
    }
}
