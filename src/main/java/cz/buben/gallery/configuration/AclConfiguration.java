package cz.buben.gallery.configuration;

import net.sf.ehcache.Ehcache;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import javax.sql.DataSource;

@SuppressWarnings("WeakerAccess")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AclConfiguration extends GlobalMethodSecurityConfiguration {

    @Bean
    public MutableAclService aclService(DataSource dataSource) {
        return new JdbcMutableAclService(
                dataSource,
                this.lookupStrategy(dataSource),
                this.aclCache());
    }

    @Bean
    public LookupStrategy lookupStrategy(DataSource dataSource) {
        return new BasicLookupStrategy(
                dataSource,
                this.aclCache(),
                this.aclAuthorizationStrategy(),
                new ConsoleAuditLogger()
        );
    }

    @Bean
    public AclCache aclCache() {
        return new EhCacheBasedAclCache(
                this.aclEhCacheFactoryBean().getObject(),
                this.permissionGrantingStrategy(),
                this.aclAuthorizationStrategy()
        );
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(() -> "read");
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean() {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(this.aclCacheManager().getObject());
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public EhCacheManagerFactoryBean aclCacheManager() {
        return new EhCacheManagerFactoryBean();
    }
}
