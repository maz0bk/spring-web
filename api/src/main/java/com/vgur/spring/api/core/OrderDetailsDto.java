package com.vgur.spring.api.core;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailsDto {
    private String address;
    private String phone;
    private String email;
}
