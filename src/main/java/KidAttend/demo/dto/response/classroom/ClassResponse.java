package KidAttend.demo.dto.response.classroom;

import KidAttend.demo.dto.response.user.TeacherResponse;
import KidAttend.demo.entity.ClassStatus;
import lombok.*;

@Getter
@Setter
@Builder
public class ClassResponse {

    private Long id;
    private String name;
    private Integer age;
    private Integer capacity;

    private String description;
    private ClassStatus status;

    private TeacherResponse teacher;

    private Long currentStudents;
}