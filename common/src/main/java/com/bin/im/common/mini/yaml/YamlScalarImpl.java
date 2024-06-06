package com.bin.im.common.mini.yaml;

public class YamlScalarImpl extends AbstractYamlNode implements MutableYamlScalar {
    private Object value;

    public YamlScalarImpl(YamlNode parent, String nodeName, Object value) {
        super(parent, nodeName);
        this.value = value;
    }

    @Override
    public <T> boolean isA(Class<T> type) {
        return value != null && value.getClass().isAssignableFrom(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T nodeValue() {
        return (T) value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T nodeValue(Class<T> type) {
        if (!isA(type)) {
            throw new YamlException("The scalar's type " + value.getClass() + " is not the expected " + type);
        }
        return (T) value;
    }

    @Override
    public void setValue(Object newValue) {
        value = newValue;
    }

    @Override
    public String toString() {
        return "YamlScalarImpl{"
                + "nodeName=" + nodeName()
                + ", value=" + value
                + '}';
    }
}
