package com.saintgits.sgs.dao;

import com.saintgits.sgs.model.Course;
import com.saintgits.sgs.model.Department;
import com.saintgits.sgs.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MasterDataDAO {

    public List<Department> findAllDepartments() throws SQLException {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT department_id, dname FROM department ORDER BY dname";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Department(rs.getInt("department_id"), rs.getString("dname")));
            }
        }
        return list;
    }

    public List<Course> findAllCourses() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT course_id, cname FROM course ORDER BY cname";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Course(rs.getInt("course_id"), rs.getString("cname")));
            }
        }
        return list;
    }
}
