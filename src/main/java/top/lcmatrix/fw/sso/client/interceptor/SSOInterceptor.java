package top.lcmatrix.fw.sso.client.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import okhttp3.Request;
import okhttp3.Response;
import top.lcmatrix.fw.sso.client.Constant;
import top.lcmatrix.fw.sso.client.entity.TokenObject;
import top.lcmatrix.fw.sso.client.listener.SSOEventListener;
import top.lcmatrix.fw.sso.client.setting.SSOSetting;
import top.lcmatrix.fw.sso.client.util.OkHttpUtil;
import top.lcmatrix.fw.sso.client.util.UrlUtil;

/**
 * <p>拦截本服务的请求</p>
 * <p>若未登录，则去sso-server验证该客户端是否已经登录sso-server（重定向或直接请求验证），如果已经登录，则获取登录信息，继续请求</p>
 * <p>否则重定向到登录页面</p>
 * @author chris
 *
 */
@Component
public class SSOInterceptor implements HandlerInterceptor {
	
	private static String COOKIE_IN_HEADER = "cookie";
	private static String ssoCookie = null;
	
	@Autowired
	private SSOSetting ssoSetting;
	
	@Autowired(required = false)
	private SSOEventListener ssoEventListener;
	
	private static final Logger logger = LoggerFactory.getLogger(SSOInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//忽略登出
		if(request.getRequestURI().equals(UrlUtil.concatUrl(request.getContextPath(), Constant.SSO_CLIENT_PATH, Constant.ENDPOINT_CLIENT_LOGOUT))){
			return true;
		}
		if (request.getSession().getAttribute(Constant.SESSION_TOKEN) != null) {
			return true;
		}else{
			String token = request.getParameter(Constant.PARAM_TOKEN);
			if (request.getHeader("X-Requested-With") != null
					&& request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
				TokenObject tokenObject = null;
				if(StringUtils.isEmpty(token)){
					tokenObject = fetchToken(ssoCookie != null ? ssoCookie : request.getHeader(COOKIE_IN_HEADER));
				}else{
					//验证token
					tokenObject = validateToken(token);
				}
				if(tokenObject != null){
					//保存session，以供sso中心控制
					saveSession(tokenObject, request, response);
					if(ssoEventListener != null){
						ssoEventListener.onValidateSuccess(tokenObject, request, response);
					}
					return true;
				}
				logger.info("sso 鉴权失败！");
				response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
			}else{
				if(StringUtils.isEmpty(token)){
					response.sendRedirect(UrlUtil.concatUrl(ssoSetting.getServerBaseUrl(), 
												Constant.SSO_SERVER_PATH, 
												Constant.ENDPOINT_SERVER_SYNC_REQUEST_VALIDATE + "?returnUrl=" 
							+ UrlUtil.encodeJustOnce(request.getRequestURL() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""), ssoSetting.getUrlEncoding())));
				}else{
					//查询SSO服务器是否已经登录
					TokenObject tokenObject = validateToken(token);
					if(tokenObject == null){
						logger.info("sso 鉴权失败！");
						//sso中心未登录
						response.sendRedirect(UrlUtil.concatUrl(ssoSetting.getServerBaseUrl(), 
												Constant.SSO_SERVER_PATH, 
												Constant.ENDPOINT_SERVER_LOGIN_PAGE + "?returnUrl=" 
								+ UrlUtil.encodeJustOnce(request.getRequestURL() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""), ssoSetting.getUrlEncoding())));
					}else{
						//保存session，以供sso中心控制
						saveSession(tokenObject, request, response);
						if(ssoEventListener != null){
							ssoEventListener.onValidateSuccess(tokenObject, request, response);
						}
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private TokenObject validateToken(String token){
		Response responseResult = null;
		try {
			responseResult = OkHttpUtil.get(UrlUtil.concatUrl(ssoSetting.getServerBaseUrl(), 
					Constant.SSO_SERVER_PATH, 
					Constant.ENDPOINT_SERVER_VALIDATETOKEN + "?token=" + token 
					+ "&baseUrl=" + ssoSetting.getMyBaseUrl()), null);
			if(responseResult != null){
				if(responseResult.isSuccessful()){
					String responseText = responseResult.body().string();
					if(StringUtils.isNotEmpty(responseText)){
						try {
							TokenObject tokenObject = JSON.parseObject(responseText, TokenObject.class);
							ssoCookie = tokenObject.getCookie();
							return tokenObject;
						} catch (Exception e) {
							logger.warn("sso 鉴权失败，解析返回数据失败", e);
						}
					}
				}else{
					logger.info("sso 鉴权失败，错误码：" + responseResult.code());
				}
			}
		} catch (IOException e1) {
			logger.warn("sso 鉴权失败，请求鉴权中心失败", e1);
		} finally {
			if(responseResult != null){
				responseResult.close();
			}
		}
		return null;
	}
	
	private TokenObject fetchToken(String cookieInHeader){
		Response responseResult = null;
		try {
			Request tokenRequest = new Request.Builder().url(UrlUtil.concatUrl(ssoSetting.getServerBaseUrl(),
																Constant.SSO_SERVER_PATH, 
																Constant.ENDPOINT_SERVER_AJAX_REQUEST_VALIDATE + "?baseUrl=" + ssoSetting.getMyBaseUrl()))
				.addHeader(COOKIE_IN_HEADER, cookieInHeader).get().build();
			responseResult = OkHttpUtil.globalOkHttpClient().newCall(tokenRequest).execute();
			if(responseResult != null && responseResult.isSuccessful()){
				return JSON.parseObject(responseResult.body().string(), TokenObject.class);
			}
		} catch (IOException e) {
			logger.warn("sso 从鉴权中心获取token失败", e);
		} finally {
			if(responseResult != null){
				responseResult.close();
			}
		}
		return null;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	private void saveSession(TokenObject tokenObject, HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		session.setAttribute(Constant.SESSION_TOKEN, tokenObject);
		request.getServletContext().setAttribute(Constant.CONTEXT_ATTR_TOKEN_PREFIX + tokenObject.getToken(), session);
	}
}
