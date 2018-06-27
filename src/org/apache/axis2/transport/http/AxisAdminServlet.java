package org.apache.axis2.transport.http;

import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AxisAdminServlet extends org.apache.axis2.webapp.AxisAdminServlet {
	private static final long serialVersionUID = 3038257566847798292L;
	private static final Log log = LogFactory.getLog(AxisAdminServlet.class);

	public void init() throws ServletException {
		super.init();
		log.warn("Web application uses " + AxisAdminServlet.class.getName()
				+ "; please update web.xml to use "
				+ org.apache.axis2.webapp.AxisAdminServlet.class.getName()
				+ " instead");
	}
}