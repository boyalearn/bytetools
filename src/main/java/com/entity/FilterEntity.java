/*
 * Copyright © Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 * Description: FiliterEntity
 * Author: zWX827285
 * Create: 2020/5/21
 */

package com.bytetools.entity;

/**
 * @author zWX827285
 * @version 1.0.0 2020/5/21
 * @see
 * @since PSM 1.0.5
 */
public class FilterEntity {
    private String packageName;

    private String className;

    private String methodName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
