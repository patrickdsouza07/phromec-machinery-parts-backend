package com.phromec.machinery.dto.machine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineListResponse {

    private MachineSummaryResponse summary;

    private List<MachineListItemResponse> machines;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;
}