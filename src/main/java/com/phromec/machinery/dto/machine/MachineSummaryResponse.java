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
public class MachineSummaryResponse {

    private long totalMachines;

    private long totalTypes;

    private long activeMachines;

    private long inactiveMachines;
}