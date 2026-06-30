package KidAttend.demo.controller;

import KidAttend.demo.common.response.ResponseData;
import KidAttend.demo.dto.request.attendance.CreateAttendanceRequest;
import KidAttend.demo.dto.request.attendance.UpdateAttendanceRequest;
import KidAttend.demo.dto.response.attendance.*;
import KidAttend.demo.dto.response.student.StudentResponse;
import KidAttend.demo.service.AttendanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Attendance Management", description = "Attendance APIs")
public class AttendanceController {

    private final AttendanceService attendanceService;

    // ================= INIT =================
    @PostMapping
    public ResponseEntity<?> init() {

        List<AttendanceResponse> response = attendanceService.init();

        return ResponseData.success(
                response,
                "Khởi tạo điểm danh thành công",
                HttpStatus.CREATED
        );
    }

    @GetMapping("/student/{studentId}/statistic")
    public ResponseEntity<?> studentStatistic(@PathVariable Long studentId) {

        StudentAttendanceStatisticResponse response =
                attendanceService.getStudentStatistic(studentId);

        return ResponseData.success(response, "Thành công", HttpStatus.OK);
    }

    // ================= CLASS ATTENDANCE =================
    @GetMapping("/class/{date}")
    public ResponseEntity<?> getClassAttendance(@PathVariable LocalDate date) {

        List<ClassAttendanceResponse> response =
                attendanceService.getClassAttendance(date);

        return ResponseData.success(
                response,
                "Lấy danh sách điểm danh theo lớp thành công",
                HttpStatus.OK
        );
    }

    @GetMapping("/student/{studentId}/history")
    public ResponseEntity<?> studentHistory(@PathVariable Long studentId) {

        List<StudentAttendanceHistoryResponse> response =
                attendanceService.getStudentHistory(studentId);

        return ResponseData.success(response, "Thành công", HttpStatus.OK);
    }

    @GetMapping("/teacher/summary/me")
    public ResponseEntity<?> teacherSummaryByMe(@RequestParam LocalDate date) {

        List<TeacherAttendanceSummaryResponse> response =
                attendanceService.getTeacherAttendanceSummaryMe(date);

        return ResponseData.success(response, "Thành công", HttpStatus.OK);
    }

    // ================= DATES BY CLASS =================
    @GetMapping("/dates/class/me")
    public ResponseEntity<?> getDatesByClass() {

        List<AttendanceDateResponse> response =
                attendanceService.getAttendanceDatesByClassId();

        return ResponseData.success(
                response,
                "Lấy danh sách ngày điểm danh theo lớp thành công",
                HttpStatus.OK
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateAttendanceRequest request
    ) {

        AttendanceResponse response = attendanceService.update(id, request);

        return ResponseData.success(
                response,
                "Cập nhật điểm danh thành công",
                HttpStatus.OK
        );
    }

    // ================= BATCH UPDATE =================
    @PutMapping("/batch")
    public ResponseEntity<?> updateBatch(
            @RequestBody @Valid List<UpdateAttendanceRequest> requests
    ) {

        List<AttendanceResponse> attendanceResponses =
                attendanceService.batchUpdate(requests);

        return ResponseData.success(
                attendanceResponses,
                "Cập nhật điểm danh hàng loạt thành công",
                HttpStatus.OK
        );
    }

    // ================= SUMMARY BY CLASS =================
    @GetMapping("/summary/class/{classId}")
    public ResponseEntity<?> summaryByClass(
            @PathVariable Long classId,
            @RequestParam LocalDate date
    ) {

        List<AttendanceStatusSummaryResponse> response =
                attendanceService.getStatusSummaryByDateAndClass(classId, date);

        return ResponseData.success(
                response,
                "Lấy thống kê điểm danh theo lớp thành công",
                HttpStatus.OK
        );
    }

    // ================= GET ALL DATES =================
    @GetMapping("/dates")
    public ResponseEntity<?> getAllDates() {

        List<AttendanceDateResponse> response =
                attendanceService.getAttendanceDates();

        return ResponseData.success(
                response,
                "Lấy danh sách ngày điểm danh thành công",
                HttpStatus.OK
        );
    }

    // ================= SUMMARY BY DATE =================
    @GetMapping("/summary/date")
    public ResponseEntity<?> summaryByDate(@RequestParam LocalDate date) {

        List<AttendanceStatusSummaryResponse> response =
                attendanceService.getStatusSummaryByDate(date);

        return ResponseData.success(
                response,
                "Lấy thống kê điểm danh theo ngày thành công",
                HttpStatus.OK
        );
    }

    // ================= ATTENDANCE RATE =================
    @GetMapping("/rate")
    public ResponseEntity<?> getRate(@RequestParam LocalDate date) {

        List<ClassAttendanceRateResponse> response =
                attendanceService.getAttendanceRate(date);

        return ResponseData.success(
                response,
                "Lấy tỷ lệ điểm danh thành công",
                HttpStatus.OK
        );
    }

    // ================= NOT YET ATTENDANCE =================
    @GetMapping("/students/not-yet")
    public ResponseEntity<?> notYetAttendance(
            @RequestParam LocalDate date,
            Pageable pageable
    ) {

        Page<StudentResponse> response =
                attendanceService.getStudentsNotYetAttendance(date, pageable);

        return ResponseData.success(
                response,
                "Danh sách học sinh chưa điểm danh",
                HttpStatus.OK
        );
    }

    // ================= TOP ABSENT =================
    @GetMapping("/top-absent")
    public ResponseEntity<?> topAbsent() {

        List<TopAbsentStudentResponse> response =
                attendanceService.getTopAbsentStudents();

        return ResponseData.success(
                response,
                "Danh sách học sinh nghỉ nhiều nhất",
                HttpStatus.OK
        );
    }

    // ================= FILTER =================
    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam LocalDate date,
            @RequestParam String status,
            Pageable pageable
    ) {

        Page<AttendanceFilterResponse> response =
                attendanceService.filterByStatus(date, status, pageable);

        return ResponseData.success(response, "Thành công", HttpStatus.OK);
    }

    // ================= GET BY DATE =================
    @GetMapping("/date")
    public ResponseEntity<?> getByDate(
            @RequestParam LocalDate date,
            Pageable pageable
    ) {

        Page<AttendanceDetailResponse> response =
                attendanceService.getAttendanceByDate(date, pageable);

        return ResponseData.success(
                response,
                "Lấy dữ liệu điểm danh theo ngày thành công",
                HttpStatus.OK
        );
    }

    // ================= CLASS HISTORY =================
    @GetMapping("/class/history/{classId}")
    public ResponseEntity<?> classHistory(
            @PathVariable Long classId,
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            Pageable pageable
    ) {

        Page<ClassAttendanceHistoryResponse> response =
                attendanceService.getClassAttendanceHistory(classId, fromDate, toDate, pageable);

        return ResponseData.success(response, "Thành công", HttpStatus.OK);
    }

    // ================= TEACHER SUMMARY =================
    @GetMapping("/teacher/summary")
    public ResponseEntity<?> teacherSummary(
            @RequestParam Long teacherId,
            @RequestParam LocalDate date
    ) {

        List<TeacherAttendanceSummaryResponse> response =
                attendanceService.getTeacherAttendanceSummary(teacherId, date);

        return ResponseData.success(response, "Thành công", HttpStatus.OK);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        attendanceService.delete(id);

        return ResponseData.success(
                null,
                "Xóa điểm danh thành công",
                HttpStatus.OK
        );
    }
}