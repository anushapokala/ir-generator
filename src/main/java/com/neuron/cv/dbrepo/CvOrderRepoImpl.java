package com.neuron.cv.dbrepo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.neuron.cv.dbentity.CvOrder;
import com.neuron.cv.dto.ParamDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CvOrderRepoImpl {

  @PersistenceContext
  @Getter
  @Setter
  private EntityManager entityManager;

  public List<CvOrder> getCvOrders(ParamDTO paramDTO) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CvOrder> query = builder.createQuery(CvOrder.class);

    Root<CvOrder> root = query.from(CvOrder.class);
    
    List<Predicate> predicates = new ArrayList<>();
    if(paramDTO.getAddress()!=null)
    predicates.add(builder.equal(root.get("streetAddress"), paramDTO.getAddress()));

    if(paramDTO.getHouseNo()!=null)
      predicates.add(builder.equal(root.get("streetAddress2"), paramDTO.getHouseNo()));
    
    //if (paramDTO.getUserId() > 0) 
    //  predicates.add(builder.equal(root.get("ipUserId"), paramDTO.getUserId()));
    
    query.where(builder.and(predicates.toArray(Predicate[]::new)));
    List<CvOrder> list = entityManager.createQuery(query).getResultList();
    return list;
  }

}
