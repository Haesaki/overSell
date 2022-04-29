package com.example.oversell.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ItemsMapper {
    Integer selectStockById(@Param(value = "id") String id);

    Integer updateStockById(@Param(value = "id") String id, @Param(value = "leftCount") Integer leftCount);

    Integer updateStockByIdAndVersion(@Param(value = "id") String id,
                                      @Param(value = "leftCount") Integer leftCount, @Param(value = "version") Integer version);
}
