package KidAttend.demo.config;

import KidAttend.demo.entity.AttendanceSetting;
import KidAttend.demo.repository.AttendanceSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AttendanceSettingRepository repository;

    @Override
    public void run(String... args) {

        if (repository.count() == 0) {

            AttendanceSetting setting = AttendanceSetting.builder()
                    .startTime(LocalTime.of(7, 0))
                    .endTime(LocalTime.of(8, 30))
                    .allowLateMinutes(15)
                    .build();

            repository.save(setting);

            log.info("Inserted default attendance setting");
        } else {
            log.warn("Attendance setting already exists");
        }
    }
}