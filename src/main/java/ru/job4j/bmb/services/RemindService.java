package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.List;

@Service
public class RemindService {

    private final SentContent sentContent;
    private final UserRepository userRepository;

    public RemindService(SentContent sentContent, UserRepository userRepository) {
        this.sentContent = sentContent;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRateString = "${remind.period}")
    public void ping() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Content content = new Content(user.getChatId()).setText("Ping");
            sentContent.sent(content);
        }
    }
}
