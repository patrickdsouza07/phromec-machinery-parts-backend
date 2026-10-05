package com.phromec.management.service.impl;

import com.phromec.management.model.Part;
import com.phromec.management.repository.PartRepository;
import com.phromec.management.service.PartService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;

    public PartServiceImpl(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Override
    public List<Part> getAllParts() {
            return partRepository.findAll();
        }


}
