package com.olive.datapipeline.web;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdviserController {

    private final MessageSource messageSource;

    @ExceptionHandler(RequestNotPermitted.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ProblemDetail handleRateLimitException(RequestNotPermitted ex,
                                                  final Locale locale, final HttpRequest request) {
        return buildProblemDetail(HttpStatus.TOO_MANY_REQUESTS, ex, locale, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail commonException(final Exception ex, final Locale locale, final HttpRequest request) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, ex, locale, request);
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, Exception ex, Locale locale, HttpRequest request) {
        log.error("Exception: {}", ex.getMessage(), ex);

        var errorMessage = messageSource.getMessage(ex.getMessage(), new Object[]{}, ex.getMessage(), locale);
        var problemDetail = ProblemDetail.forStatusAndDetail(status, errorMessage);

        problemDetail.setTitle("Unexpected error");
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setInstance(request.getURI());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
