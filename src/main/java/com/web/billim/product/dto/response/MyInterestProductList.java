package com.web.billim.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyInterestProductList {
    private List<MyInterestProduct> myInterestProductList;
}
