package com.phromec.management.service;

import com.phromec.management.model.Part;
import com.phromec.management.repository.PartServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PartServiceImpl implements PartService {

        @Autowired
        PartServiceRepository partServiceRepository;

        @Override
        public List<Part> getAllParts() {
            // TODO Auto-generated method stub

            List<Part> result=new ArrayList<>();

            try {
                result= partServiceRepository.getAllParts();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return result;

        }


}
