package com.uniresource.backend.domain.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProvinceSelector {
    
    private int provinceId;

    private String provinceName;
    
    private List<UniversitySelector> universities;
}
