package com.vgur.spring.core.converters;

import com.vgur.spring.core.dto.ProductDto;
import com.vgur.spring.core.entities.Product;
import com.vgur.spring.core.products.ProductXml;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public Product dtoToEntity(ProductDto productDto) {
        return new Product(productDto.getId(), productDto.getTitle(), productDto.getPrice());
    }

    public ProductDto entityToDto(Product product) {
        return new ProductDto(product.getId(), product.getTitle(), product.getPrice());
    }
    public ProductXml entityToXml(Product product){
        return new ProductXml(product);
    }
}
