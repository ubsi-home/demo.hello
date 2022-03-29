package ubsi.demo.hello;

import rewin.ubsi.consumer.Context;

/** 服务消费者测试类 */
public class Test {

    /** 命令行启动入口 */
    public static void main(String[] args) throws Exception {
        Context.startup(".");   // 启动UBSI Consumer，指定"."为当前工作目录
        callAsync();
        callSync();
        Context.shutdown();              // 关闭UBSI Consumer
    }

    // 同步方式请求
    static void callSync() throws Exception {
        Context ubsi = Context.request("ubsi.demo.hello", "hello", "consumer-sync"); // 创建UBSI请求对象
        String ack = (String)ubsi.call();    // 同步方式发送请求，并等待返回结果
        System.out.println("return: " + ack);
    }

    // 异步方式请求
    static void callAsync() throws Exception {
        Context ubsi = Context.request("ubsi.demo.hello", "hello", "consumer-async"); // 创建UBSI请求对象
        ubsi.callAsync((code, data) -> {
            // 在请求结果通知中的处理动作（如果是高耗时操作，为避免长时间占用I/O线程，应启动单独线程处理）
            System.out.println("code: " + code + ", data: " + data);
        }, false);   // 异步方式发送请求，通过回调得到结果
    }
}
