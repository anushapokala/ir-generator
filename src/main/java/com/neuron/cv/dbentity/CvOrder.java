package com.neuron.cv.dbentity;

import java.util.Date;
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
@Table(name = "cv_order_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CvOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cv_order_id")
  private Long cvOrderId;
  
  @Column(name = "order_id")
  private String orderId;
  
  @Column(name = "streetaddress")
  private String streetAddress;
  
  @Column(name = "streetaddress2")
  private String streetAddress2;
  
  @Column(name = "city")
  private String city;
  
  @Column(name = "state_c")
  private String state;
  
  @Column(name = "postalcode")
  private String postalCode;
  
  @Column(name = "county")
  private String county;
  
  
  @Column(name = "propertydatacollectorname")
  private String propertyDataCollectorName;
  
  @Column(name = "propertydatacollectortype")
  private String propertyDataCollectorType;
  
  
  
  @Column(name = "userid")
  private String userId;
  
  @Column(name = "firstname")
  private String firstName;
  
  @Column(name = "lastname")
  private String lastName;
  
  @Column(name = "companyname")
  private String companyName;
  
  @Column(name = "pdc_streetaddress")
  private String pdcStreetAddress;
  
  @Column(name = "pdc_city")
  private String pdcCity;
  
  @Column(name = "pdc_state")
  private String pdcState;
  
  @Column(name = "pdc_postalcode")
  private String pdcPostalCode;
  
  
  @Column(name = "pdc_phone")
  private String pdcPhone;
  
  @Column(name = "pdc_email")
  private String pdcEmail;
  
  //@Column(name = "ip_user_id")
  //private Long ipUserId;
  
  @Column(name = "status")
  private String status;
  
  @Column(name = "created_at")
  private Date createdAt;
  
  @Column(name = "modified_at")
  private Date modifiedAt;
  
}
