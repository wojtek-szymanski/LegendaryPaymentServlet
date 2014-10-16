package legacycode;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestValidator {

    private static final String secret = "15c84df6-bfa3-46c1-8929-a5dedaeab4a4";

    void validate(String amount, String status, String payload, String timestamp, String md5) {
        String expectedMd5 = DigestUtils.md5Hex(amount + status + payload + timestamp + secret);
        System.out.println("Expected MD5: " + expectedMd5);

        if (!expectedMd5.equals(md5)) {
            throw new PaymentValidationException("MD5 signature does not match!");
        }

        if (Math.abs(currentTime() - Long.valueOf(timestamp)) > 60000) {
            throw new PaymentValidationException("Timestamp does not match!");
        }
    }

    @VisibleForTesting
    long currentTime() {
        return System.currentTimeMillis();
    }
}