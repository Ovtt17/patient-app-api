package com.patientapp.authservice.config.oauth2;

import com.patientapp.authservice.entity.User;
import com.patientapp.authservice.enums.AuthProvider;
import com.patientapp.authservice.repository.RoleRepository;
import com.patientapp.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.patientapp.authservice.enums.Roles.PACIENTE;

@Service
@RequiredArgsConstructor
public class DefaultOAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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

        final String regIdFinal = registrationId;
        User user = userRepository.findByEmail(userInfo.email())
                .orElseGet(() -> createdNewUser(userInfo, regIdFinal));

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

    private User createdNewUser(OAuthUserInfo info, String registrationId) {
        var patientRole = roleRepository.findByName(PACIENTE.name())
                .orElseThrow(() -> new IllegalStateException("ROL " + PACIENTE.name() + " no encontrado"));

        return userRepository.save(
                User.builder()
                .firstName(info.givenName())
                .lastName(info.familyName())
                .username(info.email().split("@")[0])
                .email(info.email())
                .profilePicture(info.picture())
                .enabled(true)
                .accountLocked(false)
                .provider(AuthProvider.valueOf(registrationId.toUpperCase()))
                .roles(List.of(patientRole))
                .build()
        );
    }

}
