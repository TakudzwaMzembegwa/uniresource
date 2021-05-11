package com.uniresource.backend.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationDto {

    private int locationId;

    private CountryDto country;

    private ProvinceDto province;

    private UniversityDto university;
}
