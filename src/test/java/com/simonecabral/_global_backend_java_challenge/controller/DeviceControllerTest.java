package com.simonecabral._global_backend_java_challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonecabral._global_backend_java_challenge.domain.Device;
import com.simonecabral._global_backend_java_challenge.domain.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeviceRepository deviceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllDevices() throws Exception {
        Device device1 = new Device(1L, "iPhone", "Apple", "AVAILABLE", null);
        Device device2 = new Device(2L, "Galaxy", "Samsung", "IN_USE", null);

        Mockito.when(deviceRepository.findAll()).thenReturn(Arrays.asList(device1, device2));

        mockMvc.perform(get("/device"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void shouldReturnDeviceById() throws Exception {
        Device device = new Device(1L, "iPhone", "Apple", "AVAILABLE", null);
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        mockMvc.perform(get("/device/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone"));
    }

    @Test
    void shouldReturnNotFoundIfDeviceNotExists() throws Exception {
        Mockito.when(deviceRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/device/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnDevicesByBrand() throws Exception {
        Mockito.when(deviceRepository.findByBrand("Apple"))
                .thenReturn(List.of(new Device(1L, "iPhone", "Apple", "AVAILABLE", null)));

        mockMvc.perform(get("/device/brand/Apple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void shouldReturnDevicesByState() throws Exception {
        Mockito.when(deviceRepository.findByState("AVAILABLE"))
                .thenReturn(List.of(new Device(1L, "iPhone", "Apple", "AVAILABLE", null)));

        mockMvc.perform(get("/device/state/AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void shouldCreateDevice() throws Exception {
        Device input = new Device(null, "Pixel", "Google", "AVAILABLE", null);
        Device saved = new Device(3L, "Pixel", "Google", "AVAILABLE", null);

        Mockito.when(deviceRepository.save(any(Device.class))).thenReturn(saved);

        mockMvc.perform(post("/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void shouldUpdateDevice() throws Exception {
        Device existing = new Device(1L, "Old", "Brand", "IN_USE", null);
        Device update = new Device(1L, "Updated", "Apple", "AVAILABLE", null);

        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(deviceRepository.save(any(Device.class))).thenReturn(update);

        mockMvc.perform(put("/device/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void shouldBlockUpdateIfStateIsInUse() throws Exception {
        Device existing = new Device(1L, "Old", "Brand", "AVAILABLE", null);
        Device update = new Device(1L, "Updated", "Apple", "IN_USE", null);

        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));

        mockMvc.perform(put("/device/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Devices in state IN_USE cannot be deleted"));
    }

    @Test
    void shouldDeleteDevice() throws Exception {
        Device existing = new Device(1L, "Moto G", "Motorola", "INACTIVE", null);

        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));

        mockMvc.perform(delete("/device/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldBlockDeletionIfStateIsInUse() throws Exception {
        Device existing = new Device(1L, "Moto G", "Motorola", "IN_USE", null);

        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));

        mockMvc.perform(delete("/device/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Devices in state IN_USE cannot be deleted"));
    }
}
