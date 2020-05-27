/*
 * Copyright © Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 * Description: ConfigUtils
 * Author: zWX827285
 * Create: 2020/5/21
 */

package com.bytetools.config;

import com.bytetools.entity.ExceptionMonitor;
import com.bytetools.entity.FilterEntity;
import com.bytetools.entity.TimeMonitor;
import com.bytetools.utils.StringUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author zWX827285
 * @version 1.0.0 2020/5/21
 * @see
 * @since PSM 1.0.5
 */
public class ConfigUtils {
    private static String CONFIG_FILE_PATH = "D:\\IdeaWorkSpace\\MyTools\\src\\main\\resources\\config.xml";

    public static TimeMonitor timeMonitor = new TimeMonitor();

    public static ExceptionMonitor exceptionMonitor = new ExceptionMonitor();

    public static void loadConfig(String configPath) {
        if (!StringUtils.isEmpty(configPath)) {
            CONFIG_FILE_PATH = configPath;
        }
        try {
            InputStream file = new FileInputStream(CONFIG_FILE_PATH);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setExpandEntityReferences(false);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document doc = documentBuilder.parse(file);
            Element root = doc.getDocumentElement();
            bulidConfig(root, "time-monitor", "includeFilters", "include", timeMonitor.includeFilters);
            bulidConfig(root, "time-monitor", "excludeFilters", "exclude", timeMonitor.excludeFilters);
            bulidConfig(root, "exception-monitor", "includeFilters", "include", exceptionMonitor.includeFilters);
            bulidConfig(root, "exception-monitor", "excludeFilters", "exclude", exceptionMonitor.excludeFilters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<FilterEntity> getIncludeFilters() {
        if (timeMonitor.includeFilters.isEmpty()) {
            return exceptionMonitor.includeFilters;
        } else {
            return timeMonitor.includeFilters;
        }
    }

    public static List<FilterEntity> getExcludeFilters() {
        if (timeMonitor.excludeFilters.isEmpty()) {
            return exceptionMonitor.excludeFilters;
        } else {
            return timeMonitor.excludeFilters;
        }
    }

    private static void bulidConfig(Element root, String element, String filters, String type,
        List<FilterEntity> filtersConfig) {
        Element timeMonitor = getChild(root, element);
        if (null != timeMonitor) {
            Element includeFilters = getChild(timeMonitor, filters);
            if (null != includeFilters) {
                Collection<Element> includes = getChildren(includeFilters, type);
                for (Element include : includes) {
                    FilterEntity filter = new FilterEntity();
                    filter.setPackageName(include.getAttribute("package"));
                    filter.setClassName(include.getAttribute("clazz"));
                    filter.setMethodName(include.getAttribute("method"));
                    filtersConfig.add(filter);
                }
            }

        }
    }

    private static Element getChild(Element parent, String childTagName) {
        if (null == parent) {
            return null;
        }

        Element result = null;
        Node child = parent.getFirstChild();

        //遍历parent的所有子节点
        while (null != child) {
            if (child instanceof Element) {
                Element element = (Element) child;
                String tagName = element.getTagName();
                if (tagName.equals(childTagName)) {
                    result = element;
                    break;
                }
            }

            child = child.getNextSibling();
        }

        return result;
    }

    private static Collection<Element> getChildren(Element parent, String childTagName) {
        Collection<Element> children = new ArrayList<Element>();
        if (null == parent) {
            return children;
        }
        Node child = parent.getFirstChild();

        //遍历parent节点的所有子节点
        while (null != child) {
            if (child instanceof Element) {
                Element element = (Element) child;
                String tagName = element.getTagName();

                /*
                 * childTagName不为null，则取标签为childTagName的子节点
                 * 否则取parent的所有子节点
                 *
                 * 注意判断中两个条件的先后顺序
                 */
                if (null == childTagName || childTagName.equals(tagName)) {
                    children.add(element);
                }
            }

            child = child.getNextSibling();
        }

        return children;
    }

}
