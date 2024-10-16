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
@Table(name = "job_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "job_id")
  private Long jobId;

  @Column(name = "job_name")
  private String jobName;

  @Column(name = "job_status")
  private String jobStatus;

  @Column(name = "filename")
  private String fileName;

  @Column(name = "super_entity_name")
  private String superEntityName;

  @Column(name = "entity_name")
  private String entityName;

  @Column(name = "folder_path")
  private String folderPath;

  @Column(name = "address")
  private String address;

  @Column(name = "house_no")
  private String houseNo;

  @Column(name = "super_entity_id")
  private Long superEntityId;

  @Column(name = "entity_id")
  private Long entityId;

  @Column(name = "user_id_i")
  private Long userId;

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

}
