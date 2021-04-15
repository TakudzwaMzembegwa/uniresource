package com.uniresource.backend.domain.mapper;

import com.uniresource.backend.domain.dto.LocationDto;
import com.uniresource.backend.domain.dto.SimpleLocation;
import com.uniresource.backend.domain.entity.Location;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CountryMapper.class, ProvinceMapper.class, UniversityMapper.class})
public interface LocationMapper {

    @Mapping(target = "locationId", source = "id")
    public LocationDto toLocationDto(Location location);
   
    @InheritInverseConfiguration
    public Location toLocation(LocationDto locationDto);

    @Mapping(target = "locationId", source = "id")
    @Mapping(target = "country", source = "country.name")
    @Mapping(target = "province", source = "province.name")
    @Mapping(target = "university", source = "university.name")
    public SimpleLocation toSimpleLocation(Location location);

}
