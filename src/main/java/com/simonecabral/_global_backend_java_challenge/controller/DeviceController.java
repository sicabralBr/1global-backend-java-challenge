package com.simonecabral._global_backend_java_challenge.controller;

import com.simonecabral._global_backend_java_challenge.domain.Device;
import com.simonecabral._global_backend_java_challenge.domain.DeviceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping
    public List<Device> fetchAllDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> fetchSingleDevice(@PathVariable Long id) {
        return deviceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/brand/{brand}")
    public List<Device> fetchByBrand(@PathVariable String brand) {
        return deviceRepository.findByBrand(brand);
    }

    @GetMapping("/state/{state}")
    public List<Device> fetchByState(@PathVariable String state) {
        return deviceRepository.findByState(state);
    }

    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody @Valid Device device) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceRepository.save(device));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDevice(@PathVariable Long id, @RequestBody Device device) {
        return deviceRepository.findById(id)
                .map(existing -> {
                    if ("IN_USE".equalsIgnoreCase(device.getState())){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body((Object) "Devices in state IN_USE cannot be deleted");
                    }
                    existing.setName(device.getName());
                    existing.setBrand(device.getBrand());
                    existing.setState(device.getState());
                    return ResponseEntity.ok((Object) deviceRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDevice(@PathVariable Long id) {
        return deviceRepository.findById(id)
                .map(device -> {
                    if ("IN_USE".equalsIgnoreCase(device.getState())){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body((Object) "Devices in state IN_USE cannot be deleted");
                    }
                    deviceRepository.delete(device);
                    return (ResponseEntity<Object>) ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Device not found"));
    }
}
