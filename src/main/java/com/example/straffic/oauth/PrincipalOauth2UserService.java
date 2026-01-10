package com.example.straffic.oauth;

import com.example.straffic.member.entity.MemberEntity;
import com.example.straffic.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getClientRegistration:" + userRequest.getClientRegistration());
        log.info("getAccessToken:" + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes:" + oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.info("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            log.info("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            log.info("구글/네이버/카카오만 지원합니다..");
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("임시비밀번호");
        String role = "ROLE_USER";

        MemberEntity memberEntity = memberRepository.findOneById(username);

        if (memberEntity == null) {
            log.info("OAuth2 로그인이 최초입니다");
            memberEntity = new MemberEntity();
            memberEntity.setId(username);
            memberEntity.setPw(password);
            memberEntity.setName(oAuth2UserInfo.getName());
            memberEntity.setTel(oAuth2UserInfo.getMobile());
            memberEntity.setRole(role);
            memberEntity.setProvider(provider);
            memberEntity.setProviderId(providerId);
            memberRepository.save(memberEntity);
        } else {
            log.info("로그인을 이미 한 적이 있습니다. 자동 회원가입이 되어 있습니다.");
        }

        return new CustomOAuth2User(oAuth2User, memberEntity);
    }

    public static class CustomOAuth2User implements OAuth2User {
        private final OAuth2User delegate;
        private final MemberEntity memberEntity;
        private final Collection<? extends GrantedAuthority> authorities;

        public CustomOAuth2User(OAuth2User delegate, MemberEntity memberEntity) {
            this.delegate = delegate;
            this.memberEntity = memberEntity;
            this.authorities = Collections.singleton(new SimpleGrantedAuthority(memberEntity.getRole() != null ? memberEntity.getRole() : "ROLE_USER"));
        }

        public String getProvider() {
            return memberEntity.getProvider();
        }
        
        public String getRealName() {
            return memberEntity.getName();
        }

        @Override
        public Map<String, Object> getAttributes() {
            return delegate.getAttributes();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getName() {
            return memberEntity.getId();
        }
    }
}
