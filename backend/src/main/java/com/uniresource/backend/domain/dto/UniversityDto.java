package com.uniresource.backend.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UniversityDto {
    
    private int universityId;

    private String universityName;

    private String universityAbbreviation;
}
