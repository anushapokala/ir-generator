package com.neuron.cv.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InspectionReport{
    public Property property;
    public String pdaHyperlink;
    public String collectionType;
    public String caseFileID;
    public String lpaID;
    public String pdaSubmitterEntity;
    public String propertyDataCollectorName;
    public String propertyDataCollectorType;
    public List<DataCollectorContact> propertyDataCollectorContacts;
    public String pdaCollectionEntity;
    public boolean dataCollectorAcknowledgement;
    public String dataCollectionDate;
}
