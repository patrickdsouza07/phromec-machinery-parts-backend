package com.phromec.machinery.service;

import com.phromec.machinery.dto.part.PartListResponse;
import com.phromec.machinery.dto.part.PartResponse;
import com.phromec.machinery.model.part.Part;
import com.phromec.machinery.model.part.PartStatus;

public interface PartService {

    PartListResponse getParts(
            int page,
            int size,
            String search,
            PartStatus status,
            String sortBy,
            String direction
    );

    PartResponse getPartById(
            Integer partId
    );

    Part createPart(
            Part part
    );

    Part updatePart(
            Integer partId,
            Part part
    );

    void deletePart(
            Integer partId
    );
}