package legacycode;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServletTest extends MessageDigestValidatorSupport {

    @Mock
    private HttpServletResponse response;

    private PaymentServlet servlet;

    @Before
    public void init() {
        MessageDigestValidator messageDigestValidator = createMessageDigestValidator();
        servlet =  new PaymentServlet(null, messageDigestValidator);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(response);
    }

    @Test
    public void shouldFailIfInvalidSignature() throws IOException {
        //given
        //when
        servlet.handle(response, "", "", "", "", "");
        //then
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "MD5 signature does not match!");
    }

    @Test
    public void shouldFailIfInvalidTimestamp() throws IOException {
        //given
        //when
        servlet.handle(response, "", "", "", "100000", "5f142f02085b27c938897385782563f6");
        //then
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Timestamp does not match!");
    }

    @Test
    public void shouldFailIfUnrecognizedFormat() throws IOException {
        //given
        setCurrentTime(100000L);
        //when
        servlet.handle(response, "", "", "", "100000", "5f142f02085b27c938897385782563f6");
        //then
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Unrecognized format of payload!");
    }

}