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
@Table(name = "user_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;
  
  @Column(name = "email")
  private String email;
  
  @Column(name = "password")
  private String password;
  
  @Column(name = "address")
  private String address;
  
  @Column(name = "profile_image")
  private String profileImage;
  
  @Column(name = "phone_number")
  private String phoneNumber;
  
  @Column(name = "is_active")
  private Integer isActive;
  
  @Column(name = "apple_token")
  private String appleToken;
  
  @Column(name = "stripe_custid")
  private String stripeCustid;
  
  
  @Column(name = "storj_access_key")
  private String storjAccessKey;

  @Column(name = "storj_secret_key")
  private String storjSecretKey;
  
  @Column(name = "storj_prefix")
  private String storjPrefix;
  
  @Column(name = "storj_revocable")
  private String storjRevocable;
  
  @Column(name = "is_savecard_enabled")
  private Integer isSavecardEnabled;
  
  @Column(name = "account_type")
  private Integer accountType;
  
  @Column(name = "account_name")
  private String accountName;
  
  @Column(name = "super_entity")
  private String superEntity;
  
  @Column(name = "company_name")
  private String companyName;
  
  @Column(name = "super_entity_code")
  private String superEntityCode;
  
  @Column(name = "company_code")
  private String companyCode;
  
  @Column(name = "company_id")
  private Integer companyId;
  
  @Column(name = "bypass_inapp")
  private String byPassInapp;
  
  @Column(name = "bypass_stripe")
  private String byPassStripe;
  
  @Column(name = "pmid")
  private String pmid;
  
  @Column(name = "cv_userid")
  private Integer cvUserId;
  
  @Column(name = "entity_id")
  private Integer entityId;
  
  @Column(name = "role_id")
  private Integer roleId;
  
  @Column(name = "created_at")
  private Date createdAt;
  
  @Column(name = "modified_at")
  private Date modifiedAt;
  
  @Column(name = "record_status")
  private String recordStatus;
  
  
  
}
