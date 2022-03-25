package ubsi.demo.hello;

import rewin.ubsi.annotation.USEntry;
import rewin.ubsi.annotation.USParam;
import rewin.ubsi.container.ServiceContext;

/** 微服务的接口类（被主类继承） */
public class ServiceEntry {
    @USEntry(
            tips = "打个招呼",
            params = {
                    @USParam(name = "consumer", tips = "调用者的名字"),       // 接口参数说明
            },
            result = "响应",      // 返回值的说明
            readonly = true,     // 只读
            timeout = 1          // 超时设置（秒数）
    )
    /* 接口的第一个参数必须是ServiceContext，返回值可以是任意类型 */
    public String hello(ServiceContext ctx, String consumer) throws Exception {
        ctx.getLogger().info(Service.myName, "hello, " + consumer);
        return Service.myName + ": hello, " + consumer;
    }
}
