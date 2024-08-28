package com.poly.Recruitment.util;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionUtil {
    private final HttpSession session;

    public Object getSessionId(String name) {
        return session.getAttribute(name);
    }

    public void setSessionId(String name, Object value) {
        session.setAttribute(name, value);
    }
}
