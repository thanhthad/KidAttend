package KidAttend.demo.repository.projection;

public interface StudentAttendanceStatisticProjection {

    Long getStudentId();

    Long getPresentDays();

    Long getAbsentDays();

    Long getLateDays();

}