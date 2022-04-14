//package com.loyalty.marketplace;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Map;
//
//import javax.servlet.FilterChain;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//import org.springframework.web.util.WebUtils;
//
//@Component
//public class RequestTraceFilter extends WebRequestTraceFilter {
//
// 
//
//private static final String RESPONSE_BODY = "resBody";
//private static final String REQUEST_BODY = "reqBody";
//
// 
//
//public RequestTraceFilter(TraceRepository repository, TraceProperties properties) {
//    super(repository, properties);
//}
//
// 
//
//@Override
//protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//        throws ServletException, IOException {
//    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
//    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
//    filterChain.doFilter(requestWrapper, responseWrapper);
//    responseWrapper.copyBodyToResponse();
//    request.setAttribute(REQUEST_BODY, getRequestBody(requestWrapper));
//    request.setAttribute(RESPONSE_BODY, getResponseBody(responseWrapper));
//    super.doFilterInternal(requestWrapper, responseWrapper, filterChain);
//}
//
// 
//
//
//private String getRequestBody(ContentCachingRequestWrapper request) {
//    ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
//    String characterEncoding = wrapper.getCharacterEncoding();
//    return getPayload(wrapper.getContentAsByteArray(), characterEncoding);
//}
//
// 
//
//@Override
//protected Map<String, Object> getTrace(HttpServletRequest request) {
//    Map<String, Object> trace = super.getTrace(request);
//    Object requestBody = request.getAttribute(REQUEST_BODY);
//    Object responseBody = request.getAttribute(RESPONSE_BODY);
//    if(requestBody != null) {
//        trace.put(REQUEST_BODY, (String) requestBody);
//    }
//    if(responseBody != null) {
//        trace.put(RESPONSE_BODY, (String) responseBody);
//    }
//    return trace;
//}
//
// 
//
//public String getPayload(byte[] buf, String characterEncoding) {
//    String payload = null;
//        if (buf.length > 0) {
//            try {
//                payload = new String(buf, 0, buf.length, characterEncoding);
//            }
//            catch (UnsupportedEncodingException ex) {
//                payload = "[unknown]";
//            }
//        }
//    return payload;
//}
//
// 
//
//private String getResponseBody(ContentCachingResponseWrapper response) {
//    ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
//    return getPayload(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
//}
//}
