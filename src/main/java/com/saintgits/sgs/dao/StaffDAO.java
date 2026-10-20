package com.saintgits.sgs.dao;

import com.saintgits.sgs.model.Staff;
import com.saintgits.sgs.util.DBConnection;
import com.saintgits.sgs.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    private static final String SELECT_BASE =
        "SELECT s.staff_id, s.staff_code, s.first_name, s.last_name, s.email, s.phone, " +
        "s.department_id, d.dname, s.designation, s.username, s.password_hash, " +
        "s.is_committee_member, s.created_at " +
        "FROM staff s " +
        "JOIN department d ON s.department_id = d.department_id ";

    private Staff mapRow(ResultSet rs) throws SQLException {
        Staff st = new Staff();
        st.setStaffId(rs.getInt("staff_id"));
        st.setStaffCode(rs.getString("staff_code"));
        st.setFirstName(rs.getString("first_name"));
        st.setLastName(rs.getString("last_name"));
        st.setEmail(rs.getString("email"));
        st.setPhone(rs.getString("phone"));
        st.setDepartmentId(rs.getInt("department_id"));
        st.setDepartmentName(rs.getString("dname"));
        st.setDesignation(rs.getString("designation"));
        st.setUsername(rs.getString("username"));
        st.setPasswordHash(rs.getString("password_hash"));
        st.setCommitteeMember(rs.getBoolean("is_committee_member"));
        st.setCreatedAt(rs.getTimestamp("created_at"));
        return st;
    }

    public Staff authenticate(String username, String password) throws SQLException {
        String sql = SELECT_BASE + "WHERE s.username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Staff s = mapRow(rs);
                    if (PasswordUtil.verify(password, s.getPasswordHash())) return s;
                }
            }
        }
        return null;
    }

    public boolean register(Staff s, String plainPassword) throws SQLException {
        String sql = "INSERT INTO staff (staff_code, first_name, last_name, email, phone, " +
                     "department_id, designation, username, password_hash) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getStaffCode());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setInt   (6, s.getDepartmentId());
            ps.setString(7, s.getDesignation());
            ps.setString(8, s.getUsername());
            ps.setString(9, PasswordUtil.hash(plainPassword));
            return ps.executeUpdate() > 0;
        }
    }

    public Staff findById(int staffId) throws SQLException {
        String sql = SELECT_BASE + "WHERE s.staff_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Staff> findAll() throws SQLException {
        List<Staff> list = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY s.staff_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Staff> findCommitteeMembers() throws SQLException {
        List<Staff> list = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE s.is_committee_member = 1 ORDER BY s.first_name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean setCommitteeMember(int staffId, boolean isMember) throws SQLException {
        String sql = "UPDATE staff SET is_committee_member = ? WHERE staff_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, isMember);
            ps.setInt    (2, staffId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM staff WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM staff WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean existsByStaffCode(String staffCode) throws SQLException {
        String sql = "SELECT COUNT(*) FROM staff WHERE staff_code = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, staffCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean delete(int staffId) throws SQLException {
        String sql = "DELETE FROM staff WHERE staff_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            return ps.executeUpdate() > 0;
        }
    }
}
