package KidAttend.demo.specification;

import KidAttend.demo.entity.ClassEntity;
import KidAttend.demo.entity.ClassStatus;
import org.springframework.data.jpa.domain.Specification;

public class ClassSpecification {

    public static Specification<ClassEntity> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<ClassEntity> hasAge(Integer age) {
        return (root, query, cb) -> {
            if (age == null) return null;
            return cb.equal(root.get("age"), age);
        };
    }

    public static Specification<ClassEntity> hasStatus(ClassStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<ClassEntity> hasTeacher(Long teacherId) {
        return (root, query, cb) -> {
            if (teacherId == null) return null;
            return cb.equal(root.get("teacher").get("id"), teacherId);
        };
    }

    public static Specification<ClassEntity> fullText(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String like = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("description")), like)
            );
        };
    }
}