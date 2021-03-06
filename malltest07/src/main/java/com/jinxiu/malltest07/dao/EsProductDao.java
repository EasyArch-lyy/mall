package com.jinxiu.malltest07.dao;

import com.jinxiu.malltest07.nosql.elasticsearch.document.EsProduct;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 搜索系统中的商品管理自定义Dao
 */
public interface EsProductDao {
    List<EsProduct> getAllEsProductList(@Param("id") Long id);
}
