package com.kakao.cafe.repository;

import com.kakao.cafe.controller.ArticleForm;
import com.kakao.cafe.domain.Article;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemoryArticleRepository implements ArticleRepository {

    private final List<Article> store = new CopyOnWriteArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public Article save(ArticleForm articleForm) {
        int articleId = store.size() + 1;
        String createdForm = dateFormat.format(new Date());
        Article article = new Article(articleForm, articleId, createdForm);
        store.add(article);
        return article;
    }

    @Override
    public Optional<Article> findById(int id) {
        return store.stream()
                .filter(article -> article.isCorrectId(id))
                .findAny();
    }

    @Override
    public List<Article> findAll() {
        return List.copyOf(store);
    }

    @Override
    public void deleteAll() {
        store.clear();
    }

}
