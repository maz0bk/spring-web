package com.vgur.spring.core.endpoints;

import com.vgur.spring.core.converters.ProductConverter;
import com.vgur.spring.core.entities.Product;
import com.vgur.spring.core.exceptions.ResourceNotFoundException;
import com.vgur.spring.core.repositories.ProductsRepository;
import com.vgur.spring.core.services.ProductsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


import java.util.List;

@Endpoint
@RequiredArgsConstructor
@Slf4j
public class ProductEndpoint {
    private final ProductConverter productConverter;
    private final ProductsService productsService;

    private static final String NAMESPACE_URI ="http://www.vgur.com/spring/ws/products";
    @PayloadRoot( namespace = NAMESPACE_URI, localPart = "getProductByIdRequest")
    @ResponsePayload
    public GetProductByIdResponse getProductById(@RequestPayload GetProductByIdRequest request){
        GetProductByIdResponse response = new GetProductByIdResponse();
        response.setProduct(productConverter.entityToXml(productsService.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cant find product id: "+ request.getId()))));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllProductsRequest")
    @ResponsePayload
    public GetAllProductsResponse getAllProducts(@RequestPayload GetAllProductsRequest request) {
        GetAllProductsResponse response = new GetAllProductsResponse();
        List<Product> products = productsService.findAll();
        List<ProductXml> productXml = response.getProducts();
        for (Product p :
                products) {
            productXml.add(productConverter.entityToXml(p));
        }
        return response;
    }
    /*
     Пример запроса: POST http://localhost:8189/app/ws
        Header -> Content-Type: text/xml

    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:f="http://www.vgur.com/spring/ws/products">
            <soapenv:Header/>
            <soapenv:Body>
                <f:getAllProductsRequest/>
            </soapenv:Body>
        </soapenv:Envelope>
    */

}
