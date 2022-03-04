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
        /* 输出结果：
[INFO]	2022-03-04 14:36:04.562	liuxd-hp#7112	rewin.ubsi.service	ubsi.demo.hello	[1]ubsi.demo.hello.Service#init()#35	start	"ubsi"
[INFO]	2022-03-04 14:36:08.522	liuxd-hp#7112	rewin.ubsi.container	rewin.ubsi.container	[1]rewin.ubsi.container.Bootstrap#start()#154	startup	"2.3.0"
return: hello tester
[INFO]	2022-03-04 14:36:10.522	liuxd-hp#7112	rewin.ubsi.service	ubsi.demo.hello#hello	[17]ubsi.demo.hello.ServiceEntry#hello()#20	tester	"hello ubsi"
[INFO]	2022-03-04 14:36:10.522	liuxd-hp#7112	junit	test-hello	[1]ubsi.demo.hello.ServiceEntryTest#testHello()#42	return	"hello tester"
[INFO]	2022-03-04 14:36:10.537	liuxd-hp#7112	rewin.ubsi.service	ubsi.demo.hello	[1]ubsi.demo.hello.Service#close()#42	stop	"ubsi"
[INFO]	2022-03-04 14:36:10.537	liuxd-hp#7112	rewin.ubsi.container	rewin.ubsi.container	[1]rewin.ubsi.container.Bootstrap#stop()#190	shutdown	"2.3.0"
         */
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
        /* 输出结果：
[INFO]	2022-03-04 14:53:54.957	liuxd-hp#7112	rewin.ubsi.service	ubsi.demo.hello	[1]ubsi.demo.hello.Service#init()#35	start	"ubsi"
[INFO]	2022-03-04 14:53:58.910	liuxd-hp#7112	rewin.ubsi.container	rewin.ubsi.container	[1]rewin.ubsi.container.Bootstrap#start()#154	startup	"2.3.0"
{
  "name_restart": "ubsi",
  "name": "ubsi",
  "name_comment": "服务实例的名字"
}
return: hello tester
[INFO]	2022-03-04 14:54:01.004	liuxd-hp#7112	rewin.ubsi.service	ubsi.demo.hello#hello	[23]ubsi.demo.hello.ServiceEntry#hello()#20	tester	"hello new-name"
[INFO]	2022-03-04 14:54:01.004	liuxd-hp#7112	junit	test-hello	[1]ubsi.demo.hello.ServiceEntryTest#testHello()#39	return	"hello tester"
[INFO]	2022-03-04 14:54:01.004	liuxd-hp#7112	rewin.ubsi.service	ubsi.demo.hello	[1]ubsi.demo.hello.Service#close()#42	stop	"new-name"
[INFO]	2022-03-04 14:54:01.004	liuxd-hp#7112	rewin.ubsi.container	rewin.ubsi.container	[1]rewin.ubsi.container.Bootstrap#stop()#190	shutdown	"2.3.0"
         */
    }

}
