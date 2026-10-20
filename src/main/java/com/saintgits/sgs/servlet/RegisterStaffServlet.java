package com.saintgits.sgs.servlet;

import com.saintgits.sgs.dao.MasterDataDAO;
import com.saintgits.sgs.dao.StaffDAO;
import com.saintgits.sgs.model.Staff;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/RegisterStaffAction")
public class RegisterStaffServlet extends HttpServlet {

    private final StaffDAO      staffDAO      = new StaffDAO();
    private final MasterDataDAO masterDataDAO = new MasterDataDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String firstName   = trim(req.getParameter("firstName"));
        String lastName    = trim(req.getParameter("lastName"));
        String staffCode   = trim(req.getParameter("staffCode"));
        String email       = trim(req.getParameter("email"));
        String phone       = trim(req.getParameter("phone"));
        String deptIdStr   = trim(req.getParameter("departmentId"));
        String designation = trim(req.getParameter("designation"));
        String username    = trim(req.getParameter("username"));
        String password    = req.getParameter("password");
        String confirm     = req.getParameter("confirmPassword");

        StringBuilder err = new StringBuilder();
        if (firstName.isEmpty() || lastName.isEmpty()) err.append("Name is required. ");
        if (staffCode.isEmpty())                        err.append("Staff code required. ");
        if (!email.matches("^[\\w.%+\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,}$")) err.append("Invalid email. ");
        if (!phone.isEmpty() && !phone.matches("^\\d{10,15}$"))  err.append("Invalid phone number. ");
        if (username.isEmpty())                         err.append("Username required. ");
        if (password == null || password.length() < 8) err.append("Password must be ≥ 8 chars. ");
        if (!password.equals(confirm))                  err.append("Passwords do not match. ");

        int deptId = 0;
        try { deptId = Integer.parseInt(deptIdStr); } catch (NumberFormatException e) { err.append("Department required. "); }

        if (!designation.equals("HOD") && !designation.equals("Professor") && !designation.equals("Assistant Professor"))
            err.append("Invalid designation. ");

        if (err.length() > 0) { forwardWithError(req, resp, err.toString()); return; }

        try {
            if (staffDAO.existsByUsername(username))  { forwardWithError(req, resp, "Username already taken."); return; }
            if (staffDAO.existsByEmail(email))        { forwardWithError(req, resp, "Email already registered."); return; }
            if (staffDAO.existsByStaffCode(staffCode)){ forwardWithError(req, resp, "Staff code already registered."); return; }

            Staff s = new Staff();
            s.setFirstName(firstName); s.setLastName(lastName);
            s.setStaffCode(staffCode); s.setEmail(email); s.setPhone(phone);
            s.setDepartmentId(deptId); s.setDesignation(designation);
            s.setUsername(username);

            if (staffDAO.register(s, password)) {
                req.getSession().setAttribute("successMessage", "Registration successful! Please log in.");
                resp.sendRedirect(req.getContextPath() + "/Staff_Login.jsp");
            } else {
                forwardWithError(req, resp, "Registration failed. Please try again.");
            }
        } catch (Exception e) {
            forwardWithError(req, resp, "Server error: " + e.getMessage());
        }
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        try {
            req.setAttribute("errorMessage", msg);
            req.setAttribute("departments", masterDataDAO.findAllDepartments());
        } catch (Exception ignored) {}
        req.getRequestDispatcher("/Register_Staff.jsp").forward(req, resp);
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
