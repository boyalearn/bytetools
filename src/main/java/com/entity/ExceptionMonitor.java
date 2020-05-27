/*
 * Copyright © Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 * Description: ExceptionMonitor
 * Author: zWX827285
 * Create: 2020/5/21
 */

package com.bytetools.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zWX827285
 * @version 1.0.0 2020/5/21
 * @see
 * @since PSM 1.0.5
 */
public class ExceptionMonitor {
    public List<FilterEntity> excludeFilters = new ArrayList<FilterEntity>();

    public List<FilterEntity> includeFilters = new ArrayList<FilterEntity>();
}
