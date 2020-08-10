package com.fp.oauth2.configuration;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@WebFilter
@Slf4j
public class RequestFilterConfiguration extends OncePerRequestFilter {

    private static BufferedWriter writer = null;
    private static final List<MediaType> VISIBLE_TYPES;
    private int validate = -1;


    static {
        VISIBLE_TYPES = Arrays.asList(MediaType.valueOf("text/*"), MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.valueOf("application/*+json"), MediaType.valueOf("application/*+xml"), MediaType.MULTIPART_FORM_DATA);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            this.doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    private String getCorrelationId() {
        return UUID.randomUUID().toString().toUpperCase().replace("-", "") + " |";
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
            this.beforeRequest(request, response);
            if (this.validate == -1) {
                filterChain.doFilter(request, response);
            } else {
                //this.sendErrorResponse(request, response, "");
            }
        } finally {
            this.afterRequest(request, response);
            response.copyBodyToResponse();
        }

    }

    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) throws IOException {
        if (this.logger.isInfoEnabled()) {
            this.logRequestHeader(request, request.getRemoteAddr() + "|>");
        }

    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) throws IOException {
        if (this.logger.isInfoEnabled()) {
            this.logRequestBody(request, request.getRemoteAddr() + "|>");
            this.logResponse(response, request.getRemoteAddr() + "|<");
        }

    }

    private void logContent(byte[] content, String contentType, String contentEncoding, String prefix, String state) {
        boolean visible = true;
        if (contentType != null) {
            MediaType mediaType = MediaType.valueOf(contentType);
            visible = VISIBLE_TYPES.stream().anyMatch((visibleType) -> {
                return visibleType.includes(mediaType);
            });
        }

        if (visible) {
            try {
                String contentString = new String(content, contentEncoding);
                if (contentType != null && contentType.contains("application/json")) {
                    log.info(state + " : " + new Gson().toJson(contentString.replaceAll("(\\r\\n\\t|\\n)", "")));
                } else if (contentType != null && contentType.contains(MediaType.APPLICATION_FORM_URLENCODED.toString())){
                    log.info("Param" + ":" + contentString);
                }
            } catch (UnsupportedEncodingException var8) {
                log.info("{} [{} bytes content]", prefix, content.length);
            }
        } else {
            log.info("{} [{} bytes content]", prefix, content.length);
        }

    }

    private void logRequestHeader(ContentCachingRequestWrapper request, String prefix) throws IOException {
        String queryString = request.getQueryString();
        if (queryString == null) {
            log.info("{} {} {}", new Object[]{prefix, request.getMethod(), request.getRequestURI()});
        } else {
            log.info("{} {} {}?{}", new Object[]{prefix, request.getMethod(), request.getRequestURI(), queryString});
        }

        Collections.list(request.getHeaderNames()).forEach((headerName) -> {
            Collections.list(request.getHeaders(headerName)).forEach((headerValue) -> {
                log.info("{} {}: {}", new Object[]{prefix, headerName, headerValue});
            });
        });
        log.info(" Session ID: ", RequestContextHolder.currentRequestAttributes().getSessionId());
    }

    private void logRequestBody(ContentCachingRequestWrapper request, String prefix) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            log.info(" --- Request Body ---");
            this.logContent(content, request.getContentType(), request.getCharacterEncoding(), prefix, "RequestBody");
        }

    }

    private void logResponse(ContentCachingResponseWrapper response, String prefix) throws IOException {
        int status = response.getStatus();
        log.info("{} {} {}", new Object[]{prefix, status, HttpStatus.valueOf(status).getReasonPhrase()});
        response.getHeaderNames().forEach((headerName) -> {
            response.getHeaders(headerName).forEach((headerValue) -> {
                log.info("{} {}: {}", new Object[]{prefix, headerName, headerValue});
            });
        });
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            log.info(" --- Response Body ---");
            this.logContent(content, response.getContentType(), response.getCharacterEncoding(), prefix, "ResponseBody");
        }

    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        return request instanceof ContentCachingRequestWrapper ? (ContentCachingRequestWrapper)request : new ContentCachingRequestWrapper(request);
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        return response instanceof ContentCachingResponseWrapper ? (ContentCachingResponseWrapper)response : new ContentCachingResponseWrapper(response);
    }
}

