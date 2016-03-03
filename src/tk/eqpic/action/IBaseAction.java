package tk.eqpic.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public abstract class IBaseAction extends ActionSupport
	implements SessionAware,ServletRequestAware, 
	ServletResponseAware, ApplicationAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8161012584047747800L;
	/* 封装request对象与response对象 */
	public HttpServletRequest request;
	public HttpServletResponse response;
	public Map<String, Object> session;
	public Map<String, Object> application;
	

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}
}
