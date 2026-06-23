package KidAttend.demo.repository;

import KidAttend.demo.entity.ClassEntity;
import KidAttend.demo.entity.ClassStatus;
import KidAttend.demo.repository.projection.ClassProjection;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long>,
        JpaSpecificationExecutor<ClassEntity> {

    boolean existsByTeacherId(Long teacherId);

    Optional<ClassEntity> findByTeacher_Id(Long teacherId);

    @Query("""
        SELECT
            c.id as id,
            c.name as name,
            c.age as age,
            c.capacity as capacity,
            c.description as description,
            c.status as status,

            t.id as teacherId,
            t.fullName as teacherName,
            t.email as teacherEmail,
            t.phone as teacherPhone,

            COUNT(s.id) as currentStudents

        FROM ClassEntity c
        LEFT JOIN c.teacher t
        LEFT JOIN Student s ON s.classEntity.id = c.id

        WHERE (:name IS NULL OR c.name ILIKE CONCAT('%', :name, '%'))
          AND (:age IS NULL OR c.age = :age)
          AND (:status IS NULL OR c.status = :status)
          AND (:teacherId IS NULL OR t.id = :teacherId)

        GROUP BY
            c.id, c.name, c.age, c.capacity, c.description, c.status,
            t.id, t.fullName, t.email, t.phone
""")
    Page<ClassProjection> searchClasses(
            @Param("name") String name,
            @Param("age") Integer age,
            @Param("status") ClassStatus status,
            @Param("teacherId") Long teacherId,
            Pageable pageable
    );

    @Query("""
SELECT
    c.id as id,
    c.name as name,
    c.age as age,
    c.capacity as capacity,
    c.description as description,
    c.status as status,

    t.id as teacherId,
    t.fullName as teacherName,
    t.email as teacherEmail,
    t.phone as teacherPhone,

    COUNT(s.id) as currentStudents

FROM ClassEntity c
LEFT JOIN c.teacher t
LEFT JOIN Student s ON s.classEntity.id = c.id

GROUP BY
    c.id, c.name, c.age, c.capacity, c.description, c.status,
    t.id, t.fullName, t.email, t.phone
""")
    Page<ClassProjection> findAllWithTeacherAndCount(Pageable pageable);
}