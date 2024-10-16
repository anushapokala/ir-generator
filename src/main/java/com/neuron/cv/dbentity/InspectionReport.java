package com.neuron.cv.dbentity;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inspection_report_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionReport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "report_id")
  private Long reportId;

  @Column(name = "scan_id")
  private Long scanId;

  @Column(name = "user_id_i")
  private Long userId;

  @Column(name = "address")
  private String address;

  @Column(name = "house_no")
  private String houseNo;

  @Column(name = "pdapi")
  private String pdapi;

  @Column(name = "location")
  private String location;

  @Column(name = "views")
  private String views;

  @Column(name = "updates")
  private String updates;

  @Column(name = "deficiencies")
  private String deficiencies;

  @Column(name = "utilities")
  private String utilities;

  @Column(name = "status")
  private String status;

  @Column(name = "submitted_at")
  private Instant submittedAt;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "modified_at")
  private Instant modifiedAt;
  
  @Column(name = "record_status")
  private String recordStatus;
  
//  @OneToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "scan_id", referencedColumnName = "scan_id",
//      foreignKey = @ForeignKey(name = "fk_inspection_report_scan_id"),insertable = false, updatable = false)
//  private Scan scan;
  
//  @OneToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "user_id_i", referencedColumnName = "id",
//      foreignKey = @ForeignKey(name = "fk_inspection_report_user_id"),insertable = false, updatable = false)
//  private User user;
  
}
