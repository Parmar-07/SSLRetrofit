package ssl.hanshake.retofit.mode;

import ssl.hanshake.retofit.exceptions.HandShakeError;
import ssl.hanshake.retofit.exceptions.HandShakeException;
import ssl.hanshake.retofit.mode.builder.Builder;
import ssl.hanshake.retofit.mode.builder.PinBuilder;

public class PinMode extends HandShakeMode {


    private PinMode(PinBuilder pinBuilder) {
        super.setPinBuilder(pinBuilder);
        super.setMode(PIN_MODE);
    }

    public PinMode(){}


    @Override
    public HandShakeMode getHandShakeMode(Builder modeBuilder) throws HandShakeException {
        if (modeBuilder instanceof PinBuilder) {
            return new PinMode((PinBuilder) modeBuilder);
        } else {
            throw new HandShakeException(HandShakeError.NO_MODE_FOUND);
        }
    }
}
