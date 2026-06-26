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

    List<AttendanceResponse> init();

    AttendanceResponse update(
            Long id,
            UpdateAttendanceRequest request);

    List<AttendanceResponse> batchUpdate(List<UpdateAttendanceRequest> requests);

    void delete(Long id);

    List<AttendanceDateResponse> getAttendanceDates();

    Page<AttendanceDateResponse> getAttendanceDatesByClassId(Pageable pageable);


    Page<AttendanceDetailResponse> getAttendanceByDate(
            LocalDate date ,Pageable pageable);

    List<AttendanceStatusSummaryResponse> getStatusSummaryByDate(
            LocalDate date);

    List<AttendanceStatusSummaryResponse> getStatusSummaryByDateAndClass(Long classId,
            LocalDate date);

    List<TeacherAttendanceSummaryResponse> getTeacherAttendanceSummary(
            Long teacherId,
            LocalDate date);

    List<TeacherAttendanceSummaryResponse> getTeacherAttendanceSummaryMe(
            LocalDate date);

    List<ClassAttendanceResponse> getClassAttendance(
            LocalDate date);

    Page<StudentAttendanceHistoryResponse> getStudentHistory(
            Long studentId,
            Pageable pageable
    );

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