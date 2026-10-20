package com.saintgits.sgs.dao;

import com.saintgits.sgs.model.Student;
import com.saintgits.sgs.util.DBConnection;
import com.saintgits.sgs.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    private static final String SELECT_BASE =
        "SELECT s.student_id, s.first_name, s.last_name, s.admission_no, s.email, " +
        "s.course_id, c.cname, s.department_id, d.dname, s.joining_year, " +
        "s.graduation_year, s.username, s.password_hash, s.created_at " +
        "FROM student s " +
        "JOIN course c ON s.course_id = c.course_id " +
        "JOIN department d ON s.department_id = d.department_id ";

    private Student mapRow(ResultSet rs) throws SQLException {
        Student st = new Student();
        st.setStudentId(rs.getInt("student_id"));
        st.setFirstName(rs.getString("first_name"));
        st.setLastName(rs.getString("last_name"));
        st.setAdmissionNo(rs.getString("admission_no"));
        st.setEmail(rs.getString("email"));
        st.setCourseId(rs.getInt("course_id"));
        st.setCourseName(rs.getString("cname"));
        st.setDepartmentId(rs.getInt("department_id"));
        st.setDepartmentName(rs.getString("dname"));
        st.setJoiningYear(rs.getInt("joining_year"));
        st.setGraduationYear(rs.getInt("graduation_year"));
        st.setUsername(rs.getString("username"));
        st.setPasswordHash(rs.getString("password_hash"));
        st.setCreatedAt(rs.getTimestamp("created_at"));
        return st;
    }

    public Student authenticate(String username, String password) throws SQLException {
        String sql = SELECT_BASE + "WHERE s.username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student s = mapRow(rs);
                    if (PasswordUtil.verify(password, s.getPasswordHash())) return s;
                }
            }
        }
        return null;
    }

    public boolean register(Student s, String plainPassword) throws SQLException {
        String sql = "INSERT INTO student (first_name, last_name, admission_no, email, " +
                     "course_id, department_id, joining_year, graduation_year, username, password_hash) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getFirstName());
            ps.setString(2, s.getLastName());
            ps.setString(3, s.getAdmissionNo());
            ps.setString(4, s.getEmail());
            ps.setInt   (5, s.getCourseId());
            ps.setInt   (6, s.getDepartmentId());
            ps.setInt   (7, s.getJoiningYear());
            ps.setInt   (8, s.getGraduationYear());
            ps.setString(9, s.getUsername());
            ps.setString(10, PasswordUtil.hash(plainPassword));
            return ps.executeUpdate() > 0;
        }
    }

    public Student findById(int studentId) throws SQLException {
        String sql = SELECT_BASE + "WHERE s.student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Student> findAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY s.student_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean existsByAdmissionNo(String admissionNo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM student WHERE admission_no = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, admissionNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean delete(int studentId) throws SQLException {
        String sql = "DELETE FROM student WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            return ps.executeUpdate() > 0;
        }
    }
}
