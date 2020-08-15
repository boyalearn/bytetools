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
<log-file fileName="D://monitor.log" />
<agent type="com.bytecode.config.AgentType.TIME">
    <include package="com.test" clazz="" method=""/>
    <exclude package="com.test.service" clazz="" method="eat"/>
</agent>
<agent type="com.bytecode.config.AgentType.EXCEPTION">
    <include package="com.test.service" clazz="" method="eat"/>
    <exclude package="com.test" clazz="" method="end"/>
</agent>
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
