package com.mineservice.domain.article.application;

import com.mineservice.domain.article.domain.ArticleAlarm;
import com.mineservice.domain.article.domain.ArticleEntity;
import com.mineservice.domain.article.dto.ArticleDTO;
import com.mineservice.domain.article.dto.ArticleReqDTO;
import com.mineservice.domain.article.dto.ArticleResDTO;
import com.mineservice.domain.article.repository.ArticleAlarmRepository;
import com.mineservice.domain.article.repository.ArticleRepository;
import com.mineservice.domain.article_tag.domain.ArticleTagEntity;
import com.mineservice.domain.article_tag.repository.ArticleTagRepository;
import com.mineservice.domain.file_info.application.FileInfoService;
import com.mineservice.domain.tag.application.TagService;
import com.mineservice.domain.tag.domain.TagEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagService tagService;
    private final FileInfoService fileInfoService;
    private final ArticleTagRepository articleTagRepository;
    private final ArticleAlarmRepository articleAlarmRepository;

    @Transactional
    public void createArticle(String userId, ArticleReqDTO articleReqDTO) {
        String articleType = getArticleType(articleReqDTO.getUrl());
        if ("image".equals(articleType)) {
            String title = "MINE_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
            List<ArticleEntity> sameTitleArticleListEntity = articleRepository.findArticlesByUserIdAndTitleStartingWith(userId, title);
            if (sameTitleArticleListEntity.isEmpty()) {
                articleReqDTO.setTitle(title);
            } else {
                articleReqDTO.setTitle(title + "(" + sameTitleArticleListEntity.size() + ")");
            }
        } else {
            articleRepository.findArticleByUrl(articleReqDTO.getUrl())
                    .ifPresent(article -> {
                        throw new IllegalArgumentException("이미 등록된 링크입니다.");
                    });
        }
        log.info("articleTitle {}", articleReqDTO.getTitle());

        ArticleEntity articleEntity = ArticleEntity.builder()
                .userId(userId)
                .title(articleReqDTO.getTitle())
                .type(articleType)
                .url(articleReqDTO.getUrl())
                .favorite(articleReqDTO.isFavorite() ? "Y": "N")
                .build();
        articleRepository.save(articleEntity);
        log.info("article {}", articleEntity.toString());

        List<ArticleTagEntity> articleTagEntityList = new ArrayList<>();
        for (String tagName : articleReqDTO.getTags()) {
            TagEntity tagEntity = tagService.createTagByArticle(userId, tagName);
            articleTagEntityList.add(ArticleTagEntity.builder()
                    .article(articleEntity)
                    .tag(tagEntity)
                    .build());
            log.info("tag {}", tagEntity.toString());
        }

        if (articleReqDTO.getImg() != null) {
            fileInfoService.createFileInfo(userId, articleEntity.getId(), articleReqDTO.getImg());
        }

        if (articleReqDTO.getAlarmTime() != null) {
            createArticleAlarm(articleEntity.getId(), articleReqDTO.getAlarmTime());
        }

        articleTagRepository.saveAll(articleTagEntityList);
    }

    @Transactional
    public void createArticleAlarm(Long articleId, LocalDateTime alarmTime) {
        ArticleAlarm articleAlarm = ArticleAlarm.builder()
                .articleId(articleId)
                .time(alarmTime)
                .build();
        articleAlarmRepository.save(articleAlarm);
        log.info("articleAlarm {}", articleAlarm.toString());
    }

    public ArticleResDTO findAllBySearch(String title, boolean favorite, boolean readYn, List<String> types, List<String> tags, String userId, Pageable pageable) {
        String favoriteStr = favorite ? "Y" : "N";
        String readYnStr = readYn ? "Y" : "N";

        Page<ArticleEntity> allBySearch = articleRepository.findAllBySearch(title, favoriteStr, readYnStr, types, tags, userId, pageable);
        Page<ArticleDTO> articleDTOPage = allBySearch.map(this::toDTO);


        return ArticleResDTO.builder()
                .totalArticleSize(articleDTOPage.getTotalElements())
                .totalPageSize(articleDTOPage.getTotalPages())
                .articleList(articleDTOPage.getContent())
                .build();
    }

    private ArticleDTO toDTO(ArticleEntity articleEntity) {
        return ArticleDTO.builder()
                .articleId(articleEntity.getId())
                .title(articleEntity.getTitle())
                .type(articleEntity.getType())
                .favorite("Y".equals(articleEntity.getFavorite()))
                .read("Y".equals(articleEntity.getReadYn()))
                .alarm(articleAlarmRepository.findOneByArticleId(articleEntity.getId()).isPresent())
                .thumbUrl("localhost:8080/image/thumb/" + articleEntity.getId())
                .tagNames(tagService.findAllTagNameByArticleId(articleEntity.getId()))
                .build();
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        fileInfoService.deleteFileInfo(articleId);
        articleTagRepository.deleteByArticleId(articleId);
        articleAlarmRepository.deleteByArticleId(articleId);
        articleRepository.deleteById(articleId);
    }

    private String getArticleType(String url) {
        if (url == null) {
            return "image";
        } else if (url.contains("youtube")) {
            return "youtube";
        } else {
            return "article";
        }
    }

}
