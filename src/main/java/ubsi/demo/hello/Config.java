package ubsi.demo.hello;

/** 微服务的配置 */
public class Config {
    public String name;                             // 配置项
    public String name_restart;                     // 如果配置项修改后必须重启微服务才能生效，则@USConfigGet时通过???_restart返回
                                                    // 配置项在配置文件中的值（非运行时的参数值）
    public String name_comment = "服务实例的名字";    // "name"配置项的说明
}
