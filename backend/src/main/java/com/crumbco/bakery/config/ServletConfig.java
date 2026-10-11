package com.crumbco.bakery.config;

import com.crumbco.bakery.dao.JdbcProductDao;
import com.crumbco.bakery.servlet.LegacyBakeryServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {
    @Bean
    public ServletRegistrationBean<LegacyBakeryServlet> legacyBakeryServlet(JdbcProductDao dao, ObjectMapper mapper) {
        ServletRegistrationBean<LegacyBakeryServlet> registration =
                new ServletRegistrationBean<>(new LegacyBakeryServlet(dao, mapper), "/legacy/catalog");
        registration.setName("legacyBakeryCatalogServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }
}
