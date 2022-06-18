package dev.qhc99.gallery_server.services;


import dev.qhc99.gallery_server.data_class.AuthProvider;
import dev.qhc99.gallery_server.data_class.DBUser;
import dev.qhc99.gallery_server.data_class.GithubOAuth2UserInfo;
import dev.qhc99.gallery_server.data_class.OAuth2UserInfo;

import dev.qhc99.gallery_server.exceptions.OAuth2AuthenticationProcessingException;
import dev.qhc99.gallery_server.repos.DBUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Service
public class OAuth2DBUserService extends DefaultOAuth2UserService {

    @Autowired
    private DBUserRepository DBUserRepository;
    @Autowired
    UserDBToAppUserService dbuserToAppUserService;

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<DBUser> userOptional = DBUserRepository.findByEmail(oAuth2UserInfo.getEmail());
        DBUser DBUser;
        if(userOptional.isPresent()) {
            DBUser = userOptional.get();
            if(!DBUser.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        DBUser.getProvider() + " account. Please use your " + DBUser.getProvider() +
                        " account to login.");
            }
            DBUser = updateExistingUser(DBUser, oAuth2UserInfo);
        } else {
            DBUser = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return dbuserToAppUserService.create(DBUser, oAuth2User.getAttributes());
    }

    private DBUser registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        DBUser DBUser = new DBUser();
        DBUser.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        DBUser.setProviderId(oAuth2UserInfo.getId());
        DBUser.setName(oAuth2UserInfo.getName());
        DBUser.setEmail(oAuth2UserInfo.getEmail());
        DBUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        DBUser.setGithubLogin((String) oAuth2UserInfo.getAttributes().get("login"));
        return DBUserRepository.save(DBUser);
    }

    private DBUser updateExistingUser(DBUser existingDBUser, OAuth2UserInfo oAuth2UserInfo) {
        existingDBUser.setName(oAuth2UserInfo.getName());
        existingDBUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return DBUserRepository.save(existingDBUser);
    }

}
