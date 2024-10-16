package com.neuron.cv.dbentity;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scan_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "scan_id")
  private Long scanId;

  @Column(name = "scan_time")
  private String scanTime;

  @Column(name = "address")
  private String address;

  @Column(name = "house_no")
  private String houseNo;

  @Column(name = "owner_id")
  private Long ownerId;

  @Column(name = "latitude")
  private String latitude;

  @Column(name = "longitude")
  private String longitude;

  @Column(name = "room_photos")
  private String roomPhotos;

  @Column(name = "property_use")
  private String propertyUse;

  @Column(name = "deficiencies")
  private String deficiencies;

  @Column(name = "updates")
  private String updates;

  @Column(name = "features")
  private String features;

  @Column(name = "plumbing_fixture")
  private String plumbingFixture;

  @Column(name = "is_parent")
  private int isParent;

  @Column(name = "gia")
  private Double gia;

  @Column(name = "gla")
  private Double gla;

  @Column(name = "floor_livable_area")
  private Double floorLivableArea;

  @Column(name = "floor_area")
  private Double floorArea;

  @Column(name = "finished_area")
  private Double finishedArea;

  @Column(name = "unfinished_area")
  private Double unfinishedArea;

  @Column(name = "total_area")
  private Double totalArea;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "modified_at")
  private Instant modifiedAt;

  @Column(name = "grade_level")
  private String gradeLevel;

  @Column(name = "grade_type")
  private String gradeType;

  @Column(name = "dimensions")
  private String dimensions;

  @Column(name = "house_type")
  private String houseType;

  @Column(name = "structure")
  private String structure;

  @Column(name = "structure_type")
  private String structureType;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id", referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "scan_owner_id_fkey"), insertable = false, updatable = false)
  private User owner;

  @Column(name = "scan_type")
  private String scanType;
  
  @Column(name = "room_info")
  private String roomInfo;
  
  @Column(name = "model_url_3d")
  private String model_url_3d;
  
  @Column(name = "record_status")
  private String recordStatus;

}
