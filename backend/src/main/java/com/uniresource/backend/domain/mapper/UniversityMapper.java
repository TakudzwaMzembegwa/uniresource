package com.uniresource.backend.domain.mapper;

import java.util.List;

import com.uniresource.backend.domain.dto.UniversityDto;
import com.uniresource.backend.domain.dto.UniversitySelector;
import com.uniresource.backend.domain.entity.University;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { ProvinceMapper.class })
public interface UniversityMapper {

    @Mapping(target = "universityId", source = "id")
    @Mapping(target = "universityName", source = "name")
    @Mapping(target = "universityAbbreviation", source = "abbrev")
    public UniversityDto toUniversityDto(University university);
    
    @InheritInverseConfiguration
    public University toUniversity(UniversityDto universityDto);

    public List<UniversitySelector> toUniversitySelector(List<University> universities);

    @Mapping(target = "universityId", source = "id")
    @Mapping(target = "universityName", source = "name")
    @Mapping(target = "universityAbbreviation", source = "abbrev")
    public UniversitySelector toUniversitySelector(University university);

}
