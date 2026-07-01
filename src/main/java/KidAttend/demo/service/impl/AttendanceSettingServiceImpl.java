package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.attendancesetting.AttendanceSettingUpdateRequest;
import KidAttend.demo.dto.response.attendancesetting.AttendanceSettingResponse;
import KidAttend.demo.entity.AttendanceSetting;
import KidAttend.demo.exception.attendance.AttendanceNotFoundException;
import KidAttend.demo.exception.attendancesetting.AttendanceSettingNotFoundException;
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
                .orElseThrow(() -> new AttendanceNotFoundException("Chưa khởi tạo cấu hình điểm danh"));

        if (request.getStartTime() != null) {
            setting.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            setting.setEndTime(request.getEndTime());
        }

        if (setting.getStartTime() != null && setting.getEndTime() != null) {
            if (!setting.getStartTime().isBefore(setting.getEndTime())) {
                throw new IllegalArgumentException("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc");
            }
        }

        if (request.getAllowLateMinutes() != null) {

            if (request.getAllowLateMinutes() < 0) {
                throw new IllegalArgumentException("Số phút đi muộn không được nhỏ hơn 0");
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

    @Override
    public AttendanceSettingResponse getSetting() {
        AttendanceSetting setting = repository.findById(1L)
                .orElseThrow(() -> new AttendanceSettingNotFoundException("Chưa khởi tạo cấu hình điểm danh"));

        return AttendanceSettingResponse.builder()
                .startTime(setting.getStartTime())
                .endTime(setting.getEndTime())
                .allowLateMinutes(setting.getAllowLateMinutes())
                .build();
    }
}