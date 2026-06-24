package KidAttend.demo.repository;

import KidAttend.demo.entity.AttendanceSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceSettingRepository extends JpaRepository<AttendanceSetting, Long> {
}
