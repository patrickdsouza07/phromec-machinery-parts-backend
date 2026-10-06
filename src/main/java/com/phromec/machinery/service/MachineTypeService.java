package com.phromec.machinery.service;

import com.phromec.machinery.dto.machine.MachineTypeListItemResponse;
import com.phromec.machinery.dto.machine.CreateMachineTypeRequest;
import com.phromec.machinery.model.machine.MachineType;

import java.util.List;

public interface MachineTypeService {
    List<MachineTypeListItemResponse> getMachineTypes();

    List<String> getMachineTypeNames();

    MachineType createMachineType(CreateMachineTypeRequest request);

    void deleteMachineType(Integer machineTypeId);
}
