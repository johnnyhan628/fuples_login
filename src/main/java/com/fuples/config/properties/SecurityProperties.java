package com.fuples.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter @Setter
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private List<String> permitAll;
    private List<String> authenticated;
    private Roles roles;

    public String[] getPermitAll() {
        return permitAll.toArray(new String[0]);
    }

    public String[] getAuthenticated() {
        return authenticated.toArray(new String[0]);
    }

    @Getter
    @Setter
    public static class Roles {
        private List<String> signingUp;
        private List<String> approved;

        public String[] getSigningUp() {
            return signingUp.toArray(new String[0]);
        }

        public String[] getApproved() {
            return approved.toArray(new String[0]);
        }

    }

}
