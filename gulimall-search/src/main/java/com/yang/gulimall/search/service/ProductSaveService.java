package com.yang.gulimall.search.service;


import com.yang.common.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 **/
public interface ProductSaveService {

    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
