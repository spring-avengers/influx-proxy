/**
 * =============================================================
 * Copyright 2018 Lianjia Group All Rights Reserved
 * CompanyName: 上海链家有限公司
 * SystemName: 贝壳
 * ClassName: BackendNodeService
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
import com.bkjk.influx.proxy.entity.NodePo;
import com.bkjk.influx.proxy.mapper.BackendNodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyson
 * @version V1.0
 * @Description: TODO
 * @date 2019/3/14上午11:11
 */
@Service
public class BackendNodeService {

    @Autowired
    private BackendNodeMapper backendNodeMapper;


    public PageVO<BackendNodePo> geAllNodes(Integer offset, Integer limit) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setCurrent(offset / limit + 1);
        pageRequest.setPageSize(limit);
        long total = backendNodeMapper.count();
        IPage<BackendNodePo> page = backendNodeMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize()), null);
        return new PageVO<BackendNodePo>(page.getRecords(), pageRequest, total);
    }

    public List<BackendNodePo> getAllNodesWithoutCondtion() {
        return backendNodeMapper.findAll().stream().filter(BackendNodePo::isOnline).collect(Collectors.toList());
    }

    public BackendNodePo addNewNode(NodePo nodePo) {
        if (nodePo.getId() != null) {
            throw new IllegalArgumentException("创建时ID必须为空");
        }
        BackendNodePo backendNodeEntity = this.transferToData(nodePo);
        return backendNodeMapper.save(backendNodeEntity);
    }

    public BackendNodePo updateNode(NodePo nodePo) {
        BackendNodePo backendNodeEntity = this.transferToData(nodePo);
        return backendNodeMapper.save(backendNodeEntity);
    }

    public BackendNodePo getBackendNodeById(Long id) {
        return backendNodeMapper.selectById(id);
    }

    public void deleteNodeById(Long id) {
        backendNodeMapper.deleteById(id);
    }

    private BackendNodePo transferToData(NodePo nodePo) {
        BackendNodePo backendNodeEntity = BackendNodePo.builder().nodeName(nodePo.getName())
                .online(nodePo.isOnline())
                .url(nodePo.getUrl())
                .writeTimeout(nodePo.getWriteTimeout())
                .queryTimeout(nodePo.getQueryTimeout())
                .build();
        backendNodeEntity.setId(nodePo.getId());
        return backendNodeEntity;
    }
}
