package com.uniresource.backend.domain.entity;

import javax.persistence.AttributeConverter;

public class PostStatusAttributeConverter implements AttributeConverter<PostStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(PostStatus attribute) {
        if (attribute == null)
            return null;
        switch (attribute) {
            case PROCESSING:
                return 1;
            case ACTIVE:
                return 2;
            case EXPIRED:
                return 3;
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public PostStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null)
            return null;
        switch (dbData) {
            case 1:
                return PostStatus.PROCESSING;
            case 2:
                return PostStatus.ACTIVE;
            case 3:
                return PostStatus.EXPIRED;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
