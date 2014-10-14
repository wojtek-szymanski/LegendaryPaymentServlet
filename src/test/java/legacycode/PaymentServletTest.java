package legacycode;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServletTest {

    @Mock
    private HttpServletResponse response;

    private long currentTime = 0;

    private PaymentServlet sut;
    private MessageDigestValidator validator;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void init() {
        validator = new MessageDigestValidator() {
            @Override
            long currentTime() {
                return currentTime;
            }
        };
        sut =  new PaymentServlet(null, validator);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(response);
    }

    @Test
    public void shouldFailIfInvalidSignature() throws IOException {
        //given
        //when
        sut.handle(response, "", "", "", "", "");
        //then
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "MD5 signature do not match!");
    }

    @Test
    public void shouldFailIfInvalidTimestamp() throws IOException {
        //given
        //when
        sut.handle(response, "", "", "", "100000", "5f142f02085b27c938897385782563f6");
        //then
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Timestamp do not match!");
    }

    @Test
    public void shouldFailIfUnrecognizedFormat() throws IOException {
        //given
        currentTime = 100001L;
        //when
        sut.handle(response, "", "", "", "100000", "5f142f02085b27c938897385782563f6");
        //then
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Unrecognized format of payload!");
    }

    @Test
    public void shouldPassIfCorrectSignature() throws IOException {
        //given
        currentTime = 1411677303294L;
        //when
        // amount=10000&status=OK&payload=order_id%3A6792&ts=1411677303294&md5=0c672178b3ce4ddc5404833b94cf5982
        exception.expect(RuntimeException.class);
        sut.handle(response, "10000", "OK", "order_id:6792", "1411677303294", "0c672178b3ce4ddc5404833b94cf5982");
        //then
        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "MD5 signature do not match!");
    }

}