package com.kakao.cafe.domain;

import com.kakao.cafe.controller.ArticleForm;

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

    public boolean isCorrectId(int id) {
        if (articleId == id) {
            return true;
        }
        return false;
    }
}
