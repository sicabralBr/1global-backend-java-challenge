package com.simonecabral._global_backend_java_challenge.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "DEVICE")
@Entity(name = "DEVICE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private String state;

    private LocalDateTime creationTimeFinal;

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", state='" + state + '\'' +
                ", creationTimeFinal=" + creationTimeFinal +
                '}';
    }
}
