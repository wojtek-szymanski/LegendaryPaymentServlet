package legacycode;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class MessageDigestValidatorTest extends MessageDigestValidatorSupport {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private MessageDigestValidator messageDigestValidator;

    @Before
    public void init() {
        messageDigestValidator = createMessageDigestValidator();
    }

    @Test
    public void shouldFailIfInvalidSignature() {
        exception.expect(PaymentValidationException.class);
        exception.expectMessage("MD5 signature does not match!");

        messageDigestValidator.validate("", "", "", "", "");
    }

    @Test
    public void shouldFailIfInvalidTimestamp() throws IOException {
        exception.expect(PaymentValidationException.class);
        exception.expectMessage("Timestamp does not match!");

        messageDigestValidator.validate("", "", "", "100000", "5f142f02085b27c938897385782563f6");
    }

    @Test
    public void shouldPassIfValidTimestampAndSignature() throws IOException {
        // given
        setCurrentTime(100000);
        //when
        messageDigestValidator.validate("", "", "", "100000", "5f142f02085b27c938897385782563f6");
    }


    @Test
    public void shouldPadMd5SignatureWithZeros() {
        //given
        setCurrentTime(1411677303294L);
        //when
        // amount=10000&status=OK&payload=order_id%3A6792&ts=1411677303294&md5=0c672178b3ce4ddc5404833b94cf5982
        messageDigestValidator.validate("10000", "OK", "order_id:6792", "1411677303294", "0c672178b3ce4ddc5404833b94cf5982");
    }
}
