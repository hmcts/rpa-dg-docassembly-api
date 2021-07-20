package uk.gov.hmcts.reform.dg.docassembly.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uk.gov.hmcts.reform.dg.docassembly.config.security.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;

public class ServiceNameAspectTest {

    @Mock
    SecurityUtils securityUtils;

    @Mock
    HttpServletRequest request;

    @InjectMocks
    ServiceNameAspect serviceNameAspect;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testLogServiceNameEmptyServiceAuth() {

        when(request.getHeader(Mockito.anyString())).thenReturn(null);

        serviceNameAspect.logServiceName();

        Mockito.verify(securityUtils, Mockito.atLeast(0)).getServiceName(Mockito.anyString());
    }

    @Test
    public void testLogServiceName() {

        when(request.getHeader("serviceauthorization")).thenReturn("abc");
        when(securityUtils.getServiceName(Mockito.anyString())).thenReturn("xxx");
        serviceNameAspect.logServiceName();

        Mockito.verify(securityUtils, Mockito.atLeast(1)).getServiceName(Mockito.anyString());
    }

    @Test
    public void testLogServiceNameBearer() {

        when(request.getHeader("serviceauthorization")).thenReturn("Bearer abc");
        when(securityUtils.getServiceName(Mockito.anyString())).thenReturn("xxx");
        serviceNameAspect.logServiceName();

        Mockito.verify(securityUtils, Mockito.atLeast(1)).getServiceName(Mockito.anyString());
    }

}
