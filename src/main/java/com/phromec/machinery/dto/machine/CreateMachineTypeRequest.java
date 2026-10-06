package com.phromec.machinery.dto.machine;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMachineTypeRequest {
    private String typeName;
    private String typeCode;
    private String description;
}
