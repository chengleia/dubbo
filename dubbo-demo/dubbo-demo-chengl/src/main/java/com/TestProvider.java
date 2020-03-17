package com;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.config.*;

import java.util.ArrayList;
import java.util.List;

public class TestProvider {

    public static void main(String[] args) {

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("yyy");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("10.20.130.230:9090");
        registry.setUsername("aaa");
        registry.setPassword("bbb");

        // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接

        // 引用远程服务
        // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        ReferenceConfig<XxxService> reference = new ReferenceConfig<XxxService>();
        reference.setApplication(application);
        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
        reference.setInterface(XxxService.class);
        reference.setVersion("1.0.0");

        // 和本地bean一样使用xxxService
        XxxService xxxService = reference.get(); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用


        // 服务实现
        Demo2ServiceImpl xxxService = new Demo2ServiceImpl();

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("chengl-app");

        // 连接注册中心配置
//        RegistryConfig registry = new RegistryConfig();
//        registry.setUsername("aaa");
//        registry.setPassword("bbb");
//
//        RegistryConfig registry2 = new RegistryConfig();
//        registry2.setUsername("aaa");
//        registry2.setPassword("bbb");
//
//        List ab = new ArrayList();
//        ab.add(registry);
//        ab.add(registry2);

        // 服务提供者协议配置
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(12345);
        protocol.setThreads(200);


        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口

        // 服务提供者暴露服务配置
        // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        ServiceConfig<Demo2Service> service = new ServiceConfig<Demo2Service>();
        service.setApplication(application);
        // 多个注册中心可以用setRegistries()
//        service.setRegistries(ab);
        // 多个协议可以用setProtocols()
        service.setProtocol(protocol);
        service.setInterface(Demo2Service.class);
        service.setRef(xxxService);


        // 暴露及注册服务
        service.export();
    }

}
