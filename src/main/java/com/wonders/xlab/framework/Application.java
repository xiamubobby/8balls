package com.wonders.xlab.framework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import com.wonders.xlab.framework.repository.MyRepositoryImpl;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = {"${app.basePackages}"},
        repositoryBaseClass = MyRepositoryImpl.class
)
@EnableScheduling
@EntityScan({"${app.basePackages}"})
@ComponentScan({"${app.basePackages}"})
public class Application extends SpringBootServletInitializer {

    @Value("${jackson.indent.output}")
    private boolean jacksonIndentOutput = false;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE");
            }
        };
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {

        Hibernate4Module hibernateModule = new Hibernate4Module();
        hibernateModule.disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION);

        JodaModule jodaModule = new JodaModule();
        jodaModule.addSerializer(DateTime.class, new DateTimeSerializer(new JacksonJodaDateFormat(
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC())));

        return Jackson2ObjectMapperBuilder.json()
                .modules(hibernateModule, jodaModule)
                .simpleDateFormat("yyyy-MM-dd HH:mm:ss").timeZone("GMT+8")
                .indentOutput(jacksonIndentOutput)
                .serializationInclusion(JsonInclude.Include.NON_NULL);
    }

//    @Bean
//    public Integer port() {
//        return SocketUtils.findAvailableTcpPort();
//    }
//
//    @Bean
//    public EmbeddedServletContainerFactory servletContainer() {
//        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
//        tomcat.addAdditionalTomcatConnectors(createSslConnector());
//        return tomcat;
//    }
//
//    private Connector createSslConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setPort(port());
//        return connector;
//    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.profiles(new String[]{"production"}).sources(new Class[]{Application.class});
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
