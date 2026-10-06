package com.phromec.machinery.dto.machine;

public interface MachineListProjection {

    Long getMachineId();

    String getMachineName();

    String getDescription();

    Long getMachineTypeId();

    String getMachineTypeCode();

    String getMachineTypeName();

    Long getMachineModelId();

    String getModelNo();

    String getModelName();

    String getManufacturer();

    String getCapacity();

    Integer getPartsCount();

    String getStatus();
}