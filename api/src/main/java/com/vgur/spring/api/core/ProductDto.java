package com.vgur.spring.api.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product model")
public class ProductDto {
    @Schema(description = "Product ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;
    @Schema(description = "Product title", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 255, minLength = 3, example = "Milk")
    private String title;
    @Schema(description = "Product price", requiredMode = Schema.RequiredMode.REQUIRED, example = "100.02")
    private BigDecimal price;
}
