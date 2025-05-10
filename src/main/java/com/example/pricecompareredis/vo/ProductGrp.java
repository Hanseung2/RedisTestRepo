package com.example.pricecompareredis.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductGrp {
    private String prodGrpId; //FPG0001

    private List<Product> productList; // {"55156440-32fb-4b65-975d-7b81baca73fe" ...}
}
