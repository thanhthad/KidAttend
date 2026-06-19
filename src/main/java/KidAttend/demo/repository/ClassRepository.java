package KidAttend.demo.repository;

import KidAttend.demo.entity.ClassEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClassRepository extends JpaRepository<ClassEntity, Long>,
        JpaSpecificationExecutor<ClassEntity> {

    boolean existsByTeacherId(Long teacherId);
}