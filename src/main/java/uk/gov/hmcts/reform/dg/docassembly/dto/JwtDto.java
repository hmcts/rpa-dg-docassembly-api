package uk.gov.hmcts.reform.dg.docassembly.dto;

public class JwtDto {

    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        // Avoid Tokens being set from the header without validation (Fix for SonarCloud https://sonarcloud.io/organizations/hmcts/rules?open=javasecurity%3AS2083&rule_key=javasecurity%3AS2083)
        // This object does not hold a true JWT Token, but it does hold a Base64 encoded string, thus, if it is Base64 encoded, we allow it through.
        if (jwt.matches("^[A-Za-z0-9-_.+/=]+$")) {
            this.jwt = jwt;
        } else {
            this.jwt = "";
        }
    }
}
