package com.saintgits.sgs.model;

import java.io.Serializable;

public class Department implements Serializable {
    private static final long serialVersionUID = 1L;
    private int    departmentId;
    private String dname;

    public Department() {}
    public Department(int departmentId, String dname) {
        this.departmentId = departmentId;
        this.dname        = dname;
    }

    public int    getDepartmentId() { return departmentId; }
    public String getDname()        { return dname;        }
    public void   setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public void   setDname(String dname)            { this.dname        = dname;        }
}
