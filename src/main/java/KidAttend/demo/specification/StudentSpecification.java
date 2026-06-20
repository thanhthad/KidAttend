package KidAttend.demo.specification;

import KidAttend.demo.entity.Student;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {

    public static Specification<Student> filter(Long classId, String name, String address) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (classId != null) {
                predicates.add(cb.equal(root.get("classEntity").get("id"), classId));
            }

            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("fullName")),
                                "%" + name.toLowerCase() + "%")
                );
            }

            if (address != null && !address.isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("address")),
                                "%" + address.toLowerCase() + "%")
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}