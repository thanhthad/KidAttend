package KidAttend.demo.controller;

import KidAttend.demo.common.response.ResponseData;
import KidAttend.demo.dto.request.student.BulkCreateStudentRequest;
import KidAttend.demo.dto.request.student.CreateStudentRequest;
import KidAttend.demo.dto.request.student.UpdateStudentRequest;
import KidAttend.demo.dto.response.student.StudentResponse;
import KidAttend.demo.service.StudentService;
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
@RequestMapping("/api/students")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Student Management", description = "Student APIs")
public class StudentController {

    private final StudentService studentService;

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        StudentResponse response = studentService.getById(id);

        return ResponseData.success(
                response,
                "Lấy thông tin học sinh thành công",
                HttpStatus.OK
        );
    }

    // ================= GET BY CLASS (ME) =================
    @GetMapping("/class/me")
    public ResponseEntity<?> getAllByMe() {

        List<StudentResponse> response =
                studentService.getAllByMe();

        return ResponseData.success(
                response,
                "Lấy danh sách học sinh thành công",
                HttpStatus.OK
        );
    }

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateStudentRequest request
    ) {

        StudentResponse response = studentService.create(request);

        return ResponseData.success(
                response,
                "Tạo học sinh thành công",
                HttpStatus.CREATED
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request
    ) {

        StudentResponse response = studentService.update(id, request);

        return ResponseData.success(
                response,
                "Cập nhật học sinh thành công",
                HttpStatus.OK
        );
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        studentService.delete(id);

        return ResponseData.success(
                null,
                "Xóa học sinh thành công",
                HttpStatus.OK
        );
    }

    // ================= SEARCH =================
    @GetMapping
    public ResponseEntity<?> search(
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            Pageable pageable
    ) {

        Page<StudentResponse> response =
                studentService.search(classId, name, address, pageable);

        return ResponseData.success(
                response,
                "Tìm kiếm học sinh thành công",
                HttpStatus.OK
        );
    }

    // ================= BULK CREATE =================
    @PostMapping("/bulk")
    public ResponseEntity<?> bulkCreate(
            @Valid @RequestBody BulkCreateStudentRequest request
    ) {

        List<StudentResponse> response =
                studentService.bulkCreate(request);

        return ResponseData.success(
                response,
                "Tạo hàng loạt học sinh thành công",
                HttpStatus.CREATED
        );
    }

    // ================= GET ALL (PAGING) =================
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(Pageable pageable) {

        Page<StudentResponse> response =
                studentService.getAll(pageable);

        return ResponseData.success(
                response,
                "Lấy danh sách học sinh thành công",
                HttpStatus.OK
        );
    }

    // ================= GET BY CLASS ID =================
    @GetMapping("/class/{classId}")
    public ResponseEntity<?> getAllByClass(@PathVariable Long classId) {

        List<StudentResponse> response =
                studentService.getAllByClassId(classId);

        return ResponseData.success(
                response,
                "Lấy danh sách học sinh theo lớp thành công",
                HttpStatus.OK
        );
    }
}