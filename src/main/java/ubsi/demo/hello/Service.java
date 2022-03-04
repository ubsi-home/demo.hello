package ubsi.demo.hello;

import rewin.ubsi.annotation.*;
import rewin.ubsi.common.Util;
import rewin.ubsi.container.ServiceContext;

/** 微服务的主类 */
@UService(
        name = Service.SERVICE_NAME,    // 默认的服务名字
        tips = "UBSI微服务示例",         // 服务说明
        version = "1.0.0",              // 接口的版本
        release = true,                 // 发布状态：release | snapshot
        singleton = false               // 支持多实例并行
)
public class Service extends ServiceEntry {
    final static String SERVICE_NAME = "ubsi.demo.hello";   // 默认的服务名字
    final static String CONFIG_FILE = "config.json";        // 配置文件名

    static String myName = "ubsi";      // 参数配置项，用来demo动态参数配置

    /** 服务实例启动时的初始化动作 */
    @USInit
    public static void init(ServiceContext ctx) throws Exception {
        // 读取配置文件
        Config config = ctx.readDataFile(CONFIG_FILE, Config.class);
        if ( config != null ) {
            // 读到了配置文件，根据配置文件设置运行参数
            myName = config.name;
        }
        /*
          如果还有其他资源/配置文件放在resources下，可以使用ctx.getResourceAsStream("filename")来读取
         */

        // 输出服务启动日志
        ctx.getLogger().info("start", myName);
    }

    /** 服务实例停止时的资源清理动作 */
    @USClose
    public static void close(ServiceContext ctx) throws Exception {
        // 输出服务停止日志
        ctx.getLogger().info("stop", myName);
    }

    /** 返回运行信息 */
    @USInfo
    public static String info(ServiceContext ctx) throws Exception {
        return myName;
    }

    /** 返回配置参数 */
    @USConfigGet
    public static Config getConfig(ServiceContext ctx) throws Exception {
        Config config = new Config();
        config.name = myName;           // 配置项的运行值
        config.name_restart = myName;   // 配置项的配置值（该配置项立即生效，不需要从配置文件中读取）
        return config;
    }

    /** 设置配置参数 */
    @USConfigSet
    public static void setConfig(ServiceContext ctx, String json) throws Exception {
        Config config = Util.json2Type(json, Config.class);
        myName = config.name;   // 动态修改运行参数（如果配置参数要求微服务必须重启后才能生效，则不能立即修改运行值，只能保存配置文件）
        ctx.saveDataFile(CONFIG_FILE, config);  // 保存配置文件
    }
}
