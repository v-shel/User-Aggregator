package com.test.task.comparusmultidbread.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private final String id;
    private final String username;
    private final String name;
    private final String surname;
}
