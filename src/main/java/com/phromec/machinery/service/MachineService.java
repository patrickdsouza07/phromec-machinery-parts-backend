package com.phromec.machinery.service;

import com.phromec.machinery.dto.machine.MachineListResponse;
import com.phromec.machinery.model.machine.MachineModel;

public interface MachineService {

    MachineListResponse getMachines(
            int page,
            int size,
            String search,
            Integer machineTypeId,
            String status,
            String sortBy,
            String direction
    );

    MachineModel getMachineById(
            Integer machineModelId
    );

    MachineModel createMachine(
            MachineModel machineModel
    );

    MachineModel updateMachine(
            Integer machineModelId,
            MachineModel machineModel
    );

    void deleteMachine(
            Integer machineModelId
    );
}