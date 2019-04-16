/**
 * =============================================================
 * Copyright 2018 Lianjia Group All Rights Reserved
 * CompanyName: 上海链家有限公司
 * SystemName: 贝壳
 * ClassName: KeyMappingService
 * version: 1.0.0
 * date: 2019/3/14
 * author: Tyson
 * =============================================================
 */
package com.bkjk.influx.proxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bkjk.influx.proxy.common.PageRequest;
import com.bkjk.influx.proxy.common.PageVO;
import com.bkjk.influx.proxy.entity.BackendNodePo;
import com.bkjk.influx.proxy.entity.KeyMappingPo;
import com.bkjk.influx.proxy.mapper.BackendNodeMapper;
import com.bkjk.influx.proxy.mapper.KeyMappingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * @author Tyson
 * @version V1.0
 * @Description: TODO
 * @date 2019/3/14下午2:54
 */
@Service
public class KeyMappingService {

    @Autowired
    private KeyMappingMapper keyMappingMapper;


    public PageVO<KeyMappingPo> getAllKeyMapping(Integer offset, Integer limit) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setCurrent(offset / limit + 1);
        pageRequest.setPageSize(limit);
        long total = keyMappingMapper.count();
        IPage<KeyMappingPo> page = keyMappingMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize()), null);
        return new PageVO<KeyMappingPo>(page.getRecords(), pageRequest, total);
    }


    public KeyMappingPo addNewKeyMapping(KeyMappingPo keyMappingPoEntity) {
        try {
            Pattern measurement = Pattern.compile(keyMappingPoEntity.getMeasurementRegex());
            Pattern database = Pattern.compile(keyMappingPoEntity.getDatabaseRegex());
        } catch (Exception e) {
            throw new IllegalArgumentException("正则写法错误");
        }
        return keyMappingMapper.save(keyMappingPoEntity);
    }

    public KeyMappingPo updateKeyMapping(KeyMappingPo keyMappingPoEntity) {
        try {
            Pattern measurement = Pattern.compile(keyMappingPoEntity.getMeasurementRegex());
            Pattern database = Pattern.compile(keyMappingPoEntity.getDatabaseRegex());
        } catch (Exception e) {
            throw new IllegalArgumentException("正则写法错误");
        }
        return keyMappingMapper.save(keyMappingPoEntity);
    }

    public KeyMappingPo getKeyMappingById(Long id) {
        return keyMappingMapper.selectById(id);
    }

    public void deleteKeyMappingById(Long id) {
        keyMappingMapper.deleteById(id);
    }
}
