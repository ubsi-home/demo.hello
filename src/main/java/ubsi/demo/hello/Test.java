package ubsi.demo.hello;

import rewin.ubsi.consumer.Context;

/** 服务消费者测试类 */
public class Test {

    /** 命令行启动入口 */
    public static void main(String[] args) throws Exception {
        Context.startup(".");   // 启动UBSI Consumer
        // 访问微服务前必须：UBSI容器已经在localhost:7112上启动，并部署了ubsi.demo.hello微服务
        callAsync();
        callSync();
        Context.shutdown();              // 关闭UBSI Consumer

        /* 命令行输出结果：
return: hello consumer-sync
code: 0, data: hello consumer-async

           容器console的输出：
[INFO]  2022-03-04 16:26:58.233 liuxd-hp#7112   rewin.ubsi.container    rewin.ubsi.container    [1]rewin.ubsi.container.Bootstrap#start()#154   startup      "2.3.0"
[INFO]  2022-03-04 16:27:34.953 liuxd-hp#7112   rewin.ubsi.service      ubsi.demo.hello [37]ubsi.demo.hello.Service#init()#35   start   "ubsi"
[INFO]  2022-03-04 16:29:33.923 liuxd-hp#7112   rewin.ubsi.service      ubsi.demo.hello#hello   [51]ubsi.demo.hello.ServiceEntry#hello()#20     consumer-async  "hello ubsi"
[INFO]  2022-03-04 16:29:33.923 liuxd-hp#7112   rewin.ubsi.service      ubsi.demo.hello#hello   [50]ubsi.demo.hello.ServiceEntry#hello()#20     consumer-sync   "hello ubsi"
[INFO]  2022-03-04 16:36:32.299 liuxd-hp#7112   rewin.ubsi.service      ubsi.demo.hello [20]ubsi.demo.hello.Service#close()#42  stop    "ubsi"
[INFO]  2022-03-04 16:36:32.317 liuxd-hp#7112   rewin.ubsi.container    rewin.ubsi.container    [20]rewin.ubsi.container.Bootstrap#stop()#190   shutdown        "2.3.0"
         */
    }

    // 同步方式请求
    static void callSync() throws Exception {
        Context ubsi = Context.request("ubsi.demo.hello", "hello", "consumer-sync"); // 创建UBSI请求对象
        String ack = (String)ubsi.direct("localhost", 7112);    // 直接向指定容器发送同步请求，并等待返回结果
        System.out.println("return: " + ack);
    }

    // 异步方式请求
    static void callAsync() throws Exception {
        Context ubsi = Context.request("ubsi.demo.hello", "hello", "consumer-async"); // 创建UBSI请求对象
        ubsi.directAsync("localhost", 7112, (code, data) -> {
            // 在请求结果通知中的处理动作（如果是高耗时操作，为避免长时间占用I/O线程，应启动单独线程处理）
            System.out.println("code: " + code + ", data: " + data);
        }, false);   // 直接向指定容器发送异步请求
    }
}
