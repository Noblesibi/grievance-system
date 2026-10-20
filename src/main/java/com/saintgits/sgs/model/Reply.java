package com.saintgits.sgs.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Reply implements Serializable {
    private static final long serialVersionUID = 1L;

    private int       replyId;
    private int       complaintId;
    private int       staffId;
    private String    staffName;    // joined
    private String    replyText;
    private String    statusAfter;
    private Timestamp repliedAt;

    public Reply() {}

    // Getters
    public int       getReplyId()     { return replyId;     }
    public int       getComplaintId() { return complaintId; }
    public int       getStaffId()     { return staffId;     }
    public String    getStaffName()   { return staffName;   }
    public String    getReplyText()   { return replyText;   }
    public String    getStatusAfter() { return statusAfter; }
    public Timestamp getRepliedAt()   { return repliedAt;   }

    // Setters
    public void setReplyId(int replyId)           { this.replyId     = replyId;     }
    public void setComplaintId(int complaintId)   { this.complaintId = complaintId; }
    public void setStaffId(int staffId)           { this.staffId     = staffId;     }
    public void setStaffName(String staffName)    { this.staffName   = staffName;   }
    public void setReplyText(String replyText)    { this.replyText   = replyText;   }
    public void setStatusAfter(String statusAfter){ this.statusAfter = statusAfter; }
    public void setRepliedAt(Timestamp repliedAt) { this.repliedAt   = repliedAt;   }
}
