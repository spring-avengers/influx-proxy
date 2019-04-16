/**
 * =============================================================
 * Copyright 2018 Lianjia Group All Rights Reserved
 * CompanyName: 上海链家有限公司
 * SystemName: 贝壳
 * ClassName: ManagementInfoService
 * version: 1.0.0
 * date: 2019/3/20
 * author: Tyson
 * =============================================================
 */
package com.bkjk.influx.proxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bkjk.influx.proxy.common.DeleteStatusEnum;
import com.bkjk.influx.proxy.common.PageRequest;
import com.bkjk.influx.proxy.common.PageVO;
import com.bkjk.influx.proxy.entity.ManagementInfoPo;
import com.bkjk.influx.proxy.entity.ManagementInfoWarpPo;
import com.bkjk.influx.proxy.mapper.ManagementInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tyson
 * @version V1.0
 * @Description: TODO
 * @date 2019/3/20上午11:07
 */
@Service
public class ManagementInfoService {

    @Autowired
    private ManagementInfoMapper managementInfoMapper;

    public List<ManagementInfoPo> getAllManagementInfo() {
        return managementInfoMapper.findAll().stream().filter(item -> DeleteStatusEnum.NOT_DELETE.eq(item.getDeleteTag())).collect(Collectors.toList());
    }

    public PageVO<ManagementInfoPo> getAllManagementInfo(Integer offset, Integer limit) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setCurrent(offset / limit + 1);
        pageRequest.setPageSize(limit);
        Map<String, Object> param = new HashMap<>();
        param.put("delete_tag", 0);
        long total = managementInfoMapper.count();
        IPage<ManagementInfoPo> page = managementInfoMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize()), Wrappers.<ManagementInfoPo>query().allEq(param));
        return new PageVO<ManagementInfoPo>(page.getRecords(), pageRequest, total);
    }

    public ManagementInfoPo addNewRetention(ManagementInfoWarpPo managementInfoWarpPo) {
        ManagementInfoPo managementInfoPo = ManagementInfoPo.builder().nodeUrl(managementInfoWarpPo.getNodeUrl())
                .databaseName(managementInfoWarpPo.getDatabaseName())
                .retentionName(managementInfoWarpPo.getRetentionName())
                .retentionDuration(managementInfoWarpPo.getRetentionDuration())
                .isDefault(managementInfoWarpPo.isRetentionDefault())
                .build();
        return managementInfoMapper.save(managementInfoPo);
    }

    public ManagementInfoPo updateRetention(ManagementInfoPo managementInfoPo) {
        ManagementInfoPo management = managementInfoMapper.selectById(managementInfoPo.getId());
        management.setRetentionDuration(managementInfoPo.getRetentionDuration());
        management.setDefault(managementInfoPo.isDefault());
        return managementInfoMapper.save(management);
    }

    public ManagementInfoPo deleteRetentionById(Long id) {
        ManagementInfoPo managementInfoPo = managementInfoMapper.selectById(id);
        managementInfoPo.setDeleteTag(DeleteStatusEnum.IS_DELETE.getCode());
        return managementInfoMapper.save(managementInfoPo);
    }

    public ManagementInfoPo selectRetentionById(Long id) {
        return managementInfoMapper.selectById(id);
    }

}

