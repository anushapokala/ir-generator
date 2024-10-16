package com.neuron.cv.entity;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ScanningInfo {
  public Instant generatedDate;
  public Instant scannedDate;
  public Instant createdDateAndTime;
  public Instant submittedDateAndTime;
  public Instant finishedDateAndTime;
  public Instant firstSpinCapturedAt;
  public Instant lastSpinCapturedAt;
  public String scannerEmail;
  public String scannerId;
  public String scannerFirstName;
  public String scannerLastName;
}
