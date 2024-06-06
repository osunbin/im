package com.bin.im.common.id;

public class UidContext {

    private UidGenerator uidGenerator;
    /**
     * 除余基数(建议使用固定值)--控制位移
     */
    private Integer fixed = 25;
    
    /**
     * 基因因子
     */
    public Long factor;
    
    public UidContext(UidGenerator uidGenerator) {
        this.uidGenerator = uidGenerator;
    }
    


    
    /**
     * @方法名称 getUID
     * @功能描述 <pre>获取ID</pre>
     * @param group 分组
     */
    public long getUID(String group) {
        return uidGenerator.getUID();
    }

    /**
     * @方法名称 parseUID
     * @功能描述 <pre>解析ID</pre>
     * @param uid 
     * @return 输出json字符串：{\"UID\":\"\",\"timestamp\":\"\",\"workerId\":\"\",\"dataCenterId\":\"\",\"sequence\":\"\"}
     */
    public String parseUID(long uid) {
        return uidGenerator.parseUID(uid);
    }
    

    
    /**
     * 根据基因因子生成基因id
     */
    public Long geneId(Long primitiveId) {
        if (null == factor) {
            return primitiveId;
        }
        int moveBit = Integer.toBinaryString(fixed).length() - 1;
        // 加入factor基因
        return (primitiveId << moveBit) | (factor % fixed);
    }
    
    /**
     * 还原id
     */
    public long restoreId(long uid) {
        if (null == factor) {
            return uid;
        }
        int leftMoveBit = Integer.toBinaryString(fixed).length() - 1;
        return uid >>> leftMoveBit;
    }
    

    public Integer getFixed() {
        return fixed;
    }
    
    public void setFixed(Integer fixed) {
        this.fixed = fixed;
    }
    
    public Long getFactor() {
        return factor;
    }
    
    public void setFactor(Long factor) {
        this.factor = factor;
    }
}
