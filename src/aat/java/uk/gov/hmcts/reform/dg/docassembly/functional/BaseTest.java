package uk.gov.hmcts.reform.dg.docassembly.functional;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.dg.docassembly.testutil.TestUtil;
import uk.gov.hmcts.reform.dg.docassembly.testutil.ToggleProperties;
import uk.gov.hmcts.reform.em.EmTestConfig;

@SpringBootTest(classes = {TestUtil.class, EmTestConfig.class})
@PropertySource(value = "classpath:application.yml")
@RunWith(SpringRunner.class)
@EnableConfigurationProperties(ToggleProperties.class)
public abstract class BaseTest {

    @Autowired
    TestUtil testUtil;

    @Autowired
    ToggleProperties toggleProperties;

}
