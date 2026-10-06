package com.phromec.machinery.dto.part;

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
public class PartListResponse {

    private PartSummaryResponse summary;

    private List<PartResponse> parts;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;
}