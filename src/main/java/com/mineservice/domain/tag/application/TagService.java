package com.mineservice.domain.tag.application;

import com.mineservice.domain.article_tag.domain.ArticleTag;
import com.mineservice.domain.article_tag.repository.ArticleTagRepository;
import com.mineservice.domain.tag.domain.Tag;
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
    public Tag createTagByArticle(String userId, String tagName) {
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
        return tag;
    }

    @Transactional
    public void deleteTagByUserId(String userId) {
        List<Tag> tagList = tagRepository.findAllByUserId(userId);

        for(Tag tag : tagList) {
            Optional<ArticleTag> optionalArticleTag = articleTagRepository.findByTagId(tag.getId());
            if (optionalArticleTag.isEmpty()) {
                tagRepository.delete(tag);
            }
        }
    }

}
