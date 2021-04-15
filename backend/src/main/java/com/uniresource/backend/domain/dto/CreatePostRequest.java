package com.uniresource.backend.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreatePostRequest {
    @NotBlank
    private String title;
    private String description;
    private float price;
    @NotNull
    private LocationDto location;
    @NotBlank
    private String category;
    @NotBlank
    private String condition;

    public CreatePostRequest() {
        this.location = new LocationDto();
        this.location.setCountry(new CountryDto());
        this.location.setProvince(new ProvinceDto());
        this.location.setUniversity(new UniversityDto());

    }

}
