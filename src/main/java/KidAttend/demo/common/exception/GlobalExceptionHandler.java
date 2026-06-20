package KidAttend.demo.common.exception;
import KidAttend.demo.common.response.ApiResponse;
import KidAttend.demo.common.response.ResponseData;
import KidAttend.demo.exception.classroom.ClassRoomAlreadyExistsException;
import KidAttend.demo.exception.classroom.ClassRoomNotFoundException;
import KidAttend.demo.exception.refreshtoken.InvalidRefreshTokenException;
import KidAttend.demo.exception.refreshtoken.RefreshTokenExpiredException;
import KidAttend.demo.exception.refreshtoken.RefreshTokenRevokedException;
import KidAttend.demo.exception.student.StudentAlreadyExistsException;
import KidAttend.demo.exception.student.StudentNotFoundException;
import KidAttend.demo.exception.user.EmailAlreadyExistsException;
import KidAttend.demo.exception.user.PhoneAlreadyExistsException;
import KidAttend.demo.exception.user.UserAlreadyExistsException;
import KidAttend.demo.exception.user.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ================= USER =================
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseData.fail(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseData.fail(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseData.fail(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handlePhoneExists(PhoneAlreadyExistsException ex) {
        return ResponseData.fail(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // ================= CLASSROOM =================
    @ExceptionHandler(ClassRoomNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleClassRoomNotFound(ClassRoomNotFoundException ex) {
        return ResponseData.fail(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClassRoomAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleClassRoomAlreadyExists(ClassRoomAlreadyExistsException ex) {
        return ResponseData.fail(ex.getMessage(), HttpStatus.CONFLICT);
    }


    // ================= STUDENT =================
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleStudentNotFound(StudentNotFoundException ex) {
        return ResponseData.fail(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleStudentAlreadyExists(StudentAlreadyExistsException ex) {
        return ResponseData.fail(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // ================= REFRESH TOKEN =================
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidRefreshToken(InvalidRefreshTokenException ex) {
        return ResponseData.fail("Invalid refresh token", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleExpiredRefreshToken(RefreshTokenExpiredException ex) {
        return ResponseData.fail("Refresh token expired", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenRevokedException.class)
    public ResponseEntity<ApiResponse<Object>> handleRevokedRefreshToken(RefreshTokenRevokedException ex) {
        return ResponseData.fail("Refresh token revoked", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseData.fail("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    // ================= VALIDATION =================
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseData.fail("Invalid parameter type", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Validation failed");
        response.setData(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Invalid argument");
        response.setData(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleJsonParseError(
            HttpMessageNotReadableException ex) {

        return ResponseData.fail(
                "Invalid request body format",
                HttpStatus.BAD_REQUEST
        );
    }

    // ================= FALLBACK (ONLY IMPORTANT LOG) =================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {

        log.error("Exception type: {}", ex.getClass().getName(), ex);

        return ResponseData.fail(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}