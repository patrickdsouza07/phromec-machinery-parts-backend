package com.phromec.machinery.controller;

import com.phromec.machinery.dto.part.PartListResponse;
import com.phromec.machinery.dto.part.PartResponse;
import com.phromec.machinery.model.part.Part;
import com.phromec.machinery.model.part.PartStatus;
import com.phromec.machinery.service.PartService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/parts")
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    @GetMapping
    public ResponseEntity<PartListResponse> getParts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "partName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        PartStatus partStatus =
                parseStatus(status);

        return ResponseEntity.ok(
                partService.getParts(
                        page,
                        size,
                        search,
                        partStatus,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/{partId}")
    public ResponseEntity<PartResponse> getPart(
            @PathVariable Integer partId
    ) {
        return ResponseEntity.ok(
                partService.getPartById(
                        partId
                )
        );
    }

    @PostMapping
    public ResponseEntity<Part> createPart(
            @RequestBody Part part
    ) {

        Part created = partService.createPart(part);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    @PutMapping("/{partId}")
    public ResponseEntity<Part> updatePart(
            @PathVariable Integer partId,
            @RequestBody Part part
    ) {
        return ResponseEntity.ok(
                partService.updatePart(
                        partId,
                        part
                )
        );
    }

    @DeleteMapping("/{partId}")
    public ResponseEntity<Void> deletePart(
            @PathVariable Integer partId
    ) {

        partService.deletePart(partId);

        return ResponseEntity
                .noContent()
                .build();
    }

    private PartStatus parseStatus(
            String status
    ) {

        if (status == null
                || status.trim().isEmpty()
                || "ALL".equalsIgnoreCase(status)) {

            return null;
        }

        for (PartStatus value :
                PartStatus.values()) {

            if (value.name()
                    .equalsIgnoreCase(status)
                    ||
                    value.getValue()
                            .equalsIgnoreCase(status)) {

                return value;
            }
        }

        throw new IllegalArgumentException(
                "Invalid part status: "
                        + status
        );
    }
}
