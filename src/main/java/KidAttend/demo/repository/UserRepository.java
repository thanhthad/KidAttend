package KidAttend.demo.repository;


import KidAttend.demo.dto.response.user.UserResponse;
import KidAttend.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("""
    SELECT new KidAttend.demo.dto.response.user.UserResponse(
        u.id,
        u.fullName,
        u.phone,
        u.email
    )
    FROM User u
    WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))
""")
    Page<UserResponse> findByFullName(@Param("fullName") String fullName, Pageable pageable);
    @Query("""
    SELECT new KidAttend.demo.dto.response.user.UserResponse(
        u.id,
        u.fullName,
        u.phone,
        u.email
    )
    FROM User u
    WHERE LOWER(u.phone) LIKE LOWER(CONCAT('%', :phone, '%'))
""")
    Page<UserResponse> findByPhone(@Param("phone") String phone, Pageable pageable);

    @Query("""
    SELECT new KidAttend.demo.dto.response.user.UserResponse(
        u.id,
        u.fullName,
        u.phone,
        u.email
    )
    FROM User u
""")
    Page<UserResponse> findAllUsers(Pageable pageable);

}
