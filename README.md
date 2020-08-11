# 服务A
调用服务B和服务C

## 前提
- 安装Git
- 安装maven
- 安装docker
- 安装k8s集群
- 安装istio

## 打包
```
cd service-a/
mvn clean package
```

## 构建镜像
```
docker build -t kyg/service-a-docker .
```

## 查看镜像
```
docker images kyg/service-a-docker
```

## 部署服务（master节点）
```
kubectl apply -f service-a-master.yaml
```

## 部署服务（node节点）
```
kubectl apply -f service-a-1.yaml
```

## 暴露服务（master节点）
```
kubectl get svc
kubectl edit service service-a

spec:
  externalIPs:
  - 192.168.0.5
  - 172.190.105.90
```

- 192.168.0.5（内网IP）
- 172.190.105.90（外网IP）

## 入口网关（master节点）
```
kubectl apply -f service-a-gateway.yaml
```

## 访问
```
curl -d "username=admin&password=admin" http://192.168.0.5:8091/login
curl -d "username=admin&password=admin" http://172.190.105.90:8091/login
```