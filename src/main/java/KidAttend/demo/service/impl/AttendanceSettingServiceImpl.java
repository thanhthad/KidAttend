package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.attendancesetting.AttendanceSettingUpdateRequest;
import KidAttend.demo.dto.response.attendancesetting.AttendanceSettingResponse;
import KidAttend.demo.entity.AttendanceSetting;
import KidAttend.demo.exception.attendance.AttendanceNotFoundException;
import KidAttend.demo.repository.AttendanceSettingRepository;
import KidAttend.demo.service.AttendanceSettingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceSettingServiceImpl implements AttendanceSettingService {

    private final AttendanceSettingRepository repository;

    @Override
    @Transactional
    public AttendanceSettingResponse updateSetting(AttendanceSettingUpdateRequest request) {

        AttendanceSetting setting = repository.findById(1L)
                .orElseThrow(() -> new AttendanceNotFoundException("Attendance setting not initialized"));

        if (request.getStartTime() != null) {
            setting.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            setting.setEndTime(request.getEndTime());
        }

        if (setting.getStartTime() != null && setting.getEndTime() != null) {
            if (!setting.getStartTime().isBefore(setting.getEndTime())) {
                throw new IllegalArgumentException("startTime must be before endTime");
            }
        }

        if (request.getAllowLateMinutes() != null) {

            if (request.getAllowLateMinutes() < 0) {
                throw new IllegalArgumentException("allowLateMinutes must be >= 0");
            }

            setting.setAllowLateMinutes(request.getAllowLateMinutes());
        }

        AttendanceSetting saved = repository.save(setting);

        return AttendanceSettingResponse.builder()
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime())
                .allowLateMinutes(saved.getAllowLateMinutes())
                .build();
    }
}