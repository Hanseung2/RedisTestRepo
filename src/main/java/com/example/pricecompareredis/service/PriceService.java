package com.example.pricecompareredis.service;

import com.example.pricecompareredis.vo.Keyword;
import com.example.pricecompareredis.vo.Product;
import com.example.pricecompareredis.vo.ProductGrp;

import java.util.Set;

public interface PriceService {

    Set GetZsetValue (String key);

    int SetNewProduct(Product newProduct);

    int SetNewProductGrp(ProductGrp newProductGrp);

    int SetNewProductGrpToKeyword (String keyword, String prodGrpId, double score);

    Keyword GetLowestPriceProductByKeyword (String keyword);
}
