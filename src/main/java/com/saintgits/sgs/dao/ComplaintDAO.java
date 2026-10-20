package com.saintgits.sgs.dao;

import com.saintgits.sgs.model.Complaint;
import com.saintgits.sgs.model.Reply;
import com.saintgits.sgs.util.DBConnection;

import java.sql.*;
import java.util.*;

public class ComplaintDAO {

    private static final String SELECT_COMPLAINT_BASE =
        "SELECT c.complaint_id, c.student_id, " +
        "CONCAT(st.first_name, ' ', st.last_name) AS student_name, " +
        "c.complaint_date, c.subject, c.description, c.status, " +
        "c.assigned_staff_id, " +
        "CONCAT(sf.first_name, ' ', sf.last_name) AS assigned_staff_name, " +
        "c.created_at, c.updated_at " +
        "FROM complaint c " +
        "JOIN student st ON c.student_id = st.student_id " +
        "LEFT JOIN staff sf ON c.assigned_staff_id = sf.staff_id ";

    private Complaint mapComplaint(ResultSet rs) throws SQLException {
        Complaint c = new Complaint();
        c.setComplaintId(rs.getInt("complaint_id"));
        c.setStudentId(rs.getInt("student_id"));
        c.setStudentName(rs.getString("student_name"));
        c.setComplaintDate(rs.getDate("complaint_date"));
        c.setSubject(rs.getString("subject"));
        c.setDescription(rs.getString("description"));
        c.setStatus(rs.getString("status"));
        int sid = rs.getInt("assigned_staff_id");
        c.setAssignedStaffId(rs.wasNull() ? null : sid);
        c.setAssignedStaffName(rs.getString("assigned_staff_name"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setUpdatedAt(rs.getTimestamp("updated_at"));
        return c;
    }

    public boolean submit(Complaint c) throws SQLException {
        // Prevent duplicate: same student + subject + date
        String chk = "SELECT COUNT(*) FROM complaint WHERE student_id=? AND subject=? AND complaint_date=?";
        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(chk)) {
                ps.setInt   (1, c.getStudentId());
                ps.setString(2, c.getSubject());
                ps.setDate  (3, c.getComplaintDate());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) return false; // duplicate
                }
            }
            String sql = "INSERT INTO complaint (student_id, complaint_date, subject, description) VALUES (?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt   (1, c.getStudentId());
                ps.setDate  (2, c.getComplaintDate());
                ps.setString(3, c.getSubject());
                ps.setString(4, c.getDescription());
                return ps.executeUpdate() > 0;
            }
        }
    }

    public List<Complaint> findAll() throws SQLException {
        List<Complaint> list = new ArrayList<>();
        String sql = SELECT_COMPLAINT_BASE + "ORDER BY c.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapComplaint(rs));
        }
        return list;
    }

    public List<Complaint> findByStatus(String status) throws SQLException {
        List<Complaint> list = new ArrayList<>();
        String sql = SELECT_COMPLAINT_BASE + "WHERE c.status = ? ORDER BY c.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapComplaint(rs));
            }
        }
        return list;
    }

    public List<Complaint> findByStudentId(int studentId) throws SQLException {
        List<Complaint> list = new ArrayList<>();
        String sql = SELECT_COMPLAINT_BASE + "WHERE c.student_id = ? ORDER BY c.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapComplaint(rs));
            }
        }
        return list;
    }

    public List<Complaint> findByAssignedStaff(int staffId) throws SQLException {
        List<Complaint> list = new ArrayList<>();
        String sql = SELECT_COMPLAINT_BASE + "WHERE c.assigned_staff_id = ? ORDER BY c.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapComplaint(rs));
            }
        }
        return list;
    }

    public Complaint findById(int complaintId) throws SQLException {
        String sql = SELECT_COMPLAINT_BASE + "WHERE c.complaint_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, complaintId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapComplaint(rs);
            }
        }
        return null;
    }

    public boolean updateStatusAndAssign(int complaintId, String status, Integer assignedStaffId) throws SQLException {
        String sql = "UPDATE complaint SET status=?, assigned_staff_id=? WHERE complaint_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            if (assignedStaffId == null) ps.setNull(2, Types.INTEGER);
            else                         ps.setInt (2, assignedStaffId);
            ps.setInt(3, complaintId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean editComplaint(int complaintId, int studentId, String subject, String description,
                                  java.sql.Date date) throws SQLException {
        // Only allow edit if still Pending
        String sql = "UPDATE complaint SET subject=?, description=?, complaint_date=? " +
                     "WHERE complaint_id=? AND student_id=? AND status='Pending'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, subject);
            ps.setString(2, description);
            ps.setDate  (3, date);
            ps.setInt   (4, complaintId);
            ps.setInt   (5, studentId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteComplaint(int complaintId, int studentId) throws SQLException {
        // Only allow delete if still Pending
        String sql = "DELETE FROM complaint WHERE complaint_id=? AND student_id=? AND status='Pending'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, complaintId);
            ps.setInt(2, studentId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean addReply(Reply reply) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                String replySQL = "INSERT INTO complaint_reply (complaint_id, staff_id, reply_text, status_after) VALUES (?,?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(replySQL)) {
                    ps.setInt   (1, reply.getComplaintId());
                    ps.setInt   (2, reply.getStaffId());
                    ps.setString(3, reply.getReplyText());
                    ps.setString(4, reply.getStatusAfter());
                    ps.executeUpdate();
                }
                String updateSQL = "UPDATE complaint SET status=? WHERE complaint_id=?";
                try (PreparedStatement ps = con.prepareStatement(updateSQL)) {
                    ps.setString(1, reply.getStatusAfter());
                    ps.setInt   (2, reply.getComplaintId());
                    ps.executeUpdate();
                }
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public List<Reply> findRepliesByComplaint(int complaintId) throws SQLException {
        List<Reply> list = new ArrayList<>();
        String sql = "SELECT r.reply_id, r.complaint_id, r.staff_id, " +
                     "CONCAT(s.first_name, ' ', s.last_name) AS staff_name, " +
                     "r.reply_text, r.status_after, r.replied_at " +
                     "FROM complaint_reply r " +
                     "JOIN staff s ON r.staff_id = s.staff_id " +
                     "WHERE r.complaint_id = ? ORDER BY r.replied_at ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, complaintId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reply r = new Reply();
                    r.setReplyId(rs.getInt("reply_id"));
                    r.setComplaintId(rs.getInt("complaint_id"));
                    r.setStaffId(rs.getInt("staff_id"));
                    r.setStaffName(rs.getString("staff_name"));
                    r.setReplyText(rs.getString("reply_text"));
                    r.setStatusAfter(rs.getString("status_after"));
                    r.setRepliedAt(rs.getTimestamp("replied_at"));
                    list.add(r);
                }
            }
        }
        return list;
    }

    /** Returns counts grouped by status for reports. */
    public Map<String, Integer> countByStatus() throws SQLException {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT status, COUNT(*) AS cnt FROM complaint GROUP BY status";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("status"), rs.getInt("cnt"));
            }
        }
        return map;
    }

    /** Returns counts by department for reports. */
    public Map<String, Integer> countByDepartment() throws SQLException {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT d.dname, COUNT(c.complaint_id) AS cnt " +
                     "FROM department d " +
                     "LEFT JOIN student s ON s.department_id = d.department_id " +
                     "LEFT JOIN complaint c ON c.student_id = s.student_id " +
                     "GROUP BY d.dname ORDER BY cnt DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("dname"), rs.getInt("cnt"));
            }
        }
        return map;
    }
}
