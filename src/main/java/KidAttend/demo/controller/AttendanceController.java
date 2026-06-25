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

    // ================= Init =================
    @PostMapping
    public ResponseEntity<?> init() {

        List<AttendanceResponse> response = attendanceService.init();

        return ResponseData.success(
                response,
                "Create attendance successfully",
                HttpStatus.CREATED
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
                "Update attendance successfully",
                HttpStatus.OK
        );
    }

    @GetMapping("/student/{studentId}/history")
    public ResponseEntity<?> studentHistory(
            @PathVariable Long studentId
    ) {
        List<StudentAttendanceHistoryResponse> response =
                attendanceService.getStudentHistory(studentId);

        return ResponseData.success(response, "OK", HttpStatus.OK);
    }

    @GetMapping("/class/history/{classId}")
    public ResponseEntity<?> classHistory(
            @PathVariable Long classId,
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            Pageable pageable
    ) {

        Page<ClassAttendanceHistoryResponse> response =
                attendanceService.getClassAttendanceHistory(classId, fromDate, toDate, pageable);

        return ResponseData.success(response, "OK", HttpStatus.OK);
    }

    @GetMapping("/teacher/summary")
    public ResponseEntity<?> teacherSummary(
            @RequestParam Long teacherId,
            @RequestParam LocalDate date
    ) {
        List<TeacherAttendanceSummaryResponse> response =
                attendanceService.getTeacherAttendanceSummary(teacherId, date);

        return ResponseData.success(response, "OK", HttpStatus.OK);
    }

    // ================= GET DATES BY CLASS =================
    @GetMapping("/dates/class/{classId}")
    public ResponseEntity<?> getDatesByClass(@PathVariable Long classId) {

        List<AttendanceDateResponse> response =
                attendanceService.getAttendanceDatesByClassId(classId);

        return ResponseData.success(
                response,
                "Get attendance dates by class successfully",
                HttpStatus.OK
        );
    }

    // ================= STATUS SUMMARY BY CLASS =================
    @GetMapping("/summary/class/{classId}")
    public ResponseEntity<?> summaryByClass(
            @PathVariable Long classId,
            @RequestParam LocalDate date
    ) {

        List<AttendanceStatusSummaryResponse> response =
                attendanceService.getStatusSummaryByDateAndClass(classId, date);

        return ResponseData.success(
                response,
                "Get status summary by class successfully",
                HttpStatus.OK
        );
    }


    // ================= CLASS ATTENDANCE =================
    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getClassAttendance(
            @PathVariable Long classId,
            @RequestParam LocalDate date
    ) {

        List<ClassAttendanceResponse> response =
                attendanceService.getClassAttendance(classId, date);

        return ResponseData.success(
                response,
                "Get class attendance successfully",
                HttpStatus.OK
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        attendanceService.delete(id);

        return ResponseData.success(
                null,
                "Delete attendance successfully",
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
                "Get attendance dates successfully",
                HttpStatus.OK
        );
    }



    // ================= GET BY DATE (DETAIL + PAGING) =================
    @GetMapping("/date")
    public ResponseEntity<?> getByDate(
            @RequestParam LocalDate date
    ) {

        List<AttendanceDetailResponse> response =
                attendanceService.getAttendanceByDate(date);

        return ResponseData.success(
                response,
                "Get attendance by date successfully",
                HttpStatus.OK
        );
    }

    // ================= STATUS SUMMARY BY DATE =================
    @GetMapping("/summary/date")
    public ResponseEntity<?> summaryByDate(@RequestParam LocalDate date) {

        List<AttendanceStatusSummaryResponse> response =
                attendanceService.getStatusSummaryByDate(date);

        return ResponseData.success(
                response,
                "Get status summary by date successfully",
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
                "Get attendance rate successfully",
                HttpStatus.OK
        );
    }



    @GetMapping("/students/not-yet")
    public ResponseEntity<?> notYetAttendance(
            @RequestParam LocalDate date,
            Pageable pageable
    ) {
        Page<StudentResponse> response =
                attendanceService.getStudentsNotYetAttendance(date, pageable);

        return ResponseData.success(response, "OK", HttpStatus.OK);
    }

    @GetMapping("/top-absent")
    public ResponseEntity<?> topAbsent() {

        List<TopAbsentStudentResponse> response =
                attendanceService.getTopAbsentStudents();

        return ResponseData.success(response, "OK", HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam LocalDate date,
            @RequestParam String status,
            Pageable pageable
    ) {

        Page<AttendanceFilterResponse> response =
                attendanceService.filterByStatus(date, status, pageable);

        return ResponseData.success(response, "OK", HttpStatus.OK);
    }



    @GetMapping("/student/{studentId}/statistic")
    public ResponseEntity<?> studentStatistic(@PathVariable Long studentId) {

        StudentAttendanceStatisticResponse response =
                attendanceService.getStudentStatistic(studentId);

        return ResponseData.success(response, "OK", HttpStatus.OK);
    }
}