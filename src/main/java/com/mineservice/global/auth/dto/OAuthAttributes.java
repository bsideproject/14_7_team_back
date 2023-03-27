package com.mineservice.global.auth.dto;

import com.mineservice.domain.user.Role;
import com.mineservice.domain.user.User;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;
    private final String gender;
    private final String age;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        return ofNaver("id", attributes);
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
            .name((String) response.get("name"))
            .email((String) response.get("email"))
            .attributes(response)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    public User toEntity(){
        return User.builder()
            .name(name)
            .email(email)
            .role(Role.GUEST)
            .build();
    }

}
