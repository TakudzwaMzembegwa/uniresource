package com.uniresource.backend.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePasswordRequest {

    private String password;

    private String newPassword;
    
    private String confirmNewPassword;

}
