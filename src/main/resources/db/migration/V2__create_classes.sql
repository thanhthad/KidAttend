CREATE TABLE classes (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100),
                         age INT,
                         capacity INT,
                         teacher_id BIGINT UNIQUE,
                         description TEXT,
                         status VARCHAR(20),
                         created_at TIMESTAMP,
                         updated_at TIMESTAMP,

                         CONSTRAINT fk_classes_teacher
                             FOREIGN KEY (teacher_id)
                                 REFERENCES users(id)
);