package com.neuron.cv.dbrepo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.neuron.cv.dbentity.Scan;

@Repository
public interface IrScanRepository extends JpaRepository<Scan, Long> {

  List<Scan> findByAddress(String address);
}