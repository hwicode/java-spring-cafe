package com.kakao.cafe;

import com.kakao.cafe.repository.ArticleRepository;
import com.kakao.cafe.repository.JdbcUserRepository;
import com.kakao.cafe.repository.MemoryArticleRepository;
import com.kakao.cafe.repository.UserRepository;
import com.kakao.cafe.service.ArticleService;
import com.kakao.cafe.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private final DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }

    @Bean
    public UserRepository userRepository() {
        return new JdbcUserRepository(dataSource);
    }

    @Bean
    public ArticleService articleService() {
        return new ArticleService(articleRepository());
    }

    @Bean
    public ArticleRepository articleRepository() {
        return new MemoryArticleRepository();
    }
}
