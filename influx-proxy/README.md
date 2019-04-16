# 简介

influx-proxy 是用于扩展InfluxDB写入功能的代理，能根据database和measurement选择将数据写入指定节点。而query类操作则是转发到所有节点并聚合返回结果。

# 开发

开发机启动参数 `-Xmx1G -Xms1G -Dspring.profiles.active=local`

如果机器有多块网卡，可以手工指定IP地址
`-Dspring.cloud.inetutils.preferred-networks=10.241.0.187`

## 使用步骤 

* 构建Docker镜像
```
mvn -DskipTests=true install
docker build . -t influx-proxy:latest
```

* 运行容器
```
docker run --name influx-proxy -d -p 9000:9000 -p 9001:9001 -e OPS_PATH=http://influx-proxy-ops.dev/api/v1 -e JAVA_OPTS="-Xmx1G -Xms1G" influx-proxy
```

* `OPS_PATH`是用来获取配置信息的接口地址，如果不希望部署OPS模块，可以通过文件形式配置后端节点列表和映射关系

在jar包所在目录新建 `application-node.yml`，启动参数添加`-Dspring.profiles.active=node`
```yaml
proxy:
  config:
    enable: true
    node:
    - nodeName: default-node
      online: true
      queryTimeout: 60000
      url: http://localhost:8086
      writeTimeout: 60000
    - nodeName: local-2
      online: true
      queryTimeout: 60000
      url: http://localhost:8087
      writeTimeout: 60000

    mapping:
    - backendNodeNames: local-1
      databaseRegex: ".*"
      measurementRegex: "cpu.*"
    - backendNodeNames: default-node
      databaseRegex: ".*"
      measurementRegex: ".*"
```

# 功能

* query 转发至所有节点，并聚合结果
* write 从body读取每行数据，并根据配置的规则分组，然后转发到对应节点，取得结果后做聚合
* debug 不支持
* ping 只有当全部后端节点都正常，才返回200

## 接口

*以下为Postman导出的接口*

* 代理接口

```json
{
	"info": {
		"_postman_id": "7ddc513e-791d-4466-9632-132aea686dfb",
		"name": "influx-proxy-dev",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
	},
	"item": [
		{
			"name": "http://localhost:8086/query",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:8086/query?pretty=true&db=mydb&q=select * from cpu_load_short",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8086",
					"path": [
						"query"
					],
					"query": [
						{
							"key": "pretty",
							"value": "true"
						},
						{
							"key": "db",
							"value": "mydb"
						},
						{
							"key": "q",
							"value": "select * from cpu_load_short"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:9000/query",
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:9000/query?pretty=true&db=mydb&q=select * from cpu_load_short",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "9000",
					"path": [
						"query"
					],
					"query": [
						{
							"key": "pretty",
							"value": "true"
						},
						{
							"key": "db",
							"value": "mydb"
						},
						{
							"key": "q",
							"value": "select * from cpu_load_short"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:9000/query Copy",
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:9000/query?pretty=true&db=mydb&q=SELECT mean(\"value\") AS \"mean_value\" FROM \"mydb\".\"autogen\".\"cpu_load_short\" WHERE time > now() - 15m GROUP BY time(1m), \"host\" FILL(none)",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "9000",
					"path": [
						"query"
					],
					"query": [
						{
							"key": "pretty",
							"value": "true"
						},
						{
							"key": "db",
							"value": "mydb"
						},
						{
							"key": "q",
							"value": "SELECT mean(\"value\") AS \"mean_value\" FROM \"mydb\".\"autogen\".\"cpu_load_short\" WHERE time > now() - 15m GROUP BY time(1m), \"host\" FILL(none)"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:9000/query Copy",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:9000/query?pretty=true&db=mydb&q=show databases",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "9000",
					"path": [
						"query"
					],
					"query": [
						{
							"key": "pretty",
							"value": "true"
						},
						{
							"key": "db",
							"value": "mydb"
						},
						{
							"key": "q",
							"value": "show databases"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:9000/ping",
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:9000/ping",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "9000",
					"path": [
						"ping"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:9000/refresh/allBackend",
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:9000/refresh/allBackend",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "9000",
					"path": [
						"refresh",
						"allBackend"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:9000/debug",
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:9000/debug",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "9000",
					"path": [
						"debug"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:9000/metrics",
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:9000/metrics",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "9000",
					"path": [
						"metrics"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:8086/ping?verbose=true",
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:8086/ping?verbose=true",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8086",
					"path": [
						"ping"
					],
					"query": [
						{
							"key": "verbose",
							"value": "true"
						}
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:9000/write?db=mydb",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "cpu_load_short,host=server01,region=us-west value=2"
				},
				"url": {
					"raw": "http://localhost:9000/write?db=mydb",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "9000",
					"path": [
						"write"
					],
					"query": [
						{
							"key": "db",
							"value": "mydb"
						}
					]
				}
			},
			"response": []
		}
	]
}
```
