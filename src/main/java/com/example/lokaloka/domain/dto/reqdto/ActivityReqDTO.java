package com.example.lokaloka.domain.dto.reqdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityReqDTO {
    private String name;
    private String description;
    private String location;
    private String price;
    private String image;
    private String latitude;
    private String longitude;
}
