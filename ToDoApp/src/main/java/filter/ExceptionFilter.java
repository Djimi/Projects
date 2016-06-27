package filter;

import javax.servlet.*;
import java.io.IOException;

public class ExceptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("/WEB-INF/jsp/view/serverInternalError.jsp").forward
                    (request, response);

        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
