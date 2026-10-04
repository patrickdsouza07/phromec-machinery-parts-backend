package com.phromec.management.service;

import com.phromec.management.model.Machine;
import com.phromec.management.repository.MachineServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MachineServiceImpl implements MachineService {

        @Autowired
        MachineServiceRepository machineServiceRepository;

        @Override
        public List<Machine> getAllMachines() {
            // TODO Auto-generated method stub

            List<Machine> result=new ArrayList<>();

            try {
                result= machineServiceRepository.getAllMachines();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return result;

        }


}
