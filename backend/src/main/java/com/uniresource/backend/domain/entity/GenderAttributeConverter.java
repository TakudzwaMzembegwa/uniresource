package com.uniresource.backend.domain.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class GenderAttributeConverter implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender attribute) {
        if (attribute == null)
            return null;
        switch (attribute) {
            case MALE:
                return 1;
            case FEMALE:
                return 2;
            case NONBINARY:
                return 3;
            case OTHER:
                return 4;
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public Gender convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case 1:
                return Gender.MALE;
            case 2:
                return Gender.FEMALE;
            case 3:
                return Gender.NONBINARY;
            case 4:
                return Gender.OTHER;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}