package com.bin.im.common.mini.yaml;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.bin.im.common.mini.yaml.YamlUtil.asMapping;
import static com.bin.im.common.mini.yaml.YamlUtil.asScalar;
import static com.bin.im.common.mini.yaml.YamlUtil.asSequence;

public class YamlMappingImpl extends AbstractYamlNode implements MutableYamlMapping {
    private Map<String, YamlNode> children = Collections.emptyMap();

    YamlMappingImpl(YamlNode parent, String nodeName) {
        super(parent, nodeName);
    }

    @Override
    public YamlNode child(String name) {
        return children.get(name);
    }

    @Override
    public YamlMapping childAsMapping(String name) {
        return asMapping(child(name));
    }

    @Override
    public YamlSequence childAsSequence(String name) {
        return asSequence(child(name));
    }

    @Override
    public YamlScalar childAsScalar(String name) {
        return asScalar(child(name));
    }

    @Override
    public <T> T childAsScalarValue(String name) {
        return childAsScalar(name).nodeValue();
    }

    @Override
    public <T> T childAsScalarValue(String name, Class<T> type) {
        return childAsScalar(name).nodeValue(type);
    }

    @Override
    public Iterable<YamlNode> children() {
        return children.values();
    }

    @Override
    public Iterable<YamlNameNodePair> childrenPairs() {
        List<YamlNameNodePair> pairs = new LinkedList<YamlNameNodePair>();
        for (Map.Entry<String, YamlNode> child : children.entrySet()) {
            pairs.add(new YamlNameNodePair(child.getKey(), child.getValue()));
        }
        return pairs;
    }

    @Override
    public void addChild(String name, YamlNode node) {
        getOrCreateChildren().put(name, node);
    }

    @Override
    public void removeChild(String name) {
        children.remove(name);
    }

    private Map<String, YamlNode> getOrCreateChildren() {
        if (children == Collections.<String, YamlNode>emptyMap()) {
            children = new LinkedHashMap<String, YamlNode>();
        }

        return children;
    }

    @Override
    public int childCount() {
        return children.size();
    }

    @Override
    public String toString() {
        return "YamlMappingImpl{"
                + "nodeName=" + nodeName()
                + ", children=" + children
                + '}';
    }

}