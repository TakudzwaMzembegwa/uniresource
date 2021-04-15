package com.uniresource.backend.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleLocation {

    private int locationId;

    private String country;

    private String province;

    private String university;
}
