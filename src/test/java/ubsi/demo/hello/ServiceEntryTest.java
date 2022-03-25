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
         */
    }

    @After
    public void after() throws Exception {
        Bootstrap.stop();       // 关闭测试容器
    }

    /**
    * 服务接口测试
    */
    @Test
    public void testHello() throws Exception {
        Context ubsi = Context.request(Service.SERVICE_NAME, "hello", "tester");
        String ack = (String)ubsi.direct("localhost", 7112);
        Context.getLogger("junit", "test-hello").info("return", ack);
        System.out.println("return: " + ack);
    }

    /**
     * 参数配置测试
     *      微服务的参数配置接口通常会由UBSI治理工具调用，这里的测试可以用来验证接口的实现是否正确
     */
    @Test
    public void testConfig() throws Exception {
        Context ubsi = Context.request("", "getConfig", Service.SERVICE_NAME);
        Map config = (Map)ubsi.direct("localhost", 7112);   // UBSI在传输数据时会将自定义的Java-Class映射为Map
        Request.printJson(config);  // 输出获取到的配置信息

        Map new_cfg = Util.toMap("name", "new-name");
        String cfg_json = new Gson().toJson(new_cfg);
        ubsi = Context.request("", "setConfig", Service.SERVICE_NAME, cfg_json);
        ubsi.direct("localhost", 7112);     // 设置新的参数（会生成配置文件：rewin.ubsi.modules/ubsi.demo.hello/config.json）

        testHello();    // 调用hello接口，查看运行参数是否修改
    }

}
