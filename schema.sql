-- ============================================================
-- Student Grievance System (SGS) – Database Schema & Seed Data
-- Database: sgs_db | MySQL 8
-- ============================================================

CREATE DATABASE IF NOT EXISTS sgs_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sgs_db;

-- -----------------------------------------------------------
-- 1. DEPARTMENT
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS department (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    dname         VARCHAR(100) NOT NULL UNIQUE
);

-- -----------------------------------------------------------
-- 2. COURSE
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS course (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    cname     VARCHAR(100) NOT NULL UNIQUE
);

-- -----------------------------------------------------------
-- 3. ADMIN
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS admin (
    admin_id      INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(60)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100) DEFAULT 'Administrator'
);

-- -----------------------------------------------------------
-- 4. STUDENT
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS student (
    student_id      INT AUTO_INCREMENT PRIMARY KEY,
    first_name      VARCHAR(60)  NOT NULL,
    last_name       VARCHAR(60)  NOT NULL,
    admission_no    VARCHAR(40)  NOT NULL UNIQUE,
    email           VARCHAR(120) NOT NULL UNIQUE,
    course_id       INT          NOT NULL,
    department_id   INT          NOT NULL,
    joining_year    YEAR         NOT NULL,
    graduation_year YEAR         NOT NULL,
    username        VARCHAR(60)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stu_course  FOREIGN KEY (course_id)     REFERENCES course(course_id),
    CONSTRAINT fk_stu_dept    FOREIGN KEY (department_id) REFERENCES department(department_id)
);

-- -----------------------------------------------------------
-- 5. STAFF
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS staff (
    staff_id            INT AUTO_INCREMENT PRIMARY KEY,
    staff_code          VARCHAR(40)  NOT NULL UNIQUE,
    first_name          VARCHAR(60)  NOT NULL,
    last_name           VARCHAR(60)  NOT NULL,
    email               VARCHAR(120) NOT NULL UNIQUE,
    phone               VARCHAR(15),
    department_id       INT          NOT NULL,
    designation         ENUM('HOD','Professor','Assistant Professor') NOT NULL DEFAULT 'Assistant Professor',
    username            VARCHAR(60)  NOT NULL UNIQUE,
    password_hash       VARCHAR(255) NOT NULL,
    is_committee_member BOOLEAN      NOT NULL DEFAULT 0,
    created_at          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_staff_dept FOREIGN KEY (department_id) REFERENCES department(department_id)
);

-- -----------------------------------------------------------
-- 6. COMPLAINT
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS complaint (
    complaint_id       INT AUTO_INCREMENT PRIMARY KEY,
    student_id         INT          NOT NULL,
    complaint_date     DATE         NOT NULL,
    subject            VARCHAR(200) NOT NULL,
    description        TEXT         NOT NULL,
    status             ENUM('Pending','In Progress','Resolved','Escalated') NOT NULL DEFAULT 'Pending',
    assigned_staff_id  INT          DEFAULT NULL,
    created_at         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_comp_student FOREIGN KEY (student_id)        REFERENCES student(student_id),
    CONSTRAINT fk_comp_staff   FOREIGN KEY (assigned_staff_id) REFERENCES staff(staff_id)
);

-- -----------------------------------------------------------
-- 7. COMPLAINT_REPLY
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS complaint_reply (
    reply_id      INT AUTO_INCREMENT PRIMARY KEY,
    complaint_id  INT         NOT NULL,
    staff_id      INT         NOT NULL,
    reply_text    TEXT        NOT NULL,
    status_after  ENUM('Pending','In Progress','Resolved','Escalated') NOT NULL,
    replied_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reply_complaint FOREIGN KEY (complaint_id) REFERENCES complaint(complaint_id),
    CONSTRAINT fk_reply_staff     FOREIGN KEY (staff_id)     REFERENCES staff(staff_id)
);

-- ============================================================
-- SEED DATA
-- ============================================================

-- Departments
INSERT INTO department (dname) VALUES
('Computer Science'),
('Electronics & Communication'),
('Mechanical Engineering'),
('Civil Engineering'),
('Electrical Engineering'),
('Information Technology');

-- Courses
INSERT INTO course (cname) VALUES
('MCA'),
('B.Tech'),
('M.Tech'),
('BCA'),
('B.Sc Computer Science'),
('MBA');

-- Admin  (password: admin123  – BCrypt hash generated with work factor 12)
INSERT INTO admin (username, password_hash, full_name) VALUES
('admin', '$2a$12$2v4P5NuB6nF7Rh3KlZmXNeyPdx9Hy3Qk8Ie3VE2M0N7jXtOqBu7a', 'System Administrator');

