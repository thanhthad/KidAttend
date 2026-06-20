package KidAttend.demo.repository;

public interface ClassProjection {

    Long getId();
    String getName();
    Integer getAge();
    Integer getCapacity();
    String getDescription();
    String getStatus();

    Long getTeacherId();
    String getTeacherName();
    String getTeacherEmail();
    String getTeacherPhone();

    Long getCurrentStudents();
}