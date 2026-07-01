package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.attendance.CreateAttendanceRequest;
import KidAttend.demo.dto.request.attendance.UpdateAttendanceRequest;
import KidAttend.demo.dto.response.attendance.*;
import KidAttend.demo.dto.response.student.StudentResponse;
import KidAttend.demo.entity.Attendance;
import KidAttend.demo.entity.AttendanceSetting;
import KidAttend.demo.entity.Student;
import KidAttend.demo.entity.User;
import KidAttend.demo.exception.attendance.AttendanceAlreadyExistsException;
import KidAttend.demo.exception.attendance.AttendanceNotFoundException;
import KidAttend.demo.exception.attendancesetting.AttendanceSettingNotFoundException;
import KidAttend.demo.exception.classroom.ClassRoomNotFoundException;
import KidAttend.demo.exception.student.StudentNotFoundException;
import KidAttend.demo.repository.AttendanceRepository;
import KidAttend.demo.repository.AttendanceSettingRepository;
import KidAttend.demo.repository.ClassRepository;
import KidAttend.demo.repository.StudentRepository;
import KidAttend.demo.repository.projection.*;
import KidAttend.demo.security.userdetails.SecurityUtils;
import KidAttend.demo.service.AttendanceService;
import KidAttend.demo.service.UserServiceDomain;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final UserServiceDomain userServiceDomain;
    private final ClassRepository classRepository;
    private AttendanceSettingRepository attendanceSettingRepository;

    @Override
    @Transactional
    public List<AttendanceResponse> init() {

        Long userId = SecurityUtils.getCurrentUserId();

        Long classId = classRepository.findByTeacher_Id(userId)
                .orElseThrow(() -> new ClassRoomNotFoundException("Giáo viên chưa được phân lớp"))
                .getId();

        LocalDate today = LocalDate.now();

        User teacher = userServiceDomain.getByUserId(userId);

        List<Student> students =
                studentRepository.findAllByClassEntity_IdOrderByFullNameAsc(classId);

        List<Attendance> existing = attendanceRepository
                .findByClassAndDate(classId, today);

        Set<Long> existingIds = existing.stream()
                .map(a -> a.getStudent().getId())
                .collect(Collectors.toSet());

        for (Student s : students) {

            if (existingIds.contains(s.getId())) {
                continue;
            }

            attendanceRepository.insertIgnoreConflict(
                    s.getId(),
                    today,
                    "ABSENT",
                    "",
                    teacher.getId()
            );
        }

        List<Attendance> result = attendanceRepository
                .findByClassAndDate(classId, today);

        return result.stream()
                .map(a -> AttendanceResponse.builder()
                        .id(a.getId())
                        .studentId(a.getStudent().getId())
                        .studentName(a.getStudent().getFullName())
                        .attendanceDate(a.getAttendanceDate())
                        .status(a.getStatus())
                        .note(a.getNote())
                        .createdBy(a.getCreatedBy().getId())
                        .createdAt(a.getCreatedAt())
                        .updatedAt(a.getUpdatedAt())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public List<AttendanceResponse> batchUpdate(List<UpdateAttendanceRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            throw new AttendanceNotFoundException("Không tìm thấy dữ liệu điểm danh");
        }

        LocalDate today = LocalDate.now();

        AttendanceSetting setting = attendanceSettingRepository.findById(1L)
                .orElseThrow(() -> new AttendanceSettingNotFoundException("Chưa cấu hình thời gian điểm danh"));

        LocalTime now = LocalTime.now();

        if (now.isAfter(setting.getEndTime())) {
            throw new IllegalArgumentException("Đã hết thời gian điểm danh");
        }

        List<Long> attendanceIds = requests.stream()
                .map(UpdateAttendanceRequest::getAttendanceId)
                .toList();

        Map<Long, Attendance> attendanceMap = attendanceRepository
                .findAllById(attendanceIds)
                .stream()
                .collect(Collectors.toMap(
                        Attendance::getId,
                        Function.identity()
                ));

        List<AttendanceResponse> responses = new ArrayList<>();

        for (UpdateAttendanceRequest request : requests) {

            Attendance attendance = attendanceMap.get(request.getAttendanceId());

            if (attendance == null) {
                throw new AttendanceNotFoundException(
                        "Không tìm thấy điểm danh với id: "
                                + request.getAttendanceId());
            }

            if (!attendance.getAttendanceDate().isEqual(today)) {
                throw new IllegalArgumentException(
                        "Không thể cập nhật điểm danh của ngày trước");
            }

            attendance.setStatus(request.getStatus());
            attendance.setNote(request.getNote());
            attendance.setUpdatedAt(LocalDateTime.now());

            responses.add(
                    AttendanceResponse.builder()
                            .id(attendance.getId())
                            .studentId(attendance.getStudent().getId())
                            .studentName(attendance.getStudent().getFullName())
                            .attendanceDate(attendance.getAttendanceDate())
                            .status(attendance.getStatus())
                            .note(attendance.getNote())
                            .createdBy(attendance.getCreatedBy().getId())
                            .createdAt(attendance.getCreatedAt())
                            .updatedAt(attendance.getUpdatedAt())
                            .build()
            );
        }

        return responses;
    }

    @Override
    @Transactional
    public AttendanceResponse update(Long id, UpdateAttendanceRequest request) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException(
                                "Không tìm thấy điểm danh với id: " + id));

        LocalDate today = LocalDate.now();

        if (!attendance.getAttendanceDate().isEqual(today)) {
            throw new IllegalArgumentException("Không thể cập nhật điểm danh của ngày trước");
        }

        AttendanceSetting setting = attendanceSettingRepository.findById(1L)
                .orElseThrow(() -> new AttendanceSettingNotFoundException("Chưa cấu hình thời gian điểm danh"));

        LocalTime now = LocalTime.now();

        if (now.isAfter(setting.getEndTime())) {
            throw new IllegalArgumentException("Đã hết thời gian điểm danh");
        }

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
                () -> new AttendanceNotFoundException("Không tìm thấy học sinh với id: " + id)
        );
        attendanceRepository.delete(attendance);
    }

    @Override
    public List<AttendanceDateResponse> getAttendanceDates() {
        List<AttendanceDateProjection> projections =
                attendanceRepository.getAttendanceDates();

        return projections.stream()
                .map(p -> new AttendanceDateResponse(p.getAttendanceDate()))
                .toList();
    }

    @Override
    public List<AttendanceDateResponse> getAttendanceDatesByClassId() {

        Long userId = SecurityUtils.getCurrentUserId();

        Long classId = classRepository.findByTeacher_Id(userId)
                .orElseThrow(() ->
                        new ClassRoomNotFoundException("Giáo viên chưa được phân lớp"))
                .getId();

        return attendanceRepository
                .getAttendanceDatesByClassId(classId)
                .stream()
                .map(p -> new AttendanceDateResponse(p.getAttendanceDate()))
                .toList();
    }

    @Override
    public Page<AttendanceDetailResponse> getAttendanceByDate(
            LocalDate date,
            Pageable pageable
    ) {

        return attendanceRepository
                .getAttendanceByDate(date, pageable)
                .map(p -> new AttendanceDetailResponse(
                        p.getStudentId(),
                        p.getStudentName(),
                        p.getClassName(),
                        p.getStatus()
                ));
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
                    "Không tìm thấy lớp với id: " + classId
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
    public List<TeacherAttendanceSummaryResponse> getTeacherAttendanceSummaryMe(LocalDate date) {

        Long userId = SecurityUtils.getCurrentUserId();

        List<TeacherAttendanceSummaryProjection> result =
                attendanceRepository.getTeacherAttendanceSummary(
                        userId,
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
    public List<ClassAttendanceResponse> getClassAttendance(LocalDate date) {

        Long userId = SecurityUtils.getCurrentUserId();

        Long classId = classRepository.findByTeacher_Id(userId)
                .orElseThrow(() ->
                        new ClassRoomNotFoundException("Giáo viên chưa được phân lớp"))
                .getId();

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Không thể chọn ngày trong tương lai"
            );
        }

        List<ClassAttendanceProjection> projections =
                attendanceRepository.getClassAttendance(classId, date);

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
    public List<StudentAttendanceHistoryResponse> getStudentHistory(Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Không tìm thấy học sinh với id: " + studentId
            );
        }

        return attendanceRepository
                .getStudentHistory(studentId)
                .stream()
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
                    "Không tìm thấy lớp với id: " + classId
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

        StudentAttendanceStatisticProjection projection =
                attendanceRepository.getStudentStatistic(studentId);

        if (projection == null) {
            return new StudentAttendanceStatisticResponse(
                    studentId,
                    0L,
                    0L
            );
        }

        return new StudentAttendanceStatisticResponse(
                projection.getStudentId(),
                projection.getPresentDays() == null ? 0L : projection.getPresentDays(),
                projection.getAbsentDays() == null ? 0L : projection.getAbsentDays()
        );
    }
}