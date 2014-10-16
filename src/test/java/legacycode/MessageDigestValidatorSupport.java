package legacycode;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class MessageDigestValidatorSupport {

    private long currentTime = 0;

    protected MessageDigestValidator createMessageDigestValidator() {
        return new MessageDigestValidator() {
            @Override
            long currentTime() {
                return currentTime;
            }
        };
    }

    protected void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}