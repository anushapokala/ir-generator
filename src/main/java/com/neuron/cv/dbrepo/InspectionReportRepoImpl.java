package com.neuron.cv.dbrepo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.neuron.cv.constants.CVConstants;
import com.neuron.cv.dbentity.InspectionReport;
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
public class InspectionReportRepoImpl {

  @PersistenceContext
  @Getter
  @Setter
  private EntityManager entityManager;

  public List<InspectionReport> getInspectionReports(ParamDTO paramDTO) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<InspectionReport> criteria = builder.createQuery(InspectionReport.class);

    Root<InspectionReport> root = criteria.from(InspectionReport.class);
    List<Predicate> predicates = new ArrayList<>();

    if (paramDTO.getAddress() != null) {
      predicates.add(builder.equal(root.get("address"), paramDTO.getAddress()));
    }
    if (paramDTO.getHouseNo() != null) {
      predicates.add(builder.equal(root.get("houseNo"), paramDTO.getHouseNo()));
    } else
      predicates.add(builder.equal(root.get("houseNo"),CVConstants.EMPTY));
    
    if (paramDTO.getUserId() > 0) {
      predicates.add(builder.equal(root.get("userId"), paramDTO.getUserId()));
    }
    predicates.add(builder.equal(root.get("recordStatus"), CVConstants.STATUS_ACTIVE));
    
    criteria.orderBy(builder.desc(root.get("modifiedAt")));
    criteria.where(builder.and(predicates.toArray(Predicate[]::new)));

    List<InspectionReport> reports =
        entityManager.createQuery(criteria).setMaxResults(1).getResultList();
    return reports;
  }

}
