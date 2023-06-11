package com.mineservice.domain.article.application;

import com.mineservice.domain.article.domain.ArticleAlarm;
import com.mineservice.domain.article.domain.ArticleEntity;
import com.mineservice.domain.article.dto.*;
import com.mineservice.domain.article.repository.ArticleAlarmRepository;
import com.mineservice.domain.article.repository.ArticleRepository;
import com.mineservice.domain.article_tag.domain.ArticleTagEntity;
import com.mineservice.domain.article_tag.repository.ArticleTagRepository;
import com.mineservice.domain.file_info.application.FileInfoService;
import com.mineservice.domain.tag.application.TagService;
import com.mineservice.domain.tag.domain.TagEntity;
import com.mineservice.global.exception.CustomException;
import com.mineservice.global.exception.ErrorCode;
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
import java.util.Optional;

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
            if (articleReqDTO.getTitle() == null) {
                String title = "MINE_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
                List<ArticleEntity> sameTitleArticleListEntity = articleRepository.findArticlesByUserIdAndTitleStartingWith(userId, title);
                if (sameTitleArticleListEntity.isEmpty()) {
                    articleReqDTO.setTitle(title);
                } else {
                    articleReqDTO.setTitle(title + "(" + sameTitleArticleListEntity.size() + ")");
                }
            }
        } else {
            articleRepository.findArticleByUrlAndUserId(articleReqDTO.getUrl(), userId)
                    .ifPresent(article -> {
                        log.error("이미 등록된 링크입니다.");
                        throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
                    });
        }
        log.info("articleTitle {}", articleReqDTO.getTitle());

        ArticleEntity articleEntity = ArticleEntity.builder()
                .userId(userId)
                .title(articleReqDTO.getTitle())
                .type(articleType)
                .url(articleReqDTO.getUrl())
                .favorite(articleReqDTO.isFavorite() ? "Y" : "N")
                .createBy(userId)
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
        String favoriteStr = favorite ? "Y" : null;
        String readYnStr = readYn ? "N" : null;

        Page<ArticleEntity> allBySearch = articleRepository.findAllBySearch(title, favoriteStr, readYnStr, types, tags, userId, pageable);
        Page<ArticleDTO> articleDTOPage = allBySearch.map(this::toDTO);


        return ArticleResDTO.builder()
                .totalArticleSize(articleDTOPage.getTotalElements())
                .totalPageSize(articleDTOPage.getTotalPages())
                .articleList(articleDTOPage.getContent())
                .build();
    }

    public ArticleDTO findArticleById(Long articleId, String userId) {
        Optional<ArticleEntity> optionalArticle = articleRepository.findArticleByIdAndUserId(articleId, userId);
        if (optionalArticle.isEmpty()) {
            log.error("해당하는 아티클이 존재하지 않습니다 [articleId: {}, userId : {}]", articleId, userId);
            throw new CustomException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        ArticleEntity articleEntity = optionalArticle.get();
        if ("N".equals(articleEntity.getUseYn())) {
            log.error("해당하는 아티클은 삭제되었습니다 [articleId: {}, userId : {}]", articleId, userId);
            throw new CustomException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        log.info("articleEntity : {}", articleEntity.toString());
        return toDTO(articleEntity);
    }

    private ArticleDTO toDTO(ArticleEntity articleEntity) {
        String articleType = articleEntity.getType();
        if ("image".equals(articleType)) {
            return ArticleDTO.builder()
                    .articleId(articleEntity.getId())
                    .title(articleEntity.getTitle())
                    .type(articleType)
                    .favorite("Y".equals(articleEntity.getFavorite()))
                    .read("Y".equals(articleEntity.getReadYn()))
                    .alarm(articleEntity.getArticleAlarm() != null)
                    .alarmTime(articleEntity.getArticleAlarm() == null ? null : articleEntity.getArticleAlarm().getTime())
                    .thumbUrl("https://mine.directory/image/thumb/" + articleEntity.getId())
                    .tagNames(tagService.findAllTagNameByArticleId(articleEntity.getId()))
                    .build();
        } else {
            return ArticleDTO.builder()
                    .articleId(articleEntity.getId())
                    .title(articleEntity.getTitle())
                    .type(articleType)
                    .favorite("Y".equals(articleEntity.getFavorite()))
                    .read("Y".equals(articleEntity.getReadYn()))
                    .alarm(articleEntity.getArticleAlarm() != null)
                    .alarmTime(articleEntity.getArticleAlarm() == null ? null : articleEntity.getArticleAlarm().getTime())
                    .url(articleEntity.getUrl())
                    .tagNames(tagService.findAllTagNameByArticleId(articleEntity.getId()))
                    .build();
        }
    }

    @Transactional
    public void modifyArticle(ArticleModDTO dto, String userId) {
        Long articleId = dto.getArticleId();
        String title = dto.getTitle();
        Boolean favorite = dto.getFavorite();
        Boolean read = dto.getRead();
        Boolean alarm = dto.getAlarm();
        LocalDateTime alarmTime = dto.getAlarmTime();
        List<String> tags = dto.getTags();

        Optional<ArticleEntity> optionalArticle = articleRepository.findArticleByIdAndUserId(articleId, userId);
        if (optionalArticle.isEmpty()) {
            log.error("해당하는 아티클이 존재하지 않습니다 [articleId: {}, userId : {}]", articleId, userId);
            throw new CustomException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        ArticleEntity articleEntity = optionalArticle.get();

        if (title != null) {
            articleEntity.setTitle(title);
        }

        if (favorite != null) {
            if (Boolean.TRUE.equals(favorite)) {
                articleEntity.setFavorite("Y");
            } else {
                articleEntity.setFavorite("N");
            }
        }

        if (read != null) {
            if (Boolean.TRUE.equals(read)) {
                articleEntity.setReadYn("Y");
            } else {
                articleEntity.setReadYn("N");
            }
        }

        if (alarm != null) {
            ArticleAlarm articleAlarm = articleEntity.getArticleAlarm();
            if (Boolean.TRUE.equals(alarm)) {
                if (articleAlarm != null) {
                    articleAlarmRepository.save(ArticleAlarm.builder()
                            .articleId(articleId)
                            .time(alarmTime.withNano(0))
                            .build());
                } else {
                    articleAlarmRepository.save(ArticleAlarm.builder()
                            .articleId(articleId)
                            .time(alarmTime.withNano(0))
                            .createBy(userId)
                            .createDt(LocalDateTime.now())
                            .build());
                }
            } else {
                articleAlarmRepository.delete(articleAlarm);
            }
        }

        if (tags != null && !tags.isEmpty()) {
            articleTagRepository.deleteByArticleId(articleId);
            List<ArticleTagEntity> articleTagEntityList = new ArrayList<>();
            for (String tagName : tags) {
                TagEntity tagEntity = tagService.createTagByArticle(userId, tagName);
                articleTagEntityList.add(ArticleTagEntity.builder()
                        .article(articleEntity)
                        .tag(tagEntity)
                        .build());
                log.info("tag {}", tagEntity.toString());
            }
            articleTagRepository.saveAll(articleTagEntityList);
        } else {
            articleTagRepository.deleteByArticleId(articleId);
        }

        articleEntity.setModifyBy(userId);
        articleEntity.setModifyDt(LocalDateTime.now());
        articleRepository.save(articleEntity);
    }

    @Transactional
    public void modifyArticleRead(ArticleModReadDTO dto, String userId) {
        Long articleId = dto.getArticleId();
        Boolean read = dto.getRead();

        Optional<ArticleEntity> optionalArticle = articleRepository.findArticleByIdAndUserId(articleId, userId);
        if (optionalArticle.isEmpty()) {
            log.error("해당하는 아티클이 존재하지 않습니다 [articleId: {}, userId : {}]", articleId, userId);
            throw new CustomException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        ArticleEntity articleEntity = optionalArticle.get();

        if (Boolean.TRUE.equals(read)) {
            articleEntity.setReadYn("Y");
        } else {
            articleEntity.setReadYn("N");
        }

        articleEntity.setModifyBy(userId);
        articleEntity.setModifyDt(LocalDateTime.now());
        articleRepository.save(articleEntity);
    }

    @Transactional
    public void modifyArticleFavorite(ArticleModFavoriteDTO dto, String userId) {
        Long articleId = dto.getArticleId();
        Boolean favorite = dto.getFavorite();

        Optional<ArticleEntity> optionalArticle = articleRepository.findArticleByIdAndUserId(articleId, userId);
        if (optionalArticle.isEmpty()) {
            log.error("해당하는 아티클이 존재하지 않습니다 [articleId: {}, userId : {}]", articleId, userId);
            throw new CustomException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        ArticleEntity articleEntity = optionalArticle.get();

        if (Boolean.TRUE.equals(favorite)) {
            articleEntity.setFavorite("Y");
        } else {
            articleEntity.setFavorite("N");
        }
        articleEntity.setModifyBy(userId);
        articleEntity.setModifyDt(LocalDateTime.now());
        articleRepository.save(articleEntity);
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        fileInfoService.deleteFileInfo(articleId);
        articleTagRepository.deleteByArticleId(articleId);
        articleAlarmRepository.deleteByArticleId(articleId);
        articleRepository.deleteById(articleId);
    }


    public void deleteAllArticleByUserId(String userId) {
        List<ArticleEntity> articleEntityList = articleRepository.findAllByUserId(userId);
        for (ArticleEntity article : articleEntityList) {
            Long articleId = article.getId();
            fileInfoService.deleteFileInfo(articleId);
            articleTagRepository.deleteByArticleId(articleId);
            articleAlarmRepository.deleteByArticleId(articleId);
            articleRepository.deleteById(articleId);
        }
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
