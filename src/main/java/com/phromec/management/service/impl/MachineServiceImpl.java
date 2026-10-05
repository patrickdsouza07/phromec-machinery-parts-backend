package com.phromec.management.service.impl;

import com.phromec.management.model.Machine;
import com.phromec.management.repository.MachineRepository;
import com.phromec.management.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MachineServiceImpl implements MachineService {

        @Autowired
        MachineRepository machineRepository;

        @Override
        public List<Machine> getAllMachines() {
            // TODO Auto-generated method stub

            List<Machine> result=new ArrayList<>();

            try {
                result= machineRepository.findAll();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return result;

        }


}
