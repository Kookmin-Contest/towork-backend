package com.backend.towork.oauth2.service;

import com.backend.towork.member.domain.Member;
import com.backend.towork.member.domain.Role;
import com.backend.towork.member.repository.MemberRepository;
import com.backend.towork.oauth2.provider.OAuth2Provider;
import com.backend.towork.oauth2.userinfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocialAccountService {

    private final MemberRepository memberRepository;

    public Member updateSocialAccount(OAuth2UserInfo oAuth2UserInfo) {

        String email = oAuth2UserInfo.getEmail();
        OAuth2Provider provider = oAuth2UserInfo.getProvider();
        log.info(email);

        if (memberRepository.findByUsername(email).isEmpty()) {
            Member member = Member.builder()
                            .username(email)
                            .provider(provider)
                            .role(Role.USER)
                            .build();
            memberRepository.save(member);
            return member;
        }
        else {
            throw new IllegalStateException("동일한 이메일을 가진 아이디가 있습니다.");
        }
    }
}
