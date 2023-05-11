package com.mineservice.domain.tag.application;

import com.mineservice.domain.article_tag.domain.ArticleTagEntity;
import com.mineservice.domain.article_tag.repository.ArticleTagRepository;
import com.mineservice.domain.tag.domain.TagEntity;
import com.mineservice.domain.tag.dto.TagResDTO;
import com.mineservice.domain.tag.repository.TagRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

    @Transactional
    public TagEntity createTagByArticle(String userId, String tagName) {
        Optional<TagEntity> findByName = tagRepository.findByUserIdAndName(userId, tagName);
        TagEntity tagEntity;
        if (findByName.isEmpty()) {
            tagEntity = TagEntity.builder()
                    .userId(userId)
                    .name(tagName)
                    .createBy(userId)
                    .build();
            tagRepository.save(tagEntity);
        } else {
            tagEntity = findByName.get();
        }
        return tagEntity;
    }

    @Transactional
    public void deleteTagByUserId(String userId) {
        List<TagEntity> tagEntityList = tagRepository.findAllByUserId(userId);

        for (TagEntity tagEntity : tagEntityList) {
            List<ArticleTagEntity> articleTagList = articleTagRepository.findAllByTagId(tagEntity.getId());
            if (articleTagList.isEmpty()) {
                tagRepository.delete(tagEntity);
            }
        }
    }

    public List<String> findAllTagNameByArticleId(Long articleId) {
        List<TagEntity> tagList = tagRepository.findAllByArticleId(articleId);
        return tagList.stream().map(TagEntity::getName).collect(Collectors.toList());
    }

    public List<String> findAllTagNameByUserId(String userId) {
        List<TagEntity> tagEntityList = tagRepository.findAllByUserId(userId);
        return tagEntityList.stream().map(TagEntity::getName).collect(Collectors.toList());
    }

    public TagResDTO findAllTagName(Long articleId, String userId) {
        List<String> allTagNameByArticleId = new ArrayList<>();
        if (articleId != null) {
            allTagNameByArticleId = findAllTagNameByArticleId(articleId);
        }
        List<String> allTagNameByUserId = findAllTagNameByUserId(userId);

        return TagResDTO.builder()
                .tags(allTagNameByUserId)
                .selectedTags(allTagNameByArticleId)
                .build();
    }

}
