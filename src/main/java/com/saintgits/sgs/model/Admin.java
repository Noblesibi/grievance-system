package com.saintgits.sgs.model;

import java.io.Serializable;

public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    private int    adminId;
    private String username;
    private String passwordHash;
    private String fullName;

    public Admin() {}

    public Admin(int adminId, String username, String passwordHash, String fullName) {
        this.adminId      = adminId;
        this.username     = username;
        this.passwordHash = passwordHash;
        this.fullName     = fullName;
    }

    public int    getAdminId()      { return adminId;      }
    public String getUsername()     { return username;     }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName()     { return fullName;     }

    public void setAdminId(int adminId)           { this.adminId      = adminId;      }
    public void setUsername(String username)       { this.username     = username;     }
    public void setPasswordHash(String hash)       { this.passwordHash = hash;         }
    public void setFullName(String fullName)       { this.fullName     = fullName;     }
}
