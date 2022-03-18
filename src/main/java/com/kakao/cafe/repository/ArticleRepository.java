package com.kakao.cafe.repository;

import com.kakao.cafe.controller.ArticleForm;
import com.kakao.cafe.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Article save(ArticleForm articleForm);

    Optional<Article> findById(int id);

    List<Article> findAll();

    void deleteAll();
}
