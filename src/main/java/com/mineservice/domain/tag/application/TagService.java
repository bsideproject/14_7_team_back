package com.mineservice.domain.tag.application;

import com.mineservice.domain.article_tag.domain.ArticleTagEntity;
import com.mineservice.domain.article_tag.repository.ArticleTagRepository;
import com.mineservice.domain.tag.domain.TagEntity;
import com.mineservice.domain.tag.repository.TagRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        for(TagEntity tagEntity : tagEntityList) {
            Optional<ArticleTagEntity> optionalArticleTag = articleTagRepository.findByTagId(
                tagEntity.getId());
            if (optionalArticleTag.isEmpty()) {
                tagRepository.delete(tagEntity);
            }
        }
    }

}
