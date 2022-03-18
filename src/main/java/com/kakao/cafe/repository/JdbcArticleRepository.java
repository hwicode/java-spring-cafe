package com.kakao.cafe.repository;

import com.kakao.cafe.controller.ArticleForm;
import com.kakao.cafe.domain.Article;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.*;

public class JdbcArticleRepository implements ArticleRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleDateFormat dateFormat;

    public JdbcArticleRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    @Override
    public synchronized Article save(ArticleForm articleForm) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("articles").usingGeneratedKeyColumns("id");

        String createdTime = dateFormat.format(new Date());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("writer", articleForm.getWriter());
        parameters.put("title", articleForm.getTitle());
        parameters.put("contents", articleForm.getContents());
        parameters.put("createdTime", createdTime);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return new Article(articleForm, key.intValue(), createdTime);
    }

    @Override
    public Optional<Article> findById(int id) {
        Article article = jdbcTemplate.queryForObject("select * from articles where id = ?", articlesRowMapper(), id);
        return Optional.ofNullable(article);
    }

    @Override
    public List<Article> findAll() {
        return jdbcTemplate.query("select * from articles", articlesRowMapper());
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from articles");
    }

    private RowMapper<Article> articlesRowMapper() {
        return (rs, rowNum) -> {
            ArticleForm articleForm = new ArticleForm(rs.getString("writer"),
                    rs.getString("title"),
                    rs.getString("contents"));

            return new Article(articleForm,
                    rs.getInt("id"),
                    rs.getString("createdTime"));
        };
    }
}
