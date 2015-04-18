package org.telosystools.saas.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;

/**
 * Created by Adrian on 18/04/15.
 * TODO : Voir quelle implémentation est la plus adaptée - Annotation + Extends = overkill
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "")
public class BadRequestException extends HttpServerErrorException {

    public BadRequestException(String statusText) {
        super(HttpStatus.BAD_REQUEST, statusText);
    }
}
