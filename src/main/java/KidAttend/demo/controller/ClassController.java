package KidAttend.demo.controller;

import KidAttend.demo.common.response.ResponseData;
import KidAttend.demo.dto.request.classroom.ClassSearchRequest;
import KidAttend.demo.dto.request.classroom.CreateClassRequest;
import KidAttend.demo.dto.request.classroom.UpdateClassRequest;
import KidAttend.demo.dto.response.classroom.ClassResponse;
import KidAttend.demo.dto.response.user.UserResponse;
import KidAttend.demo.service.ClassService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Class Management", description = "Class APIs")
public class ClassController {

    private final ClassService classService;

    // ================= GET CLASS BY TEACHER =================
    @GetMapping("/teacher/me")
    public ResponseEntity<?> getClassByMe() {

        ClassResponse response =
                classService.getByClassByMe();

        return ResponseData.success(
                response,
                "Lấy thông tin lớp học thành công",
                HttpStatus.OK
        );
    }

    // ================= GET ALL CLASSES =================
    @GetMapping
    public ResponseEntity<?> getAllClasses(Pageable pageable) {

        Page<ClassResponse> response =
                classService.getAll(pageable);

        return ResponseData.success(
                response,
                "Lấy danh sách lớp học thành công",
                HttpStatus.OK
        );
    }

    // ================= SEARCH CLASSES =================
    @GetMapping("/search")
    public ResponseEntity<?> searchClasses(
            @Valid ClassSearchRequest request,
            Pageable pageable
    ) {

        Page<ClassResponse> response =
                classService.search(request, pageable);

        return ResponseData.success(
                response,
                "Tìm kiếm lớp học thành công",
                HttpStatus.OK
        );
    }

    // ================= GET CLASS BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<?> getClassById(@PathVariable Long id) {

        ClassResponse response =
                classService.getById(id);

        return ResponseData.success(
                response,
                "Lấy thông tin lớp học thành công",
                HttpStatus.OK
        );
    }

    // ================= GET CLASS BY TEACHER ID =================
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getClassByTeacherId(@PathVariable Long teacherId) {

        ClassResponse response =
                classService.getByTeacherId(teacherId);

        return ResponseData.success(
                response,
                "Lấy thông tin lớp theo giáo viên thành công",
                HttpStatus.OK
        );
    }

    // ================= CREATE CLASS =================
    @PostMapping
    public ResponseEntity<?> createClass(
            @Valid @RequestBody CreateClassRequest request
    ) {

        ClassResponse response =
                classService.create(request);

        return ResponseData.success(
                response,
                "Tạo lớp học thành công",
                HttpStatus.CREATED
        );
    }

    // ================= GET UNASSIGNED TEACHERS =================
    @GetMapping("/teachers/unassigned")
    public ResponseEntity<?> getUnassignedTeachers() {

        List<UserResponse> response = classService.getUnassignedTeachers();

        return ResponseData.success(
                response,
                "Lấy danh sách giáo viên chưa được phân lớp thành công",
                HttpStatus.OK
        );
    }

    // ================= UPDATE CLASS =================
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateClass(
            @PathVariable Long id,
            @RequestBody UpdateClassRequest request
    ) {

        ClassResponse response = classService.update(id, request);

        return ResponseData.success(
                response,
                "Cập nhật lớp học thành công",
                HttpStatus.OK
        );
    }

    // ================= DELETE CLASS =================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {

        classService.delete(id);

        return ResponseData.success(
                null,
                "Xóa lớp học thành công",
                HttpStatus.OK
        );
    }
}