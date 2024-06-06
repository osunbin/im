package com.bin.im.entry.spi;

import com.bin.im.entry.common.NacosHelper;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.MethodConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public class DubboGenericServiceHelper {
    private static Logger logger = LoggerFactory.getLogger(DubboGenericServiceHelper.class);

    /**
     * 默认协议
     */
    private static final String REG_ZK = "zookeeper";

    /**
     * 默认消费者名称--前缀
     */
    private static final String CONSUMER = "im-entry";

    /**
     * 消费应用缓存
     */
    public static Map<String, ApplicationConfig> apps = new HashMap<String, ApplicationConfig>();

    private static List<String> primitiveList = new ArrayList<String>();

    static {
        primitiveList.add("java.lang.Integer");
        primitiveList.add("java.lang.Long");
        primitiveList.add("java.lang.Short");
        primitiveList.add("java.lang.Character");
        primitiveList.add("java.lang.Byte");
        primitiveList.add("java.lang.Boolean");
        primitiveList.add("java.lang.Float");
        primitiveList.add("java.lang.Double");
    }

    /**
     * @param address 注册地址
     * @param service 服务名
     * @return 远程通用服务
     * @方法名称 getDubboService
     * @功能描述 <pre>根据 注册地址 和服务名 获取 远程通用服务</pre>
     */
    public static GenericService getDubboService(String address, String service) {
        return getDubboService(service, address, REG_ZK);
    }

    /**
     * @param address  注册中心地址(e.g,"192.168.10.161:2182,192.168.10.163:2182")
     * @param protocol 注册协议
     * @param service  服务名
     * @param version  版本
     * @return 远程通用服务
     * @方法名称 getDubboService
     * @功能描述 <pre>根据 注册地址 和服务名 获取 远程通用服务</pre>
     */
    public static GenericService getDubboService(String address, String protocol, String service) {
        // 泛化引用缓存
        return getReference(address, protocol, service).get();
    }

    /**
     * @param regAddress 注册中心地址(e.g,"192.168.10.161:2182,192.168.10.163:2182")
     * @param service    服务方法服务名
     * @param method     服务方法
     * @param params     方法参数
     * @param version    方法版本
     * @return 返回结果(Map类型)
     * @方法名称 invoke
     * @功能描述 <pre>执行方法</pre>
     */
    public static Object invoke(String regAddress, String service, String method, Object[] params) {
        return invoke(regAddress, service, method, params, false);
    }

    /**
     * @param regAddress 注册中心地址(e.g,"192.168.10.161:2182,192.168.10.163:2182")
     * @param service    服务方法服务名
     * @param method     服务方法
     * @param params     方法参数
     * @param version    方法版本
     * @param async      是否异步
     * @return 返回结果(Map类型)
     * @方法名称 invoke
     * @功能描述 <pre>执行方法</pre>
     */
    public static Object invoke(String regAddress, String service, String method, Object[] params, boolean async) {
        return invoke(regAddress, service, method, params, null, async);
    }

    /**
     * @param regAddress     注册中心地址(e.g,"192.168.10.161:2182,192.168.10.163:2182")
     * @param service        服务方法服务名
     * @param method         服务方法
     * @param params         方法参数
     * @param parameterTypes 方法参数类型
     * @param version        方法版本
     * @param async          是否异步
     * @return 返回结果(Map类型)
     * @方法名称 invoke
     * @功能描述 <pre>执行方法</pre>
     */
    public static Object invoke(String regAddress, String service, String method, Object[] params, String[] parameterTypes, boolean async) {
        ReferenceConfig<GenericService> rc = getReference(regAddress, REG_ZK, service);

        GenericService genericService = rc.get();
        Object result = genericService.$invoke(method, parameterTypes, params);
        return result;
    }

    /**
     * @param address  注册中心地址(e.g,"192.168.10.161:2182,192.168.10.163:2182")
     * @param protocol 注册协议
     * @param service  服务名
     * @return
     * @方法名称 getReference
     * @功能描述 <pre>获取引用对象</pre>
     */
    static ReferenceConfig<GenericService> getReference(String address, String protocol, String service) {
        ApplicationConfig application = getApp(address, protocol);
        // 泛化引用配置
        ReferenceConfig<GenericService> rc = new ReferenceConfig<GenericService>();
        rc.setGeneric("true");
        rc.setApplication(application);
        rc.setInterface(service);

        return rc;
    }

    /**
     * @param address  注册中心地址
     * @param protocol 注册中心地址协议
     * @return 消费应用对象
     * @方法名称 getApp
     * @功能描述 <pre>获取应用信息配置</pre>
     */
    static ApplicationConfig getApp(String address, String protocol) {
        ApplicationConfig application = apps.get(address);
        if (null == application) {
            synchronized (DubboGenericServiceHelper.class) {
                application = apps.get(address);
                if (null == application) {
                    application = new ApplicationConfig();
                    application.setName(CONSUMER + (apps.size() + 1));
                    // 连接注册中心配置
                    RegistryConfig registryConfig = new RegistryConfig();
                    registryConfig.setProtocol(protocol);
                    registryConfig.setAddress(address);
                    application.setRegistry(registryConfig);
                    apps.put(address, application);
                }
            }
        }
        return application;
    }
}
