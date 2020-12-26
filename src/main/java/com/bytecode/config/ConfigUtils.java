/*
 * Copyright © Huawei Technologies Co., Ltd. 2018-2019. All rights reserved.
 * Description: ConfigUtils
 * Author: zWX827285
 * Create: 2020/5/21
 */

package com.bytecode.config;

import com.bytecode.utils.StringUtils;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zWX827285
 * @version 1.0.0 2020/5/21
 * @see
 * @since PSM 1.0.5
 */
public class ConfigUtils {
    private static String CONFIG_FILE_PATH = "D:\\IdeaWorkSpace\\MyGitHub\\bytetools\\agent-config.xml";

    private final static String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";

    private static AgentConfig CONFIG = new AgentConfig();

    private static List<String> CGLIB_CLASS = new ArrayList<String>();

    private static boolean parse = false;

    private static AgentType agentType;

    public static String getLogFileName() {

        return CONFIG.getLogFileName();
    }

    public static AgentType getAgentType() {
        return agentType;
    }

    public static void setAgentType(AgentType agentType) {
        ConfigUtils.agentType = agentType;
    }

    public static List<String> getCglibClass() {
        return CGLIB_CLASS;
    }

    public static void addCglibClass(String cglibClass) {
        CGLIB_CLASS.add(cglibClass);
    }

    public static List<ConfigNode> getConfigNodeList(boolean isInclude) {
        Set<ConfigNode> set = new HashSet<ConfigNode>();
        if (isInclude) {
            for (ConfigNode node : CONFIG.getIncludes()) {
                set.add(node);


            }
        } else {
            for (ConfigNode node : CONFIG.getExcludes()) {
                set.add(node);

            }
        }
        List<ConfigNode> list = new ArrayList<ConfigNode>();
        for (ConfigNode node : set) {
            list.add(node);
        }
        return list;
    }

    public static boolean shouldIncludeClassName(String className) {
        List<ConfigNode> excludeList = getConfigNodeList(false);
        for (ConfigNode node : excludeList) {
            if (isIncludeClass(node, StringUtils.getPackageName(className), StringUtils.getClassName(className))) {
                return false;
            }
        }
        List<ConfigNode> includeList = getConfigNodeList(true);
        for (ConfigNode node : includeList) {
            if (isIncludeClass(node, StringUtils.getPackageName(className), StringUtils.getClassName(className))) {
                return true;
            }
        }

        return false;
    }

    private static boolean isIncludeClass(ConfigNode node, String packageName, String className) {
        if (StringUtils.isEmpty(node.getClassConfig()) || className.contains(node.getClassConfig())) {
            if (StringUtils.isEmpty(node.getPackageConfig()) || packageName.equals(node.getPackageConfig())) {
                return true;
            }
        }
        return false;
    }


    public static boolean shouldIncludeClass(String packageName, String className, String methodName) {
        for (ConfigNode node : getConfigNodeList(false)) {
            if (isIncludeMethod(node, packageName, className, methodName)) {
                return false;
            }
        }
        for (ConfigNode node : getConfigNodeList(true)) {
            if (isIncludeMethod(node, packageName, className, methodName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isIncludeMethod(ConfigNode node, String packageName, String className, String methodName) {
        if (StringUtils.isEmpty(node.getMethodConfig()) || methodName.contains(node.getMethodConfig())) {
            if (StringUtils.isEmpty(node.getClassConfig()) || className.contains(node.getClassConfig())) {
                if (StringUtils.isEmpty(node.getPackageConfig()) || packageName.equals(node.getPackageConfig())) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void loadConfig(String configPath) {
        if (!StringUtils.isEmpty(configPath)) {
            CONFIG_FILE_PATH = configPath;
        }

        if (parse) {
            return;
        }

        try {
            InputStream file = new FileInputStream(CONFIG_FILE_PATH);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(FEATURE, true);
            dbf.setExpandEntityReferences(false);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document doc = documentBuilder.parse(file);
            Element root = doc.getDocumentElement();
            parseLogFileInfo(root);
            parseAgentType(root);
            parseAgentConfigInfo(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

        parse = true;
    }

    private static void parseAgentType(Element root) {
        String type = root.getElementsByTagName("agent").item(0).getAttributes().item(0).getNodeValue();
        setAgentType(AgentType.getAgentType(type));
    }

    private static void parseAgentConfigInfo(Element root) throws ClassNotFoundException {
        parseExcludesConfig(root);
        parseIncludesConfig(root);
    }

    private static void parseIncludesConfig(Node node) {
        NodeList includes = node.getOwnerDocument().getDocumentElement().getElementsByTagName("include");
        CONFIG.setIncludes(parseItemConfig(includes));
    }

    private static void parseExcludesConfig(Node node) {
        NodeList excludes = node.getOwnerDocument().getDocumentElement().getElementsByTagName("exclude");

        CONFIG.setExcludes(parseItemConfig(excludes));
    }

    private static List<ConfigNode> parseItemConfig(NodeList nodeList) {
        List<ConfigNode> list = new ArrayList<ConfigNode>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            NamedNodeMap attrs = item.getAttributes();
            ConfigNode configNode = new ConfigNode();
            for (int j = 0; j < attrs.getLength(); j++) {
                if ("package".equals(attrs.item(j).getNodeName())) {
                    configNode.setPackageConfig(attrs.item(j).getNodeValue());
                }
                if ("clazz".equals(attrs.item(j).getNodeName())) {
                    configNode.setClassConfig(attrs.item(j).getNodeValue());
                }
                if ("method".equals(attrs.item(j).getNodeName())) {
                    configNode.setMethodConfig(attrs.item(j).getNodeValue());
                }
            }
            list.add(configNode);
        }
        return list;
    }

    private static String getAgentType(NamedNodeMap agentAttrs) {
        for (int j = 0; j < agentAttrs.getLength(); j++) {
            Node agentAttr = agentAttrs.item(j);
            if ("type".equals(agentAttr.getNodeName())) {
                return agentAttr.getNodeValue();
            }
        }
        return null;
    }

    private static void parseLogFileInfo(Element root) throws Exception {
        NodeList logfile = root.getElementsByTagName("log-file");
        if (logfile.getLength() > 1) {
            throw new Exception("log-file config only one");
        }
        NamedNodeMap attr = logfile.item(0).getAttributes();
        String fileName = attr.item(0).getNodeValue();
        CONFIG.setLogFileName(fileName);
    }

    public static boolean shouldInclude(String className, String methodName) {
        return ConfigUtils.shouldIncludeClass(StringUtils.getPackageName(className), StringUtils.getClassName(className), methodName);
    }

}
