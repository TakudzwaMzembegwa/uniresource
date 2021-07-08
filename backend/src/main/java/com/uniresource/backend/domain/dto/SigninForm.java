package com.uniresource.backend.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SigninForm {

    private String username;
    private String password;
}
