package com.uniresource.backend.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSearchRequest {
    private String title = "";
    private String country = "";
    private String province = "";
    private String university = "";
    private String category = "";
    private String condition = "";
    private float priceFrom = 0;
    private float priceTo = 0;
    private int pageNumber = 0;
    private int pageSize = 10;
   // public String condition; //put in filters

   // public PostStatus postStatus; check(in service) before sending to client
   
}
