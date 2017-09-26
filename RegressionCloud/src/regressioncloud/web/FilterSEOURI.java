package com.regressioncloud.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.regressioncloud.util.WebAppSettings;

/**
 * Servlet Filter implementation class FilterSEOURI
 */
@WebFilter(filterName = "FilterSEOURI", urlPatterns = { "/*" })
public class FilterSEOURI implements Filter {

	private SEOServletMap seoMap = null;
	
	public FilterSEOURI() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String contentPath = request.getServletPath().split("/")[1];
		
		if (seoMap.getQuery(contentPath) != null && !seoMap.getQuery(contentPath).isEmpty() ) {
			req.setAttribute("content", seoMap.getQuery(contentPath));
			req.getRequestDispatcher("index.jsp").forward(req, resp);
			return;
		} 
		
		chain.doFilter(req, resp);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		ServletContext servletContext = fConfig.getServletContext();
		WebAppSettings.buildObject(servletContext);
		seoMap = SEOServletMap.getObject(servletContext);
	}

}
