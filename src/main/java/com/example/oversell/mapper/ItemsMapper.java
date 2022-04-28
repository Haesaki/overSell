package com.example.oversell.mapper;

import com.example.oversell.tkmapper.TKMapper;
import com.example.oversell.pojo.Items;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemsMapper extends TKMapper<Items> {
}
