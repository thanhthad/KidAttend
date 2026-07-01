-- USERS
CREATE UNIQUE INDEX ux_users_phone
    ON users(phone)
    WHERE phone IS NOT NULL;

CREATE UNIQUE INDEX ux_users_email
    ON users(email)
    WHERE email IS NOT NULL;

-- CLASSES
CREATE INDEX idx_classes_teacher_id
    ON classes(teacher_id);

-- STUDENTS
CREATE INDEX idx_students_class_id
    ON students(class_id);

-- ATTENDANCE
CREATE INDEX idx_attendance_student_id
    ON attendance(student_id);

CREATE INDEX idx_attendance_created_by
    ON attendance(created_by);

CREATE INDEX idx_attendance_date
    ON attendance(attendance_date);

-- ATTENDANCE SETTINGS
CREATE INDEX idx_attendance_settings_created_by
    ON attendance_settings(created_by);

-- LEAVE REQUESTS
CREATE INDEX idx_leave_requests_student_id
    ON leave_requests(student_id);

CREATE INDEX idx_leave_requests_approved_by
    ON leave_requests(approved_by);