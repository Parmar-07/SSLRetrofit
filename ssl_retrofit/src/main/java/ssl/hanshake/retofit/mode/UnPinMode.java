package ssl.hanshake.retofit.mode;

import ssl.hanshake.retofit.exceptions.HandShakeError;
import ssl.hanshake.retofit.exceptions.HandShakeException;
import ssl.hanshake.retofit.mode.builder.Builder;
import ssl.hanshake.retofit.mode.builder.UnPinBuilder;

public class UnPinMode extends HandShakeMode {


    public UnPinMode(UnPinBuilder pinBuilder) {
        super.setUnPinBuilder(pinBuilder);
        super.setMode(UN_PIN_MODE);
    }

    public UnPinMode() {
    }

    @Override
    public HandShakeMode getHandShakeMode(Builder modeBuilder) throws HandShakeException {
        if (modeBuilder instanceof UnPinBuilder) {
            return new UnPinMode((UnPinBuilder) modeBuilder);
        } else {
            throw new HandShakeException(HandShakeError.NO_MODE_FOUND);
        }
    }
}
