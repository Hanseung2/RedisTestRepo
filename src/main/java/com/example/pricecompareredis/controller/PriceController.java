package com.example.pricecompareredis.controller;

import com.example.pricecompareredis.service.PriceService;
import com.example.pricecompareredis.vo.Keyword;
import com.example.pricecompareredis.vo.Product;
import com.example.pricecompareredis.vo.ProductGrp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/")
@Tag(name = "PriceController", description = "가격 관련 API")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @Operation(summary = "ZSET 값 가져오기", description = "Redis ZSET 키로부터 값 리스트를 반환합니다.")
    @GetMapping("/getZSETValue")
    public Set GetZsetValue (String key){
        return priceService.GetZsetValue(key);
    }

    @Operation(summary = "신규 상품 저장", description = "신규 상품 정보를 Redis에 저장합니다.")
    @PutMapping("/product")
    public int SetNewProduct(@RequestBody Product newProduct){
        return priceService.SetNewProduct(newProduct);
    }

    @PutMapping("/productGroup")
    public int SetNewProductGrp(@RequestBody ProductGrp newProductGrp){
        return priceService.SetNewProductGrp(newProductGrp);
    }

    @PutMapping("/productGroupToKeyword")
    public int setNewProductGrpToKeyword (String keyword, String prodGrpId, double score){
        return priceService.SetNewProductGrpToKeyword(keyword, prodGrpId, score);
    }

    @GetMapping("/productPrice/lowest")
    public Keyword GetLowestPriceProductByKeyword (String keyword){
        return priceService.GetLowestPriceProductByKeyword(keyword);
    }
}
