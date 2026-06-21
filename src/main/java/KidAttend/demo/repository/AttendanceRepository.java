package KidAttend.demo.repository;

import KidAttend.demo.entity.Attendance;
import KidAttend.demo.entity.Student;
import KidAttend.demo.repository.projection.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("""
        SELECT DISTINCT a.attendanceDate as attendanceDate
        FROM Attendance a
        ORDER BY a.attendanceDate
       """)
    List<AttendanceDateProjection> getAttendanceDates();

    @Query("""
SELECT
    s.id as studentId,
    s.fullName as studentName,
    c.name as className,
    a.status as status
FROM Attendance a
JOIN a.student s
JOIN s.classEntity c
WHERE a.attendanceDate = :date
""")
    Page<AttendanceDetailProjection> getAttendanceByDate(
            LocalDate date,
            Pageable pageable);

    @Query("""
        SELECT
            a.status as status,
            COUNT(a.id) as total
        FROM Attendance a
        WHERE a.attendanceDate = :date
        GROUP BY a.status
       """)
    List<AttendanceStatusSummaryProjection>
    getStatusSummaryByDate(LocalDate date);

    @Query("""
    SELECT
        c.name as className,
        COUNT(a.id) as totalAttendance,
        SUM(CASE WHEN a.status='PRESENT' THEN 1 ELSE 0 END) as present,
        SUM(CASE WHEN a.status='ABSENT' THEN 1 ELSE 0 END) as absent
    FROM ClassEntity c
    JOIN Student s ON s.classEntity.id = c.id
    JOIN Attendance a ON a.student.id = s.id
    WHERE c.teacher.id = :teacherId
    AND a.attendanceDate = :date
    GROUP BY c.name
    """)
    List<TeacherAttendanceSummaryProjection>
    getTeacherAttendanceSummary(Long teacherId,
                                LocalDate date);


    @Query("""
    SELECT
        s.id as studentId,
        s.fullName as studentName,
        a.status as status,
        a.note as note
    FROM Student s
    LEFT JOIN Attendance a
    ON a.student.id=s.id
    AND a.attendanceDate=:date
    WHERE s.classEntity.id=:classId
    """)
    Page<ClassAttendanceProjection>
    getClassAttendance(
            Long classId,
            LocalDate date,
            Pageable pageable);

    @Query("""
    SELECT
        a.attendanceDate as attendanceDate,
        a.status as status,
        a.note as note
    FROM Attendance a
    WHERE a.student.id=:studentId
    """)
    Page<StudentAttendanceHistoryProjection>
    getStudentHistory(
            Long studentId,
            Pageable pageable);

    @Query("""
    SELECT
        s.id as studentId,
        s.fullName as studentName,
        COUNT(a.id) as absentDays
    FROM Attendance a
    JOIN a.student s
    WHERE a.status='ABSENT'
    GROUP BY s.id,s.fullName
    ORDER BY absentDays DESC
    """)
    List<TopAbsentStudentProjection>
    findTopAbsentStudents(Pageable pageable);

    @Query("""
SELECT s
FROM Student s
WHERE NOT EXISTS(
    SELECT a.id
    FROM Attendance a
    WHERE a.student.id=s.id
    AND a.attendanceDate=:date
)
""")
    Page<Student> getStudentsNotYetAttendance(
            LocalDate date,
            Pageable pageable);

    @Query("""
SELECT
    a.student.id as studentId,
    SUM(CASE WHEN a.status='PRESENT' THEN 1 ELSE 0 END) as presentDays,
    SUM(CASE WHEN a.status='ABSENT' THEN 1 ELSE 0 END) as absentDays,
    SUM(CASE WHEN a.status='LATE' THEN 1 ELSE 0 END) as lateDays
FROM Attendance a
WHERE a.student.id=:studentId
GROUP BY a.student.id
""")
    StudentAttendanceStatisticProjection
    getStudentStatistic(Long studentId);

    @Query("""
SELECT
    c.id as classId,
    c.name as className,
    COUNT(a.id) as total,
    SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) as present
FROM ClassEntity c
JOIN Student s ON s.classEntity.id = c.id
JOIN Attendance a ON a.student.id = s.id
WHERE a.attendanceDate = :date
GROUP BY c.id, c.name
""")
    List<ClassAttendanceRateProjection> getAttendanceRate(
            LocalDate date);

    @Query("""
SELECT
    s.fullName as studentName,
    a.status as status,
    a.note as note
FROM Attendance a
JOIN a.student s
WHERE a.attendanceDate = :date
AND a.status = :status
""")
    Page<AttendanceFilterProjection> filterByStatus(
            LocalDate date,
            String status,
            Pageable pageable);

    @Query("""
SELECT
    s.fullName as studentName,
    a.attendanceDate as attendanceDate,
    a.status as status
FROM Student s
JOIN Attendance a
ON a.student.id = s.id
WHERE s.classEntity.id = :classId
AND a.attendanceDate BETWEEN :fromDate AND :toDate
""")
    Page<ClassAttendanceHistoryProjection>
    getClassAttendanceHistory(
            Long classId,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable);
}
