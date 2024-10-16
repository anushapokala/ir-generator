package com.neuron.cv.dbrepo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.neuron.cv.constants.CVConstants;
import com.neuron.cv.dbentity.Scan;
import com.neuron.cv.dto.ParamDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
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
public class ScanRepoImpl {

  @PersistenceContext
  @Getter
  @Setter
  private EntityManager entityManager;

  public List<Scan> getScanDetailsforCVReport(ParamDTO paramDTO) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Scan> criteria = builder.createQuery(Scan.class);

    Root<Scan> root = criteria.from(Scan.class);

    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.equal(root.get("isParent"), CVConstants.IS_PARENT));
    predicates.add(builder.equal(root.get("dimensions"), CVConstants.DIMENSION));

    if (paramDTO.getAddress() != null) {
      predicates.add(builder.equal(root.get("address"), paramDTO.getAddress()));
    }
    if (paramDTO.getHouseNo() != null) {
      predicates.add(builder.equal(root.get("houseNo"), paramDTO.getHouseNo()));
    } else
      predicates.add(builder.equal(root.get("houseNo"),CVConstants.EMPTY));
    

    if (paramDTO.getUserId() > 0) {
      predicates.add(builder.equal(root.get("ownerId"), paramDTO.getUserId()));
    }
    predicates.add(builder.equal(root.get("recordStatus"), CVConstants.STATUS_ACTIVE));
    root.fetch("owner", JoinType.LEFT);
    criteria.orderBy(builder.desc(root.get("modifiedAt")));
    criteria.where(builder.and(predicates.toArray(Predicate[]::new)));

    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Object[]> getSpinCaptureDetailsforCVReport(ParamDTO paramDTO) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);

    Root<Scan> root = criteria.from(Scan.class);
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.equal(root.get("isParent"), CVConstants.IS_PARENT));
    predicates.add(builder.equal(root.get("dimensions"), CVConstants.DIMENSION));

    if (paramDTO.getAddress() != null) {
      predicates.add(builder.equal(root.get("address"), paramDTO.getAddress()));
    }
    if (paramDTO.getHouseNo() != null) {
      predicates.add(builder.equal(root.get("houseNo"), paramDTO.getHouseNo()));
    } else
      predicates.add(builder.equal(root.get("houseNo"),CVConstants.EMPTY));
    
    if (paramDTO.getUserId() > 0) {
      predicates.add(builder.equal(root.get("ownerId"), paramDTO.getUserId()));
    }

    predicates.add(builder.equal(root.get("recordStatus"), CVConstants.STATUS_ACTIVE));
    criteria.multiselect(builder.min(root.get("createdAt")), builder.max(root.get("createdAt")));
    criteria.where(builder.and(predicates.toArray(Predicate[]::new)));

    List<Object[]> results = entityManager.createQuery(criteria).getResultList();

    return results;
  }

}
