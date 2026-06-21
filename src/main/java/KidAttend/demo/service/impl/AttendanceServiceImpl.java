package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.attendance.CreateAttendanceRequest;
import KidAttend.demo.dto.request.attendance.UpdateAttendanceRequest;
import KidAttend.demo.dto.response.attendance.*;
import KidAttend.demo.dto.response.student.StudentResponse;
import KidAttend.demo.entity.Attendance;
import KidAttend.demo.entity.Student;
import KidAttend.demo.entity.User;
import KidAttend.demo.exception.attendance.AttendanceAlreadyExistsException;
import KidAttend.demo.exception.attendance.AttendanceNotFoundException;
import KidAttend.demo.exception.student.StudentNotFoundException;
import KidAttend.demo.repository.AttendanceRepository;
import KidAttend.demo.repository.StudentRepository;
import KidAttend.demo.service.AttendanceService;
import KidAttend.demo.service.UserServiceDomain;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final UserServiceDomain userServiceDomain;

    @Override
    @Transactional
    public AttendanceResponse create(CreateAttendanceRequest request) {
        if(!attendanceRepository.existsByStudent_IdAndAttendanceDate(
                request.getStudentId(),request.getAttendanceDate()
        )){
            throw new AttendanceAlreadyExistsException("Student already attend today");
        }
        User user = userServiceDomain.getByUserId(request.getCreatedBy());
        Student student = studentRepository.findById(request.getStudentId()).orElseThrow(
                () -> new StudentNotFoundException("Student not found with id:" + request.getStudentId())
        );
        Attendance saved = Attendance.builder()
                .student(student)
                .note(request.getNote())
                .status(request.getStatus())
                .attendanceDate(request.getAttendanceDate())
                .createdBy(user)
                .build();

        attendanceRepository.save(saved);
        return AttendanceResponse.builder()
                .id(saved.getId())
                .studentId(request.getStudentId())
                .studentName(student.getFullName())
                .attendanceDate(request.getAttendanceDate())
                .status(request.getStatus())
                .note(request.getNote())
                .createdBy(request.getCreatedBy())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public AttendanceResponse update(Long id, UpdateAttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(
                () -> new AttendanceNotFoundException("Attendance not found with id:" + id)
        );
        attendance.setStatus(request.getStatus());
        attendance.setNote(request.getNote());
        attendanceRepository.save(attendance);
        return AttendanceResponse.builder()
                .id(id)
                .studentId(attendance.getStudent().getId())
                .studentName(attendance.getStudent().getFullName())
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .note(attendance.getNote())
                .createdBy(attendance.getCreatedBy().getId())
                .updatedAt(attendance.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow(
                () -> new AttendanceNotFoundException("Student not found with id:" + id)
        );
        attendanceRepository.delete(attendance);
    }

    @Override
    public List<AttendanceDateResponse> getAttendanceDates() {
        return List.of();
    }

    @Override
    public Page<AttendanceDetailResponse> getAttendanceByDate(LocalDate date, Pageable pageable) {
        return null;
    }

    @Override
    public List<AttendanceStatusSummaryResponse> getStatusSummaryByDate(LocalDate date) {
        return List.of();
    }

    @Override
    public List<TeacherAttendanceSummaryResponse> getTeacherAttendanceSummary(Long teacherId, LocalDate date) {
        return List.of();
    }

    @Override
    public Page<ClassAttendanceResponse> getClassAttendance(Long classId, LocalDate date, Pageable pageable) {
        return null;
    }

    @Override
    public Page<StudentAttendanceHistoryResponse> getStudentHistory(Long studentId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<StudentResponse> getStudentsNotYetAttendance(LocalDate date, Pageable pageable) {
        return null;
    }

    @Override
    public List<TopAbsentStudentResponse> getTopAbsentStudents() {
        return List.of();
    }

    @Override
    public List<ClassAttendanceRateResponse> getAttendanceRate(LocalDate date) {
        return List.of();
    }

    @Override
    public Page<AttendanceFilterResponse> filterByStatus(LocalDate date, String status, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ClassAttendanceHistoryResponse> getClassAttendanceHistory(Long classId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return null;
    }

    @Override
    public StudentAttendanceStatisticResponse getStudentStatistic(Long studentId) {
        return null;
    }
}
