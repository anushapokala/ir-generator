package com.neuron.cv.entity;

import java.util.ArrayList;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InspectionReport{
    public Property property;
    public String eSignatureDate;
    public ScanningInfo scanningInfo;
    public Object contractPrice;
    public String pdaHyperlink;
    public Object lPAKey;
    public Object sellerId;
    public String productName;
    public String loanReason;
    public Object aMCName;
    public Object lenderContactInformation;
    public String caseFileID;
    public String orderId;
    public String fileNo;
    public String borrower;
    public String county;
    public String pdaSubmitterEntity;
    public Object stateCredentialID;
    public Object licenseState;
    public String cv_propertyDataCollectorUserType;
    public String propertyDataCollectorName;
    public String propertyDataCollectorType;
    public String cv_appraisalFormType;
    public CvLender cv_lender;
    public CvAppraislManagementCompany cv_appraislManagementCompany;
    public ArrayList<Party> parties;
    public Date scanDate;
}
