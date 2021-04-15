package com.uniresource.backend.domain.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountrySelector {
    
    private int countryId;

    private String countryName;

    private List<ProvinceSelector> provinces;

    
}

