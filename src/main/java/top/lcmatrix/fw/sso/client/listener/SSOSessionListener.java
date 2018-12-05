package top.lcmatrix.fw.sso.client.listener;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import top.lcmatrix.fw.sso.client.Constant;
import top.lcmatrix.fw.sso.client.entity.TokenObject;
import top.lcmatrix.fw.sso.client.interceptor.SSOInterceptor;

/**
 * <p>本服务的登录失效时，移除存储的相关数据</p>
 * <p>spring boot应用需要启动类配置@ServletComponentScan注解，额外增加包路径top.lcmatrix.fw.sso.client</p>
 * <p>或者使用其他方法直接注册本监听器</p>
 * @author chris
 *
 */
@WebListener
public class SSOSessionListener implements HttpSessionListener{
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		TokenObject tokenObject = (TokenObject) session.getAttribute(Constant.SESSION_TOKEN);
		if(tokenObject != null){
			ServletContext servletContext = session.getServletContext();
			servletContext.removeAttribute(Constant.CONTEXT_ATTR_TOKEN_PREFIX + tokenObject.getToken());
		}
		SSOInterceptor.clearCookie();
	}
}
