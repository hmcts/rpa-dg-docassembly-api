package uk.gov.hmcts.reform.dg.docassembly.dto;

public class JwtDto {

    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        // Avoid non JWT Tokens being set from the header (Fix for SonarCloud https://sonarcloud.io/organizations/hmcts/rules?open=javasecurity%3AS2083&rule_key=javasecurity%3AS2083)
//        if (jwt.matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$")) {
//            this.jwt = jwt;
//        } else {
//            this.jwt = "";
//        }
        // TEMPORARY FOR DEBUG!
        this.jwt = jwt;
    }
}
