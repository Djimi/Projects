package filter;

import servlet.IntroServlet;
import servlet.LoginServlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session == null || session.getAttribute(LoginServlet.USERNAME) == null) {
            ((HttpServletResponse) response).sendRedirect(IntroServlet.INTRO);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
