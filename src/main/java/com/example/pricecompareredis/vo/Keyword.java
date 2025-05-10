package com.example.pricecompareredis.vo;

import lombok.Data;

import java.util.List;

@Data
public class Keyword {
    private String keyword;

    private List<ProductGrp> proudctGrpList; // {"FPG001" , "FPG002"}
}
