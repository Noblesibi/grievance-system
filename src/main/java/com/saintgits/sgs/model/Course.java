package com.saintgits.sgs.model;

import java.io.Serializable;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private int    courseId;
    private String cname;

    public Course() {}
    public Course(int courseId, String cname) {
        this.courseId = courseId;
        this.cname    = cname;
    }

    public int    getCourseId() { return courseId; }
    public String getCname()    { return cname;    }
    public void   setCourseId(int courseId) { this.courseId = courseId; }
    public void   setCname(String cname)    { this.cname    = cname;    }
}
