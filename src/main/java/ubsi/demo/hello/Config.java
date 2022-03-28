package ubsi.demo.hello;

/** 微服务的配置 */
public class Config {
    public String name;                          // 配置项的当前运行值
    public String name_restart;                  // 配置项的配置值（在配置文件中的值，非当前值）
                                                 // 如果配置项修改后须重启才能生效，则通过???_restart返回
    public String name_comment = "服务实例的名字"; // 配置项的说明，通过???_comment返回
}
