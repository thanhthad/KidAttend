package KidAttend.demo.controller;

import KidAttend.demo.common.response.ResponseData;
import KidAttend.demo.dto.request.attendancesetting.AttendanceSettingUpdateRequest;
import KidAttend.demo.dto.response.attendancesetting.AttendanceSettingResponse;
import KidAttend.demo.service.AttendanceSettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance-setting")
@RequiredArgsConstructor
public class AttendanceSettingController {

    private final AttendanceSettingService service;

    @PutMapping
    public ResponseEntity<?> updateSetting(
            @Valid @RequestBody AttendanceSettingUpdateRequest request
    ) {
        AttendanceSettingResponse response =
                service.updateSetting(request);

        return ResponseData.success(
                response,
                "Update attendance setting successfully",
                HttpStatus.OK
        );
    }
}