package ubsi.demo.hello;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import rewin.ubsi.cli.Request;
import rewin.ubsi.common.Util;
import rewin.ubsi.consumer.Context;
import rewin.ubsi.container.Bootstrap;

import java.util.Map;

/** 
* ServiceEntry Tester. 
*/
public class ServiceEntryTest { 

    @Before
    public void before() throws Exception {
        Bootstrap.start();      // 启动测试容器，缺省为 localhost#7112
        /*
          启动容器时需要rewin.ubsi.module.json文件，否则容器不会自动加载微服务
          微服务加载时如果"startup"为true，则启动微服务（执行@USInit方法）
         */
    }

    @After
    public void after() throws Exception {
        Bootstrap.stop();       // 关闭测试容器，关闭时会依次停止加载的所有微服务（执行@USClose方法）
    }

    /**
    * 服务接口测试
    */
    @Test
    public void testHello() throws Exception {
        // 创建ubsi请求对象
        Context ubsi = Context.request(Service.SERVICE_NAME, "hello", "tester");
        // 发起ubsi请求：direct直连方式通常用于测试/容器治理，正常情况下应该使用call()路由方式
        String ack = (String)ubsi.direct("localhost", 7112);
        // 通过日志输出结果
        Context.getLogger("junit", "test-hello").info("return", ack);
        // 通过console输出结果
        System.out.println("return: " + ack);
    }

    /**
     * 参数配置测试
     *      微服务的参数配置接口通常会由UBSI治理工具调用，这里的测试可以用来验证接口的实现是否正确
     */
    @Test
    public void testConfig() throws Exception {
        // 获取微服务的配置参数：向容器控制器（名为""的微服务）发getConfig请求
        Context ubsi = Context.request("", "getConfig", Service.SERVICE_NAME);
        Map config = (Map)ubsi.direct("localhost", 7112);   // UBSI在传输数据时会将自定义的Java-Class映射为Map
        Request.printJson(config);  // 输出获取到的配置信息

        // 设置微服务的配置参数：向容器控制器（名为""的微服务）发setConfig请求
        Map new_cfg = Util.toMap("name", "new-name");
        String cfg_json = new Gson().toJson(new_cfg);
        ubsi = Context.request("", "setConfig", Service.SERVICE_NAME, cfg_json);
        ubsi.direct("localhost", 7112);     // 设置新的参数（会生成配置文件：rewin.ubsi.modules/ubsi.demo.hello/config.json）

        testHello();    // 调用hello接口，查看运行参数是否修改
    }

}
