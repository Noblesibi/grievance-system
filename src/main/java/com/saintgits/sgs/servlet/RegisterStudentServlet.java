package com.saintgits.sgs.servlet;

import com.saintgits.sgs.dao.StudentDAO;
import com.saintgits.sgs.dao.MasterDataDAO;
import com.saintgits.sgs.model.Student;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/RegisterStudentAction")
public class RegisterStudentServlet extends HttpServlet {

    private final StudentDAO    studentDAO    = new StudentDAO();
    private final MasterDataDAO masterDataDAO = new MasterDataDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String firstName    = trim(req.getParameter("firstName"));
        String lastName     = trim(req.getParameter("lastName"));
        String admissionNo  = trim(req.getParameter("admissionNo"));
        String email        = trim(req.getParameter("email"));
        String courseIdStr  = trim(req.getParameter("courseId"));
        String deptIdStr    = trim(req.getParameter("departmentId"));
        String joinStr      = trim(req.getParameter("joiningYear"));
        String gradStr      = trim(req.getParameter("graduationYear"));
        String username     = trim(req.getParameter("username"));
        String password     = req.getParameter("password");
        String confirm      = req.getParameter("confirmPassword");

        // --- Server-side validation ---
        StringBuilder err = new StringBuilder();
        if (firstName.isEmpty() || lastName.isEmpty())   err.append("Name is required. ");
        if (admissionNo.isEmpty())                        err.append("Admission No required. ");
        if (!email.matches("^[\\w.%+\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,}$"))
                                                          err.append("Invalid email. ");
        if (username.isEmpty())                           err.append("Username required. ");
        if (password == null || password.length() < 8)   err.append("Password must be ≥ 8 chars. ");
        if (!password.equals(confirm))                    err.append("Passwords do not match. ");

        int courseId = 0, deptId = 0, joiningYear = 0, graduationYear = 0;
        try { courseId      = Integer.parseInt(courseIdStr); } catch (NumberFormatException e) { err.append("Course required. "); }
        try { deptId        = Integer.parseInt(deptIdStr);  } catch (NumberFormatException e) { err.append("Department required. "); }
        try { joiningYear   = Integer.parseInt(joinStr);    } catch (NumberFormatException e) { err.append("Joining year required. "); }
        try { graduationYear= Integer.parseInt(gradStr);    } catch (NumberFormatException e) { err.append("Graduation year required. "); }
        if (joiningYear > 0 && graduationYear > 0 && joiningYear >= graduationYear)
            err.append("Joining year must be before graduation year. ");

        if (err.length() > 0) {
            forwardWithError(req, resp, err.toString());
            return;
        }

        try {
            if (studentDAO.existsByUsername(username))    { forwardWithError(req, resp, "Username already taken."); return; }
            if (studentDAO.existsByEmail(email))          { forwardWithError(req, resp, "Email already registered."); return; }
            if (studentDAO.existsByAdmissionNo(admissionNo)) { forwardWithError(req, resp, "Admission No already registered."); return; }

            Student s = new Student();
            s.setFirstName(firstName); s.setLastName(lastName);
            s.setAdmissionNo(admissionNo); s.setEmail(email);
            s.setCourseId(courseId); s.setDepartmentId(deptId);
            s.setJoiningYear(joiningYear); s.setGraduationYear(graduationYear);
            s.setUsername(username);

            if (studentDAO.register(s, password)) {
                req.getSession().setAttribute("successMessage", "Registration successful! Please log in.");
                resp.sendRedirect(req.getContextPath() + "/Student_Login.jsp");
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
            req.setAttribute("courses",     masterDataDAO.findAllCourses());
        } catch (Exception ignored) {}
        req.getRequestDispatcher("/Register_Student.jsp").forward(req, resp);
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
