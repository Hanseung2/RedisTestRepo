package com.example.pricecompareredis.service;

import com.example.pricecompareredis.vo.Keyword;
import com.example.pricecompareredis.vo.Product;
import com.example.pricecompareredis.vo.ProductGrp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    private RedisTemplate redisTemplate;

    public Set GetZsetValue (String key) {
        Set myTempSet = new HashSet();
        myTempSet = redisTemplate.opsForZSet().rangeWithScores(key,0,9);
        return myTempSet;
    }

    public int SetNewProduct(Product newProduct) {
        int rank = 0;
        redisTemplate.opsForZSet().add(newProduct.getProdGrpId(), newProduct.getProductId(), newProduct.getPrice());
        rank = redisTemplate.opsForZSet().rank(newProduct.getProdGrpId(), newProduct.getProductId()).intValue();
        return rank;
    }

    public int SetNewProductGrp(ProductGrp newProductGrp) {
        List<Product> productList = newProductGrp.getProductList();
        String productId = productList.get(0).getProductId();
        int price = productList.get(0).getPrice();
        redisTemplate.opsForZSet().add(newProductGrp.getProdGrpId(),productId,price);
        int productCnt = redisTemplate.opsForZSet().zCard(newProductGrp.getProdGrpId()).intValue();
        return productCnt;
    }

    public int SetNewProductGrpToKeyword (String keyword, String prodGrpId, double score){
        redisTemplate.opsForZSet().add(keyword, prodGrpId, score);
        int rank = redisTemplate.opsForZSet().rank(keyword, prodGrpId).intValue();
        return rank;
    }

    public Keyword GetLowestPriceProductByKeyword(String keyword) {
        Keyword returnInfo = new Keyword();
        List<ProductGrp> tempProdGrp = new ArrayList<>();

        // keyword를 통해 ProductGroup 가져오기 (10개)
        tempProdGrp = GetProdGrpUsingKeyword(keyword);

        // 가져온 정보들을 Return 할 Object에 넣기
        returnInfo.setKeyword(keyword);
        returnInfo.setProudctGrpList(tempProdGrp);
        return returnInfo;
    }

    public List<ProductGrp> GetProdGrpUsingKeyword(String keyword){
        List<ProductGrp> returnInfo = new ArrayList<>();
        ProductGrp tempProdGrp = new ProductGrp();

        // 입력받은 keyword 로 productGrpId 를 조회
        List<String> prodGrpIdList = new ArrayList<>();
        List<Product> tempProdList = new ArrayList<>();
        prodGrpIdList = List.copyOf(redisTemplate.opsForZSet().range(keyword,0,9));
        Product tempProduct = new Product();

        // 10개 prodGrpId로 Loop
        for (final String prodGrpId:prodGrpIdList) {
            // Loop 타면서 ProductGroup 으로 Product:price 가져오기 (10개)
            Set prodAndPriceList = new HashSet();
            prodAndPriceList = redisTemplate.opsForZSet().rangeWithScores(prodGrpId, 0, 9);
            Iterator<Object> prodPricObj = prodAndPriceList.iterator();

            // Loop 타면서 product object에 binding (10개)
            while (prodPricObj.hasNext()) {
                ObjectMapper objectMapper = new ObjectMapper();
                // {"value":00-10111-}, {"score":11000}
                Map<String, String> prodPriceMap = objectMapper.convertValue(prodPricObj.next(), Map.class);

                // Product Obj bind
                tempProduct.setProductId((prodPriceMap.get("value")));
                tempProduct.setPrice(Integer.parseInt(prodPriceMap.get("score")));
                tempProdList.add(tempProduct);
            }
            // 10개 product price 입력완료
            tempProdGrp.setProdGrpId(prodGrpId);
            tempProdGrp.setProductList(tempProdList);
            returnInfo.add(tempProdGrp);
        }

        return returnInfo;
    }
}
