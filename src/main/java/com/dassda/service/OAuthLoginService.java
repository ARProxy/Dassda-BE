package com.dassda.service;

import com.dassda.entity.Member;
import com.dassda.kakao.KakaoInfoResponse;
import com.dassda.oauth.OAuthInfoResponse;
import com.dassda.oauth.OAuthLoginParams;
import com.dassda.oauth.RequestOAuthInfoService;
import com.dassda.repository.MemberRepository;
import com.dassda.token.AuthTokens;
import com.dassda.token.AuthTokensGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final RestTemplate restTemplate;

    public void logout(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/logout";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer" +  accessToken);

        HttpEntity<?> request = new HttpEntity<>("", httpHeaders);
        restTemplate.postForObject(url, request, KakaoInfoResponse.class);
    }
    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        return authTokensGenerator.generate(memberId);
    }
    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }
    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        Member member = Member.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .gender(oAuthInfoResponse.getGender())
                .age_range(oAuthInfoResponse.getAge_range())
                .profile_image_url(oAuthInfoResponse.getProfile_image_url())
                .birthday(oAuthInfoResponse.getBirthday())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();
        return memberRepository.save(member).getId();
    }
}