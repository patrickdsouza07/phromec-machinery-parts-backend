package com.phromec.machinery.dto.machine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineTypeListItemResponse {
    private Integer machineTypeId;
    private String typeCode;
    private String typeName;
    private String description;
    private String status;
    private long machineCount;
    private long partsCount;
    private LocalDateTime createdAt;
}
