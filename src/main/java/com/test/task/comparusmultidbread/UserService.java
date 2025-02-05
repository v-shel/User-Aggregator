package com.test.task.comparusmultidbread;

import com.test.task.comparusmultidbread.model.entity.User;
import com.test.task.comparusmultidbread.persistance.UserTemplate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserTemplate userTemplate;

    public List<User> getFromAllSources() {
        return userTemplate.getUsersFromEachDatasource();
    }
}
