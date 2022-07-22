package com.tutti.backend.controller;

import com.tutti.backend.security.UserDetailsImpl;
import com.tutti.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequiredArgsConstructor
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/{id}",produces = "text/event-stream")
    public SseEmitter subscribe(
            @PathVariable String id,
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestHeader(value="lastEventId",required = false,defaultValue = "") String lastEventId
    ){

        SseEmitter sseEmitter=notificationService.subscribe(id,lastEventId);
        log.info("5");
        System.out.println(sseEmitter.toString());
        return sseEmitter;
    }
}
