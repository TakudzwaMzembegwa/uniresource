package com.uniresource.backend.domain.mapper;

import java.util.List;

import com.uniresource.backend.domain.dto.ProvinceDto;
import com.uniresource.backend.domain.dto.ProvinceSelector;
import com.uniresource.backend.domain.entity.Province;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses =  {CountryMapper.class, UniversityMapper.class})
public interface ProvinceMapper {

    @Mapping(target = "provinceId", source = "id")
    @Mapping(target = "provinceName", source = "name")
    public ProvinceDto toProvinceDto(Province province);
    
    @InheritInverseConfiguration
    public Province toProvince(ProvinceDto provinceDto);

    public List<ProvinceSelector> toProvinceSelector(List<Province> provincies);

    @Mapping(target = "provinceId", source = "id")
    @Mapping(target = "provinceName", source = "name")
    public ProvinceSelector toProvinceSelector(Province province);

}
