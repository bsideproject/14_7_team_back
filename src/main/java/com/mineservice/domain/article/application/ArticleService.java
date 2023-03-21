package com.mineservice.domain.article.application;

import com.mineservice.domain.article.domain.Article;
import com.mineservice.domain.article.dto.ArticleReqDTO;
import com.mineservice.domain.article.repository.ArticleRepository;
import com.mineservice.domain.article_tag.domain.ArticleTag;
import com.mineservice.domain.article_tag.domain.ArticleTagPK;
import com.mineservice.domain.article_tag.repository.ArticleTagRepository;
import com.mineservice.domain.tag.domain.Tag;
import com.mineservice.domain.tag.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;


    public void createArticle(String userId, ArticleReqDTO articleReqDTO) {
        Article article = Article.builder()
                .userId(userId)
                .title(articleReqDTO.getTitle())
                .type("type")
                .url(articleReqDTO.getUrl())
                .build();
        articleRepository.save(article);

        List<ArticleTag> articleTagList = new ArrayList<>();
        for (String tagName : articleReqDTO.getTags()) {
            Optional<Tag> findByName = tagRepository.findByUserIdAndName(userId, tagName);
            Tag tag;
            if (findByName.isEmpty()) {
                tag = Tag.builder()
                        .userId(userId)
                        .name(tagName)
                        .createBy(userId)
                        .build();
                tagRepository.save(tag);
            } else {
                tag = findByName.get();
            }

            articleTagList.add(ArticleTag.builder()
                    .article(article)
                    .tag(tag)
                    .build());
        }

        articleTagRepository.saveAll(articleTagList);

    }




}
