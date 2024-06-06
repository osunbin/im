package com.bin.im.common.mini.yaml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class YamlSequenceImpl extends AbstractYamlNode implements MutableYamlSequence {
    private List<YamlNode> children = Collections.emptyList();

    YamlSequenceImpl(YamlNode parent, String nodeName) {
        super(parent, nodeName);
    }

    @Override
    public YamlNode child(int index) {
        if (index >= children.size()) {
            return null;
        }
        return children.get(index);
    }

    @Override
    public Iterable<YamlNode> children() {
        return children;
    }

    @Override
    public YamlMapping childAsMapping(int index) {
        return YamlUtil.asMapping(child(index));
    }

    @Override
    public YamlSequence childAsSequence(int index) {
        return YamlUtil.asSequence(child(index));
    }

    @Override
    public YamlScalar childAsScalar(int index) {
        return YamlUtil.asScalar(child(index));
    }

    @Override
    public <T> T childAsScalarValue(int index) {
        return childAsScalar(index).nodeValue();
    }

    @Override
    public <T> T childAsScalarValue(int index, Class<T> type) {
        return childAsScalar(index).nodeValue(type);
    }

    @Override
    public void addChild(YamlNode child) {
        getOrCreateChildren().add(child);
    }

    private List<YamlNode> getOrCreateChildren() {
        if (children == Collections.<YamlNode>emptyList()) {
            children = new ArrayList<YamlNode>();
        }

        return children;
    }

    @Override
    public int childCount() {
        return children.size();
    }

    @Override
    public String toString() {
        return "YamlSequenceImpl{"
                + "nodeName=" + nodeName()
                + ", children=" + children
                + '}';
    }
}