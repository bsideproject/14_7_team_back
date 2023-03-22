package com.mineservice.domain.article.application;

import com.mineservice.domain.article.domain.Article;
import com.mineservice.domain.article.dto.ArticleDTO;
import com.mineservice.domain.article.dto.ArticleReqDTO;
import com.mineservice.domain.article.dto.ArticleResDTO;
import com.mineservice.domain.article.repository.ArticleRepository;
import com.mineservice.domain.article_tag.domain.ArticleTag;
import com.mineservice.domain.article_tag.repository.ArticleTagRepository;
import com.mineservice.domain.tag.domain.Tag;
import com.mineservice.domain.tag.repository.TagRepository;
import com.mineservice.domain.user.domain.UserInfo;
import com.mineservice.domain.user.repository.UserInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

    private final UserInfoRepository userInfoRepository;

    @Transactional
    public UserInfo createUserInfo(String userId) {
        return userInfoRepository.save(UserInfo.builder()
                .id(userId)
                .build());
    }

    @Transactional
    public void createArticle(String userId, ArticleReqDTO articleReqDTO) {
        Article article = Article.builder()
                .userId(userId)
                .title(articleReqDTO.getTitle())
                .type(getType(articleReqDTO.getUrl()))
                .url(articleReqDTO.getUrl())
                .build();
        articleRepository.save(article);
        log.info("article {}", article.toString());

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

        // file upload

        // if alarm is on, create alarm

        articleTagRepository.saveAll(articleTagList);
    }

    public ArticleResDTO findAllArticlesByUserId(String userId, Pageable pageable) {
        Page<Article> findByUserId = articleRepository.findAllByUserId(userId, pageable);
        Page<ArticleDTO> articleDTOPage = findByUserId.map(this::toDTO);

        return ArticleResDTO.builder()
                .totalArticleSize(articleDTOPage.getTotalElements())
                .totalPageSize(articleDTOPage.getTotalPages())
                .articleList(articleDTOPage.getContent())
                .build();
    }

    public void deleteArticle(Long articleId) {
        articleTagRepository.deleteByArticleId(articleId);
        articleRepository.deleteById(articleId);
    }


    private ArticleDTO toDTO(Article article) {
        return ArticleDTO.builder()
                .articleId(article.getId())
                .type(article.getType())
                .title(article.getTitle())
                .build();
    }

    private String getType(String url) {
        if (url == null) {
            return "image";
        } else if (url.contains("youtube")) {
            return "youtube";
        } else {
            return "link";
        }
    }

}
