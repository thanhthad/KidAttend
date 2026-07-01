CREATE TABLE attendance_settings (
                                     id BIGSERIAL PRIMARY KEY,

                                     start_time TIME,
                                     end_time TIME,

                                     allow_late_minutes INT,

                                     created_by BIGINT,

                                     created_at TIMESTAMP,
                                     updated_at TIMESTAMP,

                                     CONSTRAINT fk_attendance_settings_created_by
                                         FOREIGN KEY (created_by)
                                             REFERENCES users(id)
);