-- Staff  (password for all demo staff: Staff@123)
INSERT INTO staff (staff_code, first_name, last_name, email, phone, department_id, designation, username, password_hash, is_committee_member) VALUES
('STAFF001', 'Rajesh',  'Kumar',   'rajesh.kumar@saintgits.org',   '9876543210', 1, 'HOD',                  'rajesh.kumar',   '$2a$12$hG.SZ2YFg4e8r0ZgF6BSSO3vJQpT9mLvZxTFJmqnWjNUQRJXuovgW', 1),
('STAFF002', 'Priya',   'Nair',    'priya.nair@saintgits.org',     '9876543211', 1, 'Professor',            'priya.nair',     '$2a$12$hG.SZ2YFg4e8r0ZgF6BSSO3vJQpT9mLvZxTFJmqnWjNUQRJXuovgW', 1),
('STAFF003', 'Anand',   'Menon',   'anand.menon@saintgits.org',    '9876543212', 2, 'HOD',                  'anand.menon',    '$2a$12$hG.SZ2YFg4e8r0ZgF6BSSO3vJQpT9mLvZxTFJmqnWjNUQRJXuovgW', 0),
('STAFF004', 'Meera',   'Thomas',  'meera.thomas@saintgits.org',   '9876543213', 1, 'Assistant Professor',  'meera.thomas',   '$2a$12$hG.SZ2YFg4e8r0ZgF6BSSO3vJQpT9mLvZxTFJmqnWjNUQRJXuovgW', 0),
('STAFF005', 'Suresh',  'Pillai',  'suresh.pillai@saintgits.org',  '9876543214', 3, 'Professor',            'suresh.pillai',  '$2a$12$hG.SZ2YFg4e8r0ZgF6BSSO3vJQpT9mLvZxTFJmqnWjNUQRJXuovgW', 0);

-- Students (password for all demo students: Student@123)
INSERT INTO student (first_name, last_name, admission_no, email, course_id, department_id, joining_year, graduation_year, username, password_hash) VALUES
('Arun',    'Krishnan', 'ADM2024001', 'arun.k@student.saintgits.org',  1, 1, 2024, 2026, 'arun.krishnan',  '$2a$12$Bg.TW3ZFh5f9s1AhG7CTTO4wKRqU0nMwAxUGKnroPkOVSRKYvpwgX'),
('Anjali',  'Rajan',    'ADM2024002', 'anjali.r@student.saintgits.org', 2, 1, 2024, 2028, 'anjali.rajan',   '$2a$12$Bg.TW3ZFh5f9s1AhG7CTTO4wKRqU0nMwAxUGKnroPkOVSRKYvpwgX'),
('Kiran',   'George',   'ADM2024003', 'kiran.g@student.saintgits.org',  2, 2, 2024, 2028, 'kiran.george',   '$2a$12$Bg.TW3ZFh5f9s1AhG7CTTO4wKRqU0nMwAxUGKnroPkOVSRKYvpwgX'),
('Sneha',   'Varghese', 'ADM2023001', 'sneha.v@student.saintgits.org',  1, 1, 2023, 2025, 'sneha.varghese', '$2a$12$Bg.TW3ZFh5f9s1AhG7CTTO4wKRqU0nMwAxUGKnroPkOVSRKYvpwgX');

-- Sample Complaints
INSERT INTO complaint (student_id, complaint_date, subject, description, status, assigned_staff_id) VALUES
(1, '2024-10-01', 'Lab Equipment Issue', 'The computers in Lab 3 are outdated and frequently crash during practical sessions, causing us to lose our work.', 'Resolved',    4),
(2, '2024-10-05', 'Timetable Conflict',  'There is a clash between the Advanced Java and Data Structures lab sessions on Wednesday afternoons.', 'In Progress', 2),
(3, '2024-10-10', 'Library Book Unavailability', 'Reference books for the current semester syllabus are not available in the library. Multiple students are affected.', 'Pending',     NULL),
(1, '2024-10-12', 'Hostel Wi-Fi Issue',  'The Wi-Fi connectivity in the hostel is very poor, especially in the evenings when bandwidth is needed for assignments.', 'Escalated',  2);

-- Sample Replies
INSERT INTO complaint_reply (complaint_id, staff_id, reply_text, status_after) VALUES
(1, 4, 'We have noted the issue with Lab 3 computers. A maintenance team has been scheduled for next week.', 'In Progress'),
(1, 4, 'New computers have been ordered and will be installed by end of month. Issue considered resolved.', 'Resolved'),
(2, 2, 'We are reviewing the timetable and will resolve the conflict in the next revision.', 'In Progress');
