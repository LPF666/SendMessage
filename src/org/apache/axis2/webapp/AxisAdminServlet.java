package org.apache.axis2.webapp;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.AxisServlet;

public class AxisAdminServlet extends AxisServlet {
	private static final long serialVersionUID = -6740625806509755370L;
	protected transient AdminAgent agent;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doGet(req, res);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			req.getSession().setAttribute("servicePath",
					this.configContext.getServicePath());
			this.agent.handle(req, resp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		this.configContext = ((ConfigurationContext) servletContext
				.getAttribute("CONFIGURATION_CONTEXT"));

		servletContext.setAttribute(getClass().getName(), this);
		this.agent = new AdminAgent(this.configContext);
		this.servletConfig = config;
	}

	public void init() throws ServletException {
		if (this.servletConfig != null)
			init(this.servletConfig);
	}
}