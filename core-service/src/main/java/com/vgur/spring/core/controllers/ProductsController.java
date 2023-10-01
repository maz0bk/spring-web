package com.vgur.spring.core.controllers;

import com.vgur.spring.api.exceptions.ResourceNotFoundException;
import com.vgur.spring.core.converters.ProductConverter;
import com.vgur.spring.api.core.ProductDto;
import com.vgur.spring.core.entities.Product;
import com.vgur.spring.core.services.ProductsService;
import com.vgur.spring.core.validators.ProductValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products", description ="methods of working with products" )
public class ProductsController {
    private final ProductsService productsService;
    private final ProductConverter productConverter;
    private final ProductValidator productValidator;

    @Operation(
            summary = "Query for getting page of products",
            responses = {
                    @ApiResponse(
                            description = "Successful response", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    )
            }
    )
    @GetMapping
    public Page<ProductDto> getAllProducts(
            @RequestParam(name = "p", defaultValue = "1") Integer page,
            @RequestParam(name = "min_price", required = false) Integer minPrice,
            @RequestParam(name = "max_price", required = false) Integer maxPrice,
            @RequestParam(name = "title_part", required = false) String titlePart
    ) {
        if (page < 1) {
            page = 1;
        }
        return productsService.findAll(minPrice, maxPrice, titlePart, page).map(
                p -> productConverter.entityToDto(p)
        );
    }
@Operation(
        summary = "Query for getting product by id",
        responses = {
                @ApiResponse(
                        description = "Successful responce", responseCode = "200",
                        content = @Content(schema = @Schema(implementation = ProductDto.class))
                )
        }
)
    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable @Parameter(description = "Product ID", required = true) Long id) {
        Product product = productsService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found, id: " + id));
        return productConverter.entityToDto(product);
    }

    @Operation(
            summary = "Save new product in db",
            responses = {
                    @ApiResponse(description = "Successful response, new product has been created", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ProductDto.class)))
            }
    )
    @PostMapping
    public ProductDto saveNewProduct(@RequestBody ProductDto productDto) {
        log.warn("try to create new product title: "+productDto.getTitle());
        productValidator.validate(productDto);
        Product product = productConverter.dtoToEntity(productDto);
        product = productsService.save(product);
        return productConverter.entityToDto(product);
    }

    @Operation(
            summary = "Update product in db",
            responses = {
                    @ApiResponse(description = "Successful response, product has been updated", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ProductDto.class)))
            }
    )
    @PutMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        productValidator.validate(productDto);
        Product product = productsService.update(productDto);
        return productConverter.entityToDto(product);
    }

    @Operation(summary = "Delete product by ID",
    responses = {@ApiResponse(description = "Successful response, product has been deleted", responseCode = "200")})
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        productsService.deleteById(id);
    }
}
