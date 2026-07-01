CREATE TABLE leave_requests (
                                id BIGSERIAL PRIMARY KEY,

                                student_id BIGINT,

                                start_date DATE,
                                end_date DATE,

                                reason TEXT,

                                status VARCHAR(20),

                                approved_by BIGINT,

                                created_at TIMESTAMP,
                                updated_at TIMESTAMP,

                                CONSTRAINT fk_leave_student
                                    FOREIGN KEY (student_id)
                                        REFERENCES students(id),

                                CONSTRAINT fk_leave_approved_by
                                    FOREIGN KEY (approved_by)
                                        REFERENCES users(id)
);