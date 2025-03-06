// package net.engineeringdigest.journalApp.aspects;

// import org.springframework.lang.Nullable;
// import org.springframework.stereotype.Component;
// import org.springframework.web.servlet.HandlerInterceptor;
// import org.springframework.web.servlet.ModelAndView;
// import org.springframework.web.util.ContentCachingResponseWrapper;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.extern.slf4j.Slf4j;
// import javax.servlet.*;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
// class ResponseCachingFilter implements Filter {

//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//         if (response instanceof HttpServletResponse) {
//             ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);
//             chain.doFilter(request, cachingResponse); // Proceed with filter chain
//             cachingResponse.copyBodyToResponse(); // Copy cached body back to response
//         } else {
//             chain.doFilter(request, response);
//         }
//     }
// }

// @Slf4j
// @Component
// public class TransformInterceptor implements HandlerInterceptor{
//     public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView){
//         log.info("inside transform intereceptor");

//         if(response instanceof ContentCachingResponseWrapper){
//             ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
//             System.out.println("wrapper: " + wrapper.getResponse()); 
//         }
//         System.out.println("request: " + request);
//         System.out.println("status: " + response);
//         // System.out.println("response: " + response.);

//         System.out.println("handler: "+ handler);
//         System.out.println("modelAndView" + modelAndView);
//     }
// }
