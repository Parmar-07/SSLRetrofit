package ssl.hanshake.retofit.exceptions;

import androidx.annotation.Nullable;

public class HandShakeException extends Exception {

    private HandShakeError error;

    public HandShakeError getError() {
        return error;
    }

    public HandShakeException(HandShakeError error) {
        this.error = error;
    }

    @Nullable
    @Override
    public String getMessage() {

        String message;

        switch (error) {
            case NO_URL_FOUND:
                message = "No URL found for Handshake !";
                break;
            case NO_PUBLIC_KEY_FOUND:
                message = "No Public Key Found !";
                break;
            case PUBLIC_KEY_EXTRACT_ERROR:
                message = "Public Key Extractor Error : " + super.getMessage();
                break;
            case NO_MODE_FOUND:
                message = "Kindly Select the mode for Handshake";
                break;
            case CERTIFICATE_FILE_NOT_FOUND:
                message = "Error while loading certificate file !";
                break;
            case INVALID_URL:
                message = "Invalid Hand-Shaking URL !";
                break;
            case CERTIFICATE_ALGORITHM_ERROR:
                message = "Issue found in CA(Certificate Algorithm) !";
                break;
            case CERTIFICATE_KEY_ERROR:
                message = "Issue found in CK(Certificate Key) !";
                break;
            case NO_CERTIFICATE_FOUND:
                message = "No Certificate found !";
                break;
            default:
                message = super.getMessage();
                break;
        }

        return message;
    }
}
