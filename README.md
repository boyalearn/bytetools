# bytetools


## 特性

#### 监听指定方法的耗时时间

#### 打印方法上的异常


可以打印异常的方法堆栈

```java
void loadConf() throws IOException
```


## 使用方式

#### 配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<config>
    <!-- 如果配置了文件路径那么日志将打印到文件中 -->
    <log-file fileName="D://monitor.log" />
    <!-- type目前支持两种，一种是监听方法调用耗时(TIME)、一种是监听方法中处理的异常信息方便问题定位(EXCEPTION) -->
    <agent type="com.bytecode.config.AgentType.TIME">
        <!-- 包含的方法配置 -->
        <include package="com.test" clazz="" method=""/>
        <!-- 排除的方法配置 -->
        <exclude package="com.test.service" clazz="" method="eat"/>
    </agent>
</config>
```

#### 运行方式

在java启动命令上添加 -javaagent:xxxx.jar=configPathFile




#### bug修复方式

JDK8以上正常运行

``` xml
<!-- https://mvnrepository.com/artifact/org.javassist/javassist -->
<dependency>
    <groupId>org.javassist</groupId>
    <artifactId>javassist</artifactId>
    <version>3.20.0-GA</version>
</dependency>

```
