package com.phromec.machinery.dto.machine;

public interface MachineSummaryProjection {

    Long getTotalMachines();

    Long getActiveMachines();

    Long getInactiveMachines();
}