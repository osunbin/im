package com.bin.im.entry.tcp.handle;

import com.bin.im.entry.spi.Invoker;
import com.bin.im.entry.spi.InvokerAfter;
import com.bin.im.entry.tcp.TcpHeader;
import com.bin.im.entry.tcp.session.OnePeer;

import java.util.Map;

public class EntryContext {

    private long seq;

    private Map<String, Object> parameterMap;

    private Map<String, Object> attachments;

    private OnePeer onePeer;

    private long logId;

    private String logPre;

    private String module;
    private String cmd;
    private String apiInterface;
    private String method;

    private  TcpHeader tcpHeader;

    private Invoker invoker;

    private InvokerAfter after;

    private Map<String, Object> resultMap;

    public EntryContext() {

    }

    public EntryContext(TcpHeader tcpHeader) {
        this.tcpHeader = tcpHeader;
    }


    public TcpHeader getTcpHeader() {
        return tcpHeader;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public Map<String, Object> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

    public OnePeer getOnePeer() {
        return onePeer;
    }

    public void setOnePeer(OnePeer onePeer) {
        this.onePeer = onePeer;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public String getLogPre() {
        return logPre;
    }

    public void setLogPre(String logPre) {
        this.logPre = logPre;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }



    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    public String getApiInterface() {
        return apiInterface;
    }

    public void setApiInterface(String apiInterface) {
        this.apiInterface = apiInterface;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public InvokerAfter getAfter() {
        return after;
    }

    public void setAfter(InvokerAfter after) {
        this.after = after;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    public void invoke() {
        if (invoker != null) {
            invoker.invoke(this);
        }
    }

    public void onAfter() {
        if (getAfter() != null) {
            getAfter().after(this);
        }
    }
}
