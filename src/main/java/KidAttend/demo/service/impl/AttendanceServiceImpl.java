package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.attendance.CreateAttendanceRequest;
import KidAttend.demo.dto.request.attendance.UpdateAttendanceRequest;
import KidAttend.demo.dto.response.attendance.*;
import KidAttend.demo.dto.response.student.StudentResponse;
import KidAttend.demo.entity.Attendance;
import KidAttend.demo.entity.ClassEntity;
import KidAttend.demo.entity.Student;
import KidAttend.demo.entity.User;
import KidAttend.demo.exception.attendance.AttendanceAlreadyExistsException;
import KidAttend.demo.exception.attendance.AttendanceNotFoundException;
import KidAttend.demo.exception.classroom.ClassRoomNotFoundException;
import KidAttend.demo.exception.student.StudentNotFoundException;
import KidAttend.demo.repository.AttendanceRepository;
import KidAttend.demo.repository.ClassRepository;
import KidAttend.demo.repository.StudentRepository;
import KidAttend.demo.repository.projection.*;
import KidAttend.demo.service.AttendanceService;
import KidAttend.demo.service.UserServiceDomain;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final ClassRepository classRepository;

    @Override
    @Transactional
    public AttendanceResponse create(CreateAttendanceRequest request) {
        if(attendanceRepository.existsByStudent_IdAndAttendanceDate(
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

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException(
                                "Attendance not found with id: " + id));

        attendance.setStatus(request.getStatus());
        attendance.setNote(request.getNote());

        return AttendanceResponse.builder()
                .id(attendance.getId())
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

        List<AttendanceDateProjection> projections =
                attendanceRepository.getAttendanceDates();

        return projections.stream()
                .map(p -> new AttendanceDateResponse(
                        p.getAttendanceDate()
                ))
                .toList();
    }

    @Override
    public List<AttendanceDateResponse> getAttendanceDatesByClassId(Long classId) {

        if (!classRepository.existsById(classId)) {
            throw new ClassRoomNotFoundException(
                    "Class not found with id: " + classId
            );
        }

        List<AttendanceDateProjection> projections =
                attendanceRepository.getAttendanceDatesByClassId(classId);

        return projections.stream()
                .map(p -> new AttendanceDateResponse(
                        p.getAttendanceDate()
                ))
                .toList();
    }

    @Override
    public List<AttendanceDetailResponse> getAttendanceByDate(LocalDate date) {

        List<AttendanceDetailProjection> result =
                attendanceRepository.getAttendanceByDate(date);

        return result.stream()
                .map(p -> new AttendanceDetailResponse(
                        p.getStudentId(),
                        p.getStudentName(),
                        p.getClassName(),
                        p.getStatus()
                ))
                .toList();
    }

    @Override
    public List<AttendanceStatusSummaryResponse> getStatusSummaryByDate(LocalDate date) {

        List<AttendanceStatusSummaryProjection> result =
                attendanceRepository.getStatusSummaryByDate(date);

        return result.stream()
                .map(p -> new AttendanceStatusSummaryResponse(
                        p.getStatus(),
                        p.getTotal()
                ))
                .toList();
    }

    @Override
    public List<AttendanceStatusSummaryResponse> getStatusSummaryByDateAndClass(
            Long classId,
            LocalDate date
    ) {

        if (!classRepository.existsById(classId)) {
            throw new ClassRoomNotFoundException(
                    "Class not found with id: " + classId
            );
        }

        List<AttendanceStatusSummaryProjection> result =
                attendanceRepository.getStatusSummaryByDateAndClass(classId, date);

        return result.stream()
                .map(p -> new AttendanceStatusSummaryResponse(
                        p.getStatus(),
                        p.getTotal()
                ))
                .toList();
    }


    @Override
    public List<TeacherAttendanceSummaryResponse> getTeacherAttendanceSummary(
            Long teacherId,
            LocalDate date
    ) {

        User teacher = userServiceDomain.getByUserId(teacherId);

        List<TeacherAttendanceSummaryProjection> result =
                attendanceRepository.getTeacherAttendanceSummary(
                        teacherId,
                        date
                );

        return result.stream()
                .map(p -> new TeacherAttendanceSummaryResponse(
                        p.getClassName(),
                        p.getTotalAttendance(),
                        p.getPresent(),
                        p.getAbsent()
                ))
                .toList();
    }

    @Override
    public List<ClassAttendanceResponse> getClassAttendance(Long classId, LocalDate date) {

        if (!classRepository.existsById(classId)) {
            throw new ClassRoomNotFoundException(
                    "Class not found with id: " + classId
            );
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date cannot be in the future"
            );
        }

        List<ClassAttendanceProjection> projections =
                attendanceRepository.getClassAttendance(classId, date);

        // 4. Map projection -> response DTO
        return projections.stream()
                .map(p -> new ClassAttendanceResponse(
                        p.getStudentId(),
                        p.getStudentName(),
                        p.getStatus(),
                        p.getNote()
                ))
                .toList();
    }

    @Override
    public List<StudentAttendanceHistoryResponse> getStudentHistory(
            Long studentId
    ) {

        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Student not found with id: " + studentId
            );
        }

        List<StudentAttendanceHistoryProjection> result =
                attendanceRepository.getStudentHistory(studentId);

        return result.stream()
                .map(p -> new StudentAttendanceHistoryResponse(
                        p.getAttendanceDate(),
                        p.getStatus(),
                        p.getNote()
                ))
                .toList();
    }

    @Override
    public Page<StudentResponse> getStudentsNotYetAttendance(
            LocalDate date,
            Pageable pageable
    ) {

        Page<Student> students =
                attendanceRepository.getStudentsNotYetAttendance(
                        date,
                        pageable
                );

        return students.map(student ->
                StudentResponse.builder()
                        .id(student.getId())
                        .classId(student.getClassEntity().getId())
                        .className(student.getClassEntity().getName())
                        .fullName(student.getFullName())
                        .gender(student.getGender())
                        .dateOfBirth(student.getDateOfBirth())
                        .parentName(student.getParentName())
                        .parentPhone(student.getParentPhone())
                        .parentEmail(student.getParentEmail())
                        .address(student.getAddress())
                        .status(student.getStatus())
                        .build()
        );
    }

    @Override
    public List<TopAbsentStudentResponse> getTopAbsentStudents() {

        List<TopAbsentStudentProjection> result =
                attendanceRepository.findTopAbsentStudents(
                        PageRequest.of(0, 10)
                );

        return result.stream()
                .map(p -> new TopAbsentStudentResponse(
                        p.getStudentId(),
                        p.getStudentName(),
                        p.getAbsentDays()
                ))
                .toList();
    }

    @Override
    public List<ClassAttendanceRateResponse> getAttendanceRate(LocalDate date) {

        List<ClassAttendanceRateProjection> result =
                attendanceRepository.getAttendanceRate(date);

        return result.stream()
                .map(p -> new ClassAttendanceRateResponse(
                        p.getClassId(),
                        p.getClassName(),
                        p.getTotal(),
                        p.getPresent(),
                        p.getRate()
                ))
                .toList();
    }

    @Override
    public Page<AttendanceFilterResponse> filterByStatus(
            LocalDate date,
            String status,
            Pageable pageable
    ) {

        Page<AttendanceFilterProjection> result =
                attendanceRepository.filterByStatus(
                        date,
                        status,
                        pageable
                );

        return result.map(p -> new AttendanceFilterResponse(
                p.getClassName(),
                p.getStudentName(),
                p.getStatus(),
                p.getNote()
        ));
    }

    @Override
    public Page<ClassAttendanceHistoryResponse> getClassAttendanceHistory(
            Long classId,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) {

        if (!classRepository.existsById(classId)) {
            throw new ClassRoomNotFoundException(
                    "Class not found with id: " + classId
            );
        }

        Page<ClassAttendanceHistoryProjection> result =
                attendanceRepository.getClassAttendanceHistory(
                        classId,
                        fromDate,
                        toDate,
                        pageable
                );

        return result.map(p -> new ClassAttendanceHistoryResponse(
                p.getStudentName(),
                p.getAttendanceDate(),
                p.getStatus()
        ));
    }

    @Override
    public StudentAttendanceStatisticResponse getStudentStatistic(Long studentId) {
        return null;
    }
}
