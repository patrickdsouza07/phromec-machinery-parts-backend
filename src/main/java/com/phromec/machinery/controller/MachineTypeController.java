package com.phromec.machinery.controller;

import com.phromec.machinery.dto.machine.MachineTypeListItemResponse;
import com.phromec.machinery.dto.machine.CreateMachineTypeRequest;
import com.phromec.machinery.model.machine.MachineType;
import com.phromec.machinery.service.MachineTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/machine-types")
@RequiredArgsConstructor
public class MachineTypeController {

    private final MachineTypeService machineTypeService;

    @GetMapping
    public ResponseEntity<List<MachineTypeListItemResponse>> getMachineTypes() {
        return ResponseEntity.ok(machineTypeService.getMachineTypes());
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getMachineTypeNames() {
        return ResponseEntity.ok(machineTypeService.getMachineTypeNames());
    }

    @PostMapping
    public ResponseEntity<MachineType> createMachineType(
            @RequestBody CreateMachineTypeRequest request) {
        MachineType created = machineTypeService.createMachineType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{machineTypeId}")
    public ResponseEntity<Void> deleteMachineType(@PathVariable Integer machineTypeId) {
        machineTypeService.deleteMachineType(machineTypeId);
        return ResponseEntity.noContent().build();
    }
}
