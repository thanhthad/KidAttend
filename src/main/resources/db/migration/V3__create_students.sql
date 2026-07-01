CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          class_id BIGINT,

                          full_name VARCHAR(100),
                          gender VARCHAR(10),
                          date_of_birth DATE,

                          parent_name VARCHAR(100),
                          parent_phone VARCHAR(20),
                          parent_email VARCHAR(100),

                          address TEXT,

                          status VARCHAR(20),

                          created_at TIMESTAMP,
                          updated_at TIMESTAMP,

                          CONSTRAINT fk_students_class
                              FOREIGN KEY (class_id)
                                  REFERENCES classes(id)
);