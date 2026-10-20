package com.saintgits.sgs.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Staff implements Serializable {
    private static final long serialVersionUID = 1L;

    private int       staffId;
    private String    staffCode;
    private String    firstName;
    private String    lastName;
    private String    email;
    private String    phone;
    private int       departmentId;
    private String    departmentName;
    private String    designation;        // HOD | Professor | Assistant Professor
    private String    username;
    private String    passwordHash;
    private boolean   isCommitteeMember;
    private Timestamp createdAt;

    public Staff() {}

    // Getters
    public int       getStaffId()          { return staffId;          }
    public String    getStaffCode()        { return staffCode;        }
    public String    getFirstName()        { return firstName;        }
    public String    getLastName()         { return lastName;         }
    public String    getFullName()         { return firstName + " " + lastName; }
    public String    getEmail()            { return email;            }
    public String    getPhone()            { return phone;            }
    public int       getDepartmentId()     { return departmentId;     }
    public String    getDepartmentName()   { return departmentName;   }
    public String    getDesignation()      { return designation;      }
    public String    getUsername()         { return username;         }
    public String    getPasswordHash()     { return passwordHash;     }
    public boolean   isCommitteeMember()   { return isCommitteeMember;}
    public Timestamp getCreatedAt()        { return createdAt;        }

    // Setters
    public void setStaffId(int staffId)                     { this.staffId          = staffId;          }
    public void setStaffCode(String staffCode)              { this.staffCode        = staffCode;        }
    public void setFirstName(String firstName)              { this.firstName        = firstName;        }
    public void setLastName(String lastName)                { this.lastName         = lastName;         }
    public void setEmail(String email)                      { this.email            = email;            }
    public void setPhone(String phone)                      { this.phone            = phone;            }
    public void setDepartmentId(int departmentId)           { this.departmentId     = departmentId;     }
    public void setDepartmentName(String departmentName)    { this.departmentName   = departmentName;   }
    public void setDesignation(String designation)          { this.designation      = designation;      }
    public void setUsername(String username)                { this.username         = username;         }
    public void setPasswordHash(String passwordHash)        { this.passwordHash     = passwordHash;     }
    public void setCommitteeMember(boolean committeeMember) { this.isCommitteeMember= committeeMember;  }
    public void setCreatedAt(Timestamp createdAt)           { this.createdAt        = createdAt;        }
}
