package com.phromec.machinery.dto.dashboard;

import java.math.BigDecimal;

public record MachineTypeShareResponse(
        String machineType,
        BigDecimal sharePercent
) {}