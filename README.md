# 背景

如果监控全部接入单节点influxdb显然不能满足需求

Influxdb免费版不支持集群，商业版按照节点数收费不合理

在查询Influxdb监控数据的时候天然不会跨表（Measurement）查询

考虑到这些因素可以简单开发一个proxy来做数据分片，它的功能包括

1. 按 db 和 measurement 分片写入不同节点
2. 聚合查询请求
3. 转发其它控制命令到所有节点

除此之外还有一个influx-proxy-ops模块，它的功能包括：

1. 保存节点配置、转发规则到数据库
2. 动态配置和更新influx-proxy中的配置
3. 在某个节点宕机后，下线或者替换某个节点


![架构图](https://github.com/ke-finance/influx-proxy/blob/master/doc/img/image2019-1-25_14-16-3.png?raw=true)


# influx-proxy-ops 功能

## 节点配置

![节点列表](https://github.com/ke-finance/influx-proxy/blob/master/doc/img/img_15551404818839.png?raw=true)


## 映射规则配置

![映射规则配置](https://github.com/ke-finance/influx-proxy/blob/master/doc/img/img_20190413152826.png?raw=true)


