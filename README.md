# bytetools


## 特性

#### 监听指定方法的耗时时间

#### 打印方法上的异常


可以打印异常的方法堆栈

` void loadConf() throws IOException`


## 使用方式

#### 配置文件

`<config>
    <!-- 配置耗时监听 -->
    <time-monitor>
        <excludeFilters>
            <exclude package="" clazz="" method="setNonNullParameter"/>
            <exclude package="" clazz="ProfessionalServiceAccessoryExpiredCleanTimer" method="getAccessoryCreateTime"/>
        </excludeFilters>
        <includeFilters>
            <include package="com.huawei.psm" clazz="" method=""/>
        </includeFilters>
    </time-monitor>
    <!-- 配置异常打印耗时-->
    <!--<exception-monitor>
        <excludeFilters>
            <exclude package="com.bytetools.test" clazz="Controller" method="send"/>
            <exclude package="com.bytetools.test" clazz="Controller" method="send"/>
        </excludeFilters>
        <includeFilters>
            <include package="com.bytetools.test" clazz="Expect" method="test"/>
        </includeFilters>
    </exception-monitor>-->
</config>`

#### 运行方式

在java启动命令上添加 -javaagent:xxxx.jar=configPathFile

BasicHttpEntity

