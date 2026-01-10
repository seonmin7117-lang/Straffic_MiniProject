package com.example.straffic.oauth;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProviderId();

    String getProvider();

    String getEmail();

    String getName();

    String getMobile();
}

