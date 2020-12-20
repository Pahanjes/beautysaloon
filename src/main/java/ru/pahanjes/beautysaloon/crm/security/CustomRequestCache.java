package ru.pahanjes.beautysaloon.crm.security;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class CustomRequestCache extends HttpSessionRequestCache {

    // Сохраняет запросы, не прошедшие проверку подлинности, чтобы перенаправить пользователя
    // На страницу, к которой он пытался получить доступ, после входа в систему.
    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (!SecurityUtils.isFrameworkInternalRequest(request)) {
            super.saveRequest(request, response);
        }
    }

}