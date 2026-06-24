package KidAttend.demo.service;

import KidAttend.demo.dto.request.attendance.CreateAttendanceRequest;
import KidAttend.demo.dto.request.attendance.UpdateAttendanceRequest;
import KidAttend.demo.dto.response.attendance.*;
import KidAttend.demo.dto.response.student.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    AttendanceResponse create(CreateAttendanceRequest request);

    AttendanceResponse update(
            Long id,
            UpdateAttendanceRequest request);

    void delete(Long id);

    List<AttendanceDateResponse> getAttendanceDates();

    List<AttendanceDateResponse> getAttendanceDatesByClassId(Long classId);


    List<AttendanceDetailResponse> getAttendanceByDate(
            LocalDate date);

    List<AttendanceStatusSummaryResponse> getStatusSummaryByDate(
            LocalDate date);

    List<AttendanceStatusSummaryResponse> getStatusSummaryByDateAndClass(Long classId,
            LocalDate date);

    List<TeacherAttendanceSummaryResponse> getTeacherAttendanceSummary(
            Long teacherId,
            LocalDate date);

    List<ClassAttendanceResponse> getClassAttendance(
            Long classId,
            LocalDate date);

    List<StudentAttendanceHistoryResponse> getStudentHistory(
            Long studentId);

    Page<StudentResponse> getStudentsNotYetAttendance(
            LocalDate date,
            Pageable pageable);

    List<TopAbsentStudentResponse> getTopAbsentStudents();

    List<ClassAttendanceRateResponse> getAttendanceRate(
            LocalDate date);

    Page<AttendanceFilterResponse> filterByStatus(
            LocalDate date,
            String status,
            Pageable pageable);

    Page<ClassAttendanceHistoryResponse> getClassAttendanceHistory(
            Long classId,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable);

    StudentAttendanceStatisticResponse getStudentStatistic(
            Long studentId);
}