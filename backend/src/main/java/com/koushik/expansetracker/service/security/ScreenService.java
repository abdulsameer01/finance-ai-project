package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.dto.CreateScreenRequest;
import com.koushik.expansetracker.entity.security.Screen;
import com.koushik.expansetracker.repository.security.ScreenRepository;
import org.springframework.stereotype.Service;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;

    public ScreenService(ScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    public Screen createScreen(CreateScreenRequest request) {

        Screen screen = Screen.builder()
                .screenName(request.getScreenName())
                .screenRoute(request.getScreenRoute())
                .isMenu(request.isMenu())
                .build();

        return screenRepository.save(screen);
    }
}
