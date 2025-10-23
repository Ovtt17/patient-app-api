package com.patientapp.authservice.config.oauth2;

import com.patientapp.authservice.modules.user.entity.User;
import com.patientapp.authservice.modules.user.enums.AuthProvider;
import com.patientapp.authservice.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DefaultOAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        AuthProvider.fromProviderId(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no soportado: " + registrationId));

        OAuthUserInfo userInfo;

        if ("google".equals(registrationId)) {
            userInfo = new OAuthUserInfo(
                    oAuth2User.getAttribute("email"),
                    oAuth2User.getAttribute("given_name"),
                    oAuth2User.getAttribute("family_name"),
                    oAuth2User.getAttribute("picture")
            );
        } else if ("facebook".equals(registrationId)) {
            String name = oAuth2User.getAttribute("name");
            String givenName = name != null ? name.split(" ")[0] : "";
            String familyName = name != null && name.contains(" ") ? name.split(" ", 2)[1] : "";
            String email = oAuth2User.getAttribute("email");
            String picture = extractFacebookPicture(oAuth2User);

            userInfo = new OAuthUserInfo(email, givenName, familyName, picture);
        } else {
            throw new OAuth2AuthenticationException("Proveedor no soportado: " + registrationId);
        }

        User user = userRepository.findByEmail(userInfo.email())
                .orElseThrow(() -> new OAuth2AuthenticationException(
                        "Usuario no encontrado. Solo se permite login de usuarios existentes."));

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private String extractFacebookPicture(OAuth2User oAuth2User) {
        Object pictureObj = oAuth2User.getAttribute("picture");
        if (!(pictureObj instanceof Map<?, ?> pictureMap)) return null;

        Object dataObj = pictureMap.get("data");
        if (!(dataObj instanceof Map<?, ?> dataMap)) return null;

        Object url = dataMap.get("url");
        return url != null ? url.toString() : null;
    }
}
