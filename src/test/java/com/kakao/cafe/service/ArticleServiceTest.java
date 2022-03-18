package com.kakao.cafe.service;

import com.kakao.cafe.controller.ArticleForm;
import com.kakao.cafe.domain.Article;
import com.kakao.cafe.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
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
        Article article1 = createArticle(articleForm1, 1);

        given(articleRepository.save(articleForm1))
                .willReturn(article1);

        //when
        Article newArticle = articleService.write(articleForm1);

        //then
        assertThat(article1).isEqualTo(newArticle);
    }

    @Test
    void findArticles() {
        //given
        Article article1 = createArticle(createArticleForm(1), 1);
        Article article2 = createArticle(createArticleForm(2), 2);
        Article article3 = createArticle(createArticleForm(3), 3);

        given(articleRepository.findAll())
                .willReturn(List.of(article1, article2, article3));

        //when
        List<Article> articles = articleService.findArticles();

        //then
        assertThat(articles).contains(article1, article2, article3);
    }

    @Test
    void findArticle() {
        //given
        int id = 1;
        Article article = createArticle(createArticleForm(1), id);

        given(articleRepository.findById(id))
                .willReturn(Optional.of(article));

        //when
        Article savedArticle = articleService.findArticle(id);

        //then
        assertThat(article).isEqualTo(savedArticle);
    }

    @Test
    void findNonexistentArticle() {
        //given
        int nonexistentArticleId = 1;

        given(articleRepository.findById(nonexistentArticleId))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> articleService.findArticle(nonexistentArticleId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("존재하지 않는 게시물입니다.");
    }
}
