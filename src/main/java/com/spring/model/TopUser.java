package com.spring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TopUser {
    private String profilImageID;
    private String userImageUrl;
    private String userName;
    private String coverImage;
    private long userID;
    private int x;
}
