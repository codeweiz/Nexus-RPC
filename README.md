## 自定义 RPC 框架 —— Nexus

### 包结构介绍：

- Nexus-Core 核心包
    - annotation RPC 相关注解
    - codec 编码器和解码器
    - compress 压缩与解压缩
    - config 自定义配置
    - constants 常量
    - enums 自定义注解
    - exception 自定义异常
    - extension 扩展
    - factory 自定义工厂
    - handler 处理器
    - loadbalance 负载均衡
    - netty netty 相关
    - pojo RPC 实体类
    - provider 服务提供者
    - proxy 代理
    - registry 注册中心
    - serialize 序列化与反序列化
    - server netty、socket 服务
    - spring 自定义扫描器、扫描器注册器
    - transport 网络传输
    - utils 工具类

### 技术栈：

1. 服务注册与发现：Zookeeper
2. 序列化与反序列化：Kryo、Hessian、ProtoStuff
3. 网络传输：Netty、Socket
4. 压缩与解压：Gzip
5. 代理：Java reflect 包代理

### 使用方式：

@RpcService(group = "", version = "") 注解，标注一个可用的服务，供其他服务调用。

@RpcReference(group = "", version = "") 注解，标注一个服务的引用，通过服务引用可以调用远程服务的方法。

@RpcScan(basePackage = {""}) 注解，标识需要扫描的包，扫描这些包下的 @RpcService 注解，把这些服务注册到注册中心

