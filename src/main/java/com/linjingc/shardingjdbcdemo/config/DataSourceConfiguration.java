package com.linjingc.shardingjdbcdemo.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import io.shardingsphere.core.keygen.KeyGenerator;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * sharding-jdbc数据源配置
 *
 * @author cxc
 */
@Configuration
@AutoConfigureAfter(DataSource.class)
public class DataSourceConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public DataSource getDataSource() throws SQLException {
        return buildDataSource();
    }

    private DataSource buildDataSource() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>(4);
        // 添加两个数据库db0,db1到map里
        dataSourceMap.put("db0", createDataSource("db0"));
        dataSourceMap.put("db1", createDataSource("db1"));


        // 创建user表的分片规则
        TableRuleConfiguration userNodes = createDataNodes();

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        //添加分片规则
        shardingRuleConfig.getTableRuleConfigs().add(userNodes);


        //配置默认数据源 不分片的数据存入这里
        // 设置默认db为db0，也就是为那些没有配置分库分表策略的指定的默认库
        // 如果只有一个库，也就是不需要分库的话，map里只放一个映射就行了，只有一个库时不需要指定默认库，但2个及以上时必须指定默认库，否则那些没有配置策略的表将无法操作数据
        // *** 这里需要注意的是 两个库的表需要一样 不然排查的时候会找不到表 就无法使用默认数据源**
        shardingRuleConfig.setDefaultDataSourceName("db0");


        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), new Properties());

        return dataSource;
    }


    /**
     * 创建数据源
     *
     * @param dataSourceName
     * @return
     */
    private static DataSource createDataSource(final String dataSourceName) {
        // 使用druid连接数据库
        DruidDataSource result = new DruidDataSource();
        result.setDriverClassName("com.mysql.cj.jdbc.Driver");

        result.setUrl(String.format("jdbc:mysql://112.74.43.136:3306/%s", dataSourceName, "?useUnicode=true&amp;characterEncoding=utf8"));
        result.setUsername("root");
        result.setPassword("");
        return result;
    }

    /**
     * 配置分表逻辑的节点
     *
     * @return
     */
    private static TableRuleConfiguration createDataNodes() {
        // 配置user表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("t_user");
        orderTableRuleConfig.setActualDataNodes("db${0..1}.t_user${0..1}");

        // 配置分库策略
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "db${id % 2}"));

        //分表策略
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "t_user${id % 2}"));

        return orderTableRuleConfig;
    }


    @Bean
    public KeyGenerator keyGenerator() {
        return new DefaultKeyGenerator();
    }


    //private Properties getProperties() {
    //    Properties p = new Properties();
    //    p.put("minimum-idle", env.getProperty("hikari.minimum-idle"));
    //    p.put("maximum-pool-size", env.getProperty("hikari.maximum-pool-size"));
    //    p.put("auto-commit", env.getProperty("hikari.auto-commit"));
    //    p.put("idle-timeout", env.getProperty("hikari.idle-timeout"));
    //    p.put("max-lifetime", env.getProperty("hikari.max-lifetime"));
    //    p.put("connection-timeout", env.getProperty("hikari.connection-timeout"));
    //    p.put("connection-test-query", env.getProperty("hikari.connection-test-query"));
    //    return p;
    //}
}