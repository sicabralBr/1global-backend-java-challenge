package com.simonecabral._global_backend_java_challenge.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByBrand(String brand);

    List<Device> findByState(String state);
}
