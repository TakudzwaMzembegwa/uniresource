package com.uniresource.backend.domain.entity;

import javax.persistence.AttributeConverter;

public class StudyYearAttributeConverter implements AttributeConverter<StudyYear, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StudyYear attribute) {
        if (attribute == null)
            return null;
        switch (attribute) {
            case YEAR1:
                return 1;
            case YEAR2:
                return 2;
            case YEAR3:
                return 3;
            case YEAR4:
                return 4;
            case Honors:
                return 5;
            case MASTERS:
                return 6;
            case DOCTORATE:
                return 7;
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }

    }

    @Override
    public StudyYear convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;
        switch (dbData) {
            case 1:
                return StudyYear.YEAR1;
            case 2:
                return StudyYear.YEAR2;
            case 3:
                return StudyYear.YEAR3;
            case 4:
                return StudyYear.YEAR4;
            case 5:
                return StudyYear.Honors;
            case 6:
                return StudyYear.MASTERS;
            case 7:
                return StudyYear.DOCTORATE;
            default:
                throw new IllegalArgumentException(dbData + " not supported");
        }
    }
}
