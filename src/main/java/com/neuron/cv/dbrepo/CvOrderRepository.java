package com.neuron.cv.dbrepo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.neuron.cv.dbentity.CvOrder;

@Repository
public interface CvOrderRepository extends JpaRepository<CvOrder, Long> {

  List<CvOrder> findByStreetAddress(String streetAddress);
}
