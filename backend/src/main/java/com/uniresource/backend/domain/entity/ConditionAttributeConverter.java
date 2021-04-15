package com.uniresource.backend.domain.entity;

import javax.persistence.AttributeConverter;

public class ConditionAttributeConverter implements AttributeConverter<Condition, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Condition attribute) {
        if (attribute == null)
            return null;
        switch (attribute) {
            case NEW:
                return 1;
            case MINT:
                return 2;
            case GOOD:
                return 3;
            case FAIR:
                return 4;
            case POOR:
                return 5;
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public Condition convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;
        switch (dbData) {
            case 1:
                return Condition.NEW;
            case 2:
                return Condition.MINT;
            case 3:
                return Condition.GOOD;
            case 4:
                return Condition.FAIR;
            case 5:
                return Condition.POOR;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
