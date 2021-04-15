package com.uniresource.backend.domain.entity;

import javax.persistence.AttributeConverter;

public class CategoryAttributeConverter implements AttributeConverter<Category, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Category attribute) {
        if (attribute == null)
            return null;
        switch (attribute) {
            case BOOKS:
                return 1;
            case ACCESSORIES:
                return 2;
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public Category convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;
        switch (dbData) {
            case 1:
                return Category.BOOKS;
            case 2:
                return Category.ACCESSORIES;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
