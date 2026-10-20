package com.saintgits.sgs.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    private int       complaintId;
    private int       studentId;
    private String    studentName;     // joined
    private Date      complaintDate;
    private String    subject;
    private String    description;
    private String    status;          // Pending | In Progress | Resolved | Escalated
    private Integer   assignedStaffId; // nullable
    private String    assignedStaffName; // joined
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Complaint() {}

    // Getters
    public int       getComplaintId()       { return complaintId;      }
    public int       getStudentId()         { return studentId;        }
    public String    getStudentName()       { return studentName;      }
    public Date      getComplaintDate()     { return complaintDate;    }
    public String    getSubject()           { return subject;          }
    public String    getDescription()       { return description;      }
    public String    getStatus()            { return status;           }
    public Integer   getAssignedStaffId()   { return assignedStaffId;  }
    public String    getAssignedStaffName() { return assignedStaffName;}
    public Timestamp getCreatedAt()         { return createdAt;        }
    public Timestamp getUpdatedAt()         { return updatedAt;        }

    // Setters
    public void setComplaintId(int complaintId)               { this.complaintId      = complaintId;      }
    public void setStudentId(int studentId)                   { this.studentId        = studentId;        }
    public void setStudentName(String studentName)            { this.studentName      = studentName;      }
    public void setComplaintDate(Date complaintDate)          { this.complaintDate    = complaintDate;    }
    public void setSubject(String subject)                    { this.subject          = subject;          }
    public void setDescription(String description)            { this.description      = description;      }
    public void setStatus(String status)                      { this.status           = status;           }
    public void setAssignedStaffId(Integer assignedStaffId)   { this.assignedStaffId  = assignedStaffId;  }
    public void setAssignedStaffName(String assignedStaffName){ this.assignedStaffName = assignedStaffName;}
    public void setCreatedAt(Timestamp createdAt)             { this.createdAt        = createdAt;        }
    public void setUpdatedAt(Timestamp updatedAt)             { this.updatedAt        = updatedAt;        }
}
