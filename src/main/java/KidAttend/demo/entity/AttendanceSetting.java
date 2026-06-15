package KidAttend.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalTime startTime; // 07:00

    private LocalTime endTime;   // 08:30

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    private LocalDateTime updatedAt = LocalDateTime.now();
}
