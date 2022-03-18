package com.kakao.cafe.service;

import com.kakao.cafe.controller.ArticleForm;
import com.kakao.cafe.domain.Article;
import com.kakao.cafe.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ArticleServiceIntegrationTest {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;

    ArticleForm createArticleForm(int i) {
        return new ArticleForm(
                "writer" + i,
                "title" + i,
                "contents" + i
        );
    }

    Article createArticle(ArticleForm articleForm, int id) {
        return new Article(
                articleForm,
                id,
                "1111-11-11 11:11"
        );
    }

    @Test
    void write() {
        //given
        ArticleForm articleForm1 = createArticleForm(1);

        //when
        Article newArticle = articleService.write(articleForm1);
        Article article = new Article(articleForm1, newArticle.getArticleId(), newArticle.getCreatedTime());

        //then
        Article findArticle = articleService.findArticle(newArticle.getArticleId());
        assertThat(article).isEqualTo(findArticle);
    }

    @Test
    void findArticles() {
        //given
        ArticleForm articleForm1 = createArticleForm(1);
        ArticleForm articleForm2 = createArticleForm(2);
        ArticleForm articleForm3 = createArticleForm(3);

        Article article1 = articleService.write(articleForm1);
        Article article2 = articleService.write(articleForm2);
        Article article3 = articleService.write(articleForm3);

        //when
        List<Article> articles = articleService.findArticles();

        //then
        assertThat(articles).contains(article1, article2, article3);
    }

    @Test
    void findArticle() {
        //given
        ArticleForm articleForm = createArticleForm(1);
        Article article = articleService.write(articleForm);

        //when
        Article savedArticle = articleService.findArticle(article.getArticleId());

        //then
        assertThat(article).isEqualTo(savedArticle);
    }
}
