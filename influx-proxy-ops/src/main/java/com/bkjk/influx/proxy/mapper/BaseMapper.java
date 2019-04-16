package com.bkjk.influx.proxy.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bkjk.influx.proxy.entity.BasePo;

import java.util.List;

/**
 * @Program: influx-proxy
 * @Description:
 * @Author: shaoze.wang
 * @Create: 2019/2/28 11:01
 **/
public interface BaseMapper<T extends BasePo> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    default List<T> findAll(){
        return selectList(Wrappers.emptyWrapper());
    }

    default T save(T entity){
        if(selectById(entity.getId())!=null){
            updateById(entity);
        }else {
            insert(entity);
        }
        return selectById(entity.getId());
    }

    default long count(){
        return selectCount(Wrappers.emptyWrapper());
    }
}
