package com.uniresource.backend.domain.mapper;

import java.util.List;

import com.uniresource.backend.domain.dto.CountryDto;
import com.uniresource.backend.domain.dto.CountrySelector;
import com.uniresource.backend.domain.entity.Country;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProvinceMapper.class, UniversityMapper.class})
public interface CountryMapper {
    
    @Mapping(target = "countryId", source = "id")
    @Mapping(target = "countryName", source = "name")
    public CountryDto toCountryDto(Country country);

    @InheritInverseConfiguration
    public Country toCountry(CountryDto countryDto);

    public List<CountrySelector> toCountrySelector(List<Country> countries);

    
    @Mapping(target = "countryId", source = "id")
    @Mapping(target = "countryName", source = "name")
    public CountrySelector toCountrySelector(Country country);


    


}
