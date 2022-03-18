package com.kakao.cafe.domain;

import com.kakao.cafe.controller.ArticleForm;

import java.util.Objects;

public class Article {

    private final String writer;
    private final String title;
    private final String contents;
    private final int articleId;
    private final String createdTime;

    public Article(ArticleForm articleForm, int articleId, String createdTime) {
        this.writer = articleForm.getWriter();
        this.title = articleForm.getTitle();
        this.contents = articleForm.getContents();
        this.articleId = articleId;
        this.createdTime = createdTime;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public int getArticleId() {
        return articleId;
    }

    public boolean isCorrectId(int id) {
        if (articleId == id) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return articleId == article.articleId
                && Objects.equals(writer, article.writer)
                && Objects.equals(title, article.title)
                && Objects.equals(contents, article.contents)
                && Objects.equals(createdTime, article.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(writer, title, contents, articleId, createdTime);
    }
}
