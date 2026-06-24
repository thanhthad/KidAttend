package KidAttend.demo.service;


import KidAttend.demo.dto.request.attendancesetting.AttendanceSettingUpdateRequest;
import KidAttend.demo.dto.response.attendancesetting.AttendanceSettingResponse;

public interface AttendanceSettingService {

    AttendanceSettingResponse updateSetting(AttendanceSettingUpdateRequest request);
}