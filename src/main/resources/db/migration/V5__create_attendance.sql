CREATE TABLE attendance (
                            id BIGSERIAL PRIMARY KEY,

                            student_id BIGINT,

                            attendance_date DATE,

                            status VARCHAR(20),
                            note TEXT,

                            created_by BIGINT,

                            created_at TIMESTAMP,
                            updated_at TIMESTAMP,

                            CONSTRAINT fk_attendance_student
                                FOREIGN KEY (student_id)
                                    REFERENCES students(id),

                            CONSTRAINT fk_attendance_created_by
                                FOREIGN KEY (created_by)
                                    REFERENCES users(id),

                            CONSTRAINT uk_attendance_student_date
                                UNIQUE (student_id, attendance_date)
);