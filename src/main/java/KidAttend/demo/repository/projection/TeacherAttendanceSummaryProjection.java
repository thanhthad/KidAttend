package KidAttend.demo.repository.projection;

public interface TeacherAttendanceSummaryProjection {

    String getClassName();

    Long getTotalAttendance();

    Long getPresent();

    Long getAbsent();

}