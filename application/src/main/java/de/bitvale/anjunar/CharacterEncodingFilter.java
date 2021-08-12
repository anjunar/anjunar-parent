package de.bitvale.anjunar;

import javax.servlet.*;

import java.io.IOException;

public class CharacterEncodingFilter implements Filter {

    private String encoding;

    private boolean forceEncoding;

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
        String encod = filterConfig.getInitParameter("encoding");
        if(encod !=null){
            encoding = encod ;
        }
        String forceEncod = filterConfig.getInitParameter("forceEncoding");
        if(forceEncod !=null){
            forceEncoding = Boolean.valueOf(forceEncod) ;
        }
    }

    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.encoding != null && (this.forceEncoding || request.getCharacterEncoding() == null)) {
            request.setCharacterEncoding(this.encoding);
            if (this.forceEncoding) {
                response.setCharacterEncoding(this.encoding);
            }
        }
        filterChain.doFilter(request, response);
    }

}