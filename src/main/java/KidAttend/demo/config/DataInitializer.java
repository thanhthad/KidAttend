package KidAttend.demo.config;

import KidAttend.demo.entity.AttendanceSetting;
import KidAttend.demo.entity.User;
import KidAttend.demo.repository.AttendanceSettingRepository;
import KidAttend.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AttendanceSettingRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        if (!userRepository.existsByRole("ADMIN")) {

            for (int i = 1; i <= 5; i++) {

                User admin = User.builder()
                        .fullName("Admin " + i)
                        .email("admin" + i + "@gmail.com")
                        .phone("096944999" + i)
                        .passwordHash(passwordEncoder.encode("123456"))
                        .role("ADMIN")
                        .status("ACTIVE")
                        .build();

                userRepository.save(admin);
            }

            log.info("Inserted default admins");
        }
    }


}