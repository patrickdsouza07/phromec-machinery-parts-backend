package com.phromec.machinery.controller;

import com.phromec.machinery.dto.machine.MachineListResponse;
import com.phromec.machinery.model.machine.MachineModel;
import com.phromec.machinery.service.MachineService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/machines")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;


    // ============================================================
    // LIST
    // ============================================================

    @GetMapping
    public ResponseEntity<MachineListResponse> getMachines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer machineTypeId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "machineName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction

    ) {

        MachineListResponse response =
                machineService.getMachines(
                        page,
                        size,
                        search,
                        machineTypeId,
                        status,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(response);
    }


    // ============================================================
    // GET BY ID
    // ============================================================

    @GetMapping("/{machineId}")
    public ResponseEntity<MachineModel> getMachineById(
            @PathVariable Integer machineId
    ) {

        return ResponseEntity.ok(
                machineService.getMachineById(
                        machineId
                )
        );
    }


    // ============================================================
    // CREATE
    // ============================================================

    @PostMapping
    public ResponseEntity<MachineModel> createMachine(
            @RequestBody MachineModel machine
    ) {

        MachineModel created =
                machineService.createMachine(
                        machine
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    // ============================================================
    // UPDATE
    // ============================================================

    @PutMapping("/{machineId}")
    public ResponseEntity<MachineModel> updateMachine(

            @PathVariable Integer machineId,

            @RequestBody MachineModel machine
    ) {

        MachineModel updated =
                machineService.updateMachine(
                        machineId,
                        machine
                );

        return ResponseEntity.ok(updated);
    }


    // ============================================================
    // DELETE
    // ============================================================

    @DeleteMapping("/{machineId}")
    public ResponseEntity<Void> deleteMachine(
            @PathVariable Integer machineId
    ) {

        machineService.deleteMachine(
                machineId
        );

        return ResponseEntity.noContent()
                .build();
    }
}