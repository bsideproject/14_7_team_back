package com.mineservice.domain.push.application;

import com.mineservice.domain.article.domain.ArticleEntity;
import com.mineservice.domain.article.repository.ArticleRepository;
import com.mineservice.domain.push.domain.PushNoti;
import com.mineservice.domain.push.dto.PushNotiResDTO;
import com.mineservice.domain.push.repository.PushNotiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotiService {

    private final ArticleRepository articleRepository;
    private final PushNotiRepository pushNotiRepository;


    public List<PushNotiResDTO> findAllByUserId(String userId) {
        List<PushNoti> notiList = pushNotiRepository.findAllByUserIdOrderByIdDesc(userId);
        notiList = notiList.stream()
                .filter(noti -> noti.getCreatedDate().isAfter(LocalDate.now().minusDays(30)))
                .collect(Collectors.toList());

        List<PushNotiResDTO> dtoList = notiList.stream()
                .map(noti -> toDTO(noti, userId))
                .filter(dto -> dto.getArticleUrl() != null)
                .collect(Collectors.toList());

        notiList.forEach(noti -> noti.setReadYn("Y"));
        pushNotiRepository.saveAll(notiList);

        return dtoList;
    }

    private PushNotiResDTO toDTO(PushNoti pushNoti, String userId) {
        Optional<ArticleEntity> optionalArticle = articleRepository.findArticleByIdAndUserId(pushNoti.getArticleId(), userId);
        if (optionalArticle.isEmpty()) {
            return PushNotiResDTO.builder().build();
        }
        ArticleEntity article = optionalArticle.get();
        return PushNotiResDTO.builder()
                .type(article.getType())
                .title(article.getTitle())
                .notiDate(localDate2Str(pushNoti.getCreatedDate()))
                .articleId(article.getId())
                .articleUrl("https://mine.directory/api/v1/articles/" + article.getId())
                .build();
    }

    private String localDate2Str(LocalDate refDate) {
        long between = ChronoUnit.DAYS.between(refDate,  LocalDate.now());
        switch ((int) between) {
            case 0:
                return "오늘";
            case 1:
                return "어제";
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return between + "일전";
            case 7:
                return "일주일전";
            default:
                return refDate.format(DateTimeFormatter.ofPattern("M월 d일"));
        }
    }

}
