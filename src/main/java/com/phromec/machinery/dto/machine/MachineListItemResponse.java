package com.phromec.machinery.dto.machine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineListItemResponse {

    // MACHINE
    private String machineName;
    private String description;

    // MACHINE TYPE
    private Long machineTypeId;
    private String machineTypeCode;
    private String machineTypeName;

    // MODEL
    private Long machineModelId;
    private String modelNo;
    private String modelName;

    // OTHER DETAILS
    private String manufacturer;
    private String capacity;

    // PARTS
    private Integer partsCount;

    // STATUS
    private String status;
}