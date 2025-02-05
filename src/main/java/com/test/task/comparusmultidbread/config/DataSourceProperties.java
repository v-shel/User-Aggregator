package com.test.task.comparusmultidbread.config;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceProperties {

    private String url;
    private String user;
    private String password;
    private String table;
    private Map<String, String> mapping;
}
