package com.mineservice.domain.push.api;

import com.mineservice.domain.push.application.PushNotiService;
import com.mineservice.domain.push.dto.PushNotiResDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class PushNotiController {

    private final PushNotiService pushNotiService;

    @GetMapping("/push-noti")
    @ApiOperation(value = "알림 목록 불러오기")
    public ResponseEntity<List<PushNotiResDTO>> getNotiList(@ApiIgnore @AuthenticationPrincipal UserDetails user) {
        String userId = user.getUsername();

        List<PushNotiResDTO> notiList = pushNotiService.findAllByUserId(userId);

        return ResponseEntity.ok(notiList);
    }


}
