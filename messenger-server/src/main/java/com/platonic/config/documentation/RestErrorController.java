package com.platonic.config.documentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class RestErrorController implements ErrorController {
    private static final String PATH = "/error";

    @Value("${debug:true}")
    private boolean debug;

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    public ErrorJson error(HttpServletRequest request, HttpServletResponse response) {
        return new ErrorJson(response.getStatus(), getErrorAttributes(request, debug));
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    private class ErrorJson {
        public Integer status;
        public String error;
        public String message;
        public String trace;

        public ErrorJson(int status, Map<String, Object> errorAttributes) {
            this.status = status;
            this.error = (String) errorAttributes.get("error");
            this.message = (String) errorAttributes.get("message");
            this.trace = (String) errorAttributes.get("trace");
        }
    }
}