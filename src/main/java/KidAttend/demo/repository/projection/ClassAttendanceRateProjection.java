package KidAttend.demo.repository.projection;

public interface ClassAttendanceRateProjection {

    Long getClassId();

    String getClassName();

    Long getTotal();

    Long getPresent();

    Double getRate();

}
