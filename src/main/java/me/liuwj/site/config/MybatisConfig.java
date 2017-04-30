package me.liuwj.site.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by vince on 2017/3/26.
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan("com.huaying.poker.business")
public class MybatisConfig {

//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setTypeAliasesPackage("info.liuwenjun.site.model");
//        bean.setTypeHandlers(new TypeHandler[]{new InstantTypeHandler()});
//
//        // fix bug of 'could not resolve type alias', http://www.scienjus.com/mybatis-vfs-bug/
//        bean.setVfs(SpringBootVFS.class);
//
//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        bean.setMapperLocations(resolver.getResources("classpath:mybatis-mappings/**/*.xml"));
//
//        SqlSessionFactory factory = bean.getObject();
//        factory.getConfiguration().setMapUnderscoreToCamelCase(true);
//        return factory;
//    }
//
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
}
