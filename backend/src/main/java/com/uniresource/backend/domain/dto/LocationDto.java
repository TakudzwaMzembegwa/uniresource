package com.uniresource.backend.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationDto {

    public int locationId;

    public CountryDto country;

    public ProvinceDto province;

    public UniversityDto university;
}
