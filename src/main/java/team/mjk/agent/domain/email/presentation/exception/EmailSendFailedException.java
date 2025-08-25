package team.mjk.agent.domain.email.presentation.exception;

import team.mjk.agent.global.exception.CustomException;

public class EmailSendFailedException extends CustomException {

    public EmailSendFailedException() {
        super(EmailExceptionCode.SEND_FAILED);
    }

}
