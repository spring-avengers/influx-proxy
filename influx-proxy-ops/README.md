# 开发

开发机启动参数 `-Xmx1G -Xms1G -Dspring.profiles.active=local `

当`spring.profiles.active=dev`且数据库中无数据时，会自动创建两条测试数据

## 数据库初始化

修改`pom.xml`中`flyway-maven-plugin`的数据库链接信息

```xml
<plugin>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-maven-plugin</artifactId>
    <configuration>
        <url>jdbc:mysql://127.0.0.1:3306/influx_proxy_ops?characterEncoding=UTF-8</url>
        <user>root</user>
        <password>root</password>
        <locations>
            <location>filesystem:src/main/resources/db/migration</location>
        </locations>
    </configuration>
</plugin>
```

执行下面命令初始化数据库
```text
mvn flyway:migrate
```


## 使用步骤 

* 构建Docker镜像

```
mvn -DskipTests=true install
docker build . -t influx-proxy-ops:latest
```

* 登录权限配置

可以通过启动参数来指定AD配置。其中LDAP_URL、LDAP_DOMAIN是必须字段,LDAP_ADMIN_USER_NAME用来配置哪个账号是管理员（只有管理员有增删改权限）。
```yaml
ldap:
  url: ${LDAP_URL:ldap://ldap.com:389}
  adminUserName: ${LDAP_ADMIN_USER_NAME:admin,root}
  domain: ${LDAP_DOMAIN:ldap.com}
  facker: true # true 表示不走ldap验证，而是直接返回登录成功
  rootDn: DC=foo,DC=com
  searchFilter: (&(objectClass=user)(mail={0}))
eureka:
  client:
    serviceUrl:
      defaultZone: http://eureka.local/eureka/
```

* 运行容器

```
docker run --name influx-proxy-ops -d -p 8080:8080 -p 8081:8081 -e JAVA_OPTS="-Xmx1G -Xms1G" -e MYSQL_HOST=10.241.1.167:3306 -e MYSQL_USER=admin -e MYSQL_PASSWORD=admin  influx-proxy-ops
```

## 接口

*以下为Postman导出的接口*

```json
{
	"info": {
		"_postman_id": "f677b5d1-e993-4b02-8db9-deea23ee531e",
		"name": "influx-proxy-management-dev",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
	},
	"item": [
		{
			"name": "http://localhost:8080/management/nodes",
			"request": {
				"method": "POST",
				"header": [
					{
						"key": "Content-Type",
						"name": "Content-Type",
						"value": "application/json",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": " {\n        \"nodeName\": \"local2\",\n        \"url\": \"http://localhost:8086\",\n        \"writeTimeout\": 100000,\n        \"queryTimeout\": null,\n        \"healthCheckInterval\": null,\n        \"online\": false,\n        \"statusDescription\": null\n}"
				},
				"url": {
					"raw": "http://localhost:8080/management/nodes",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"management",
						"nodes"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:8080/management/nodes",
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:8080/management/nodes",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"management",
						"nodes"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:8080/management/nodes/5",
			"request": {
				"method": "PATCH",
				"header": [
					{
						"key": "Content-Type",
						"name": "Content-Type",
						"value": "application/json",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": " {\n        \"id\": 5,\n        \"nodeName\": \"local\",\n        \"url\": \"http://localhost:8086\",\n        \"writeTimeout\": null,\n        \"queryTimeout\": null,\n        \"healthCheckInterval\": null,\n        \"online\": true,\n        \"statusDescription\": null\n}"
				},
				"url": {
					"raw": "http://localhost:8080/management/nodes/5",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"management",
						"nodes",
						"5"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:8080/management/nodes/5",
			"request": {
				"method": "DELETE",
				"header": [
					{
						"key": "Content-Type",
						"name": "Content-Type",
						"type": "text",
						"value": "application/json"
					}
				],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:8080/management/nodes/5",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"management",
						"nodes",
						"5"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:8080/management/keyMappings",
			"request": {
				"method": "POST",
				"header": [
					{
						"key": "Content-Type",
						"name": "Content-Type",
						"value": "application/json",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\n        \"databaseRegex\": \".*\",\n        \"measurementRegex\": \"def\",\n        \"backendNodeNames\": \"test\"\n}"
				},
				"url": {
					"raw": "http://localhost:8080/management/keyMappings",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"management",
						"keyMappings"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:8080/management/keyMappings",
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:8080/management/keyMappings",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"management",
						"keyMappings"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:8080/management/keyMappings/1",
			"request": {
				"method": "PATCH",
				"header": [
					{
						"key": "Content-Type",
						"name": "Content-Type",
						"value": "application/json",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": "{\n        \"databaseRegex\": \".*\",\n        \"measurementRegex\": \"def\",\n        \"backendNodeNames\": \"test\"\n}"
				},
				"url": {
					"raw": "http://localhost:8080/management/keyMappings/1",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"management",
						"keyMappings",
						"1"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://localhost:8080/management/keyMappings/1",
			"request": {
				"method": "DELETE",
				"header": [
					{
						"key": "Content-Type",
						"name": "Content-Type",
						"value": "application/json",
						"type": "text"
					}
				],
				"body": {
					"mode": "raw",
					"raw": ""
				},
				"url": {
					"raw": "http://localhost:8080/management/keyMappings/1",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"management",
						"keyMappings",
						"1"
					]
				}
			},
			"response": []
		}
	]
}
```