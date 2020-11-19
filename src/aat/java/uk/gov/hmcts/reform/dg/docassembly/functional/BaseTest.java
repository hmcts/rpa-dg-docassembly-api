package uk.gov.hmcts.reform.dg.docassembly.functional;

import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import uk.gov.hmcts.reform.dg.docassembly.testutil.TestUtil;
import uk.gov.hmcts.reform.dg.docassembly.testutil.ToggleProperties;
import uk.gov.hmcts.reform.em.EmTestConfig;

@SpringBootTest(classes = {TestUtil.class, EmTestConfig.class})
@TestPropertySource(value = "classpath:application.yml")
@EnableConfigurationProperties(ToggleProperties.class)
@RunWith(SpringIntegrationSerenityRunner.class)
@WithTags({@WithTag("testType:Functional")})
public abstract class BaseTest {

    @Autowired
    TestUtil testUtil;

    @Autowired
    ToggleProperties toggleProperties;

}
