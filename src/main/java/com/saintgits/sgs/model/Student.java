package com.saintgits.sgs.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private int       studentId;
    private String    firstName;
    private String    lastName;
    private String    admissionNo;
    private String    email;
    private int       courseId;
    private String    courseName;
    private int       departmentId;
    private String    departmentName;
    private int       joiningYear;
    private int       graduationYear;
    private String    username;
    private String    passwordHash;
    private Timestamp createdAt;

    public Student() {}

    // Getters
    public int       getStudentId()      { return studentId;      }
    public String    getFirstName()      { return firstName;      }
    public String    getLastName()       { return lastName;       }
    public String    getFullName()       { return firstName + " " + lastName; }
    public String    getAdmissionNo()    { return admissionNo;    }
    public String    getEmail()          { return email;          }
    public int       getCourseId()       { return courseId;       }
    public String    getCourseName()     { return courseName;     }
    public int       getDepartmentId()   { return departmentId;   }
    public String    getDepartmentName() { return departmentName; }
    public int       getJoiningYear()    { return joiningYear;    }
    public int       getGraduationYear() { return graduationYear; }
    public String    getUsername()       { return username;       }
    public String    getPasswordHash()   { return passwordHash;   }
    public Timestamp getCreatedAt()      { return createdAt;      }

    // Setters
    public void setStudentId(int studentId)           { this.studentId      = studentId;      }
    public void setFirstName(String firstName)         { this.firstName      = firstName;      }
    public void setLastName(String lastName)           { this.lastName       = lastName;       }
    public void setAdmissionNo(String admissionNo)     { this.admissionNo    = admissionNo;    }
    public void setEmail(String email)                 { this.email          = email;          }
    public void setCourseId(int courseId)              { this.courseId       = courseId;       }
    public void setCourseName(String courseName)       { this.courseName     = courseName;     }
    public void setDepartmentId(int departmentId)      { this.departmentId   = departmentId;   }
    public void setDepartmentName(String deptName)     { this.departmentName = deptName;       }
    public void setJoiningYear(int joiningYear)        { this.joiningYear    = joiningYear;    }
    public void setGraduationYear(int graduationYear)  { this.graduationYear = graduationYear; }
    public void setUsername(String username)           { this.username       = username;       }
    public void setPasswordHash(String passwordHash)   { this.passwordHash   = passwordHash;   }
    public void setCreatedAt(Timestamp createdAt)      { this.createdAt      = createdAt;      }
}
