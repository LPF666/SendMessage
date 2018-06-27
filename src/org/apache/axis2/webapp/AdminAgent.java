package org.apache.axis2.webapp;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ServiceGroupContext;
import org.apache.axis2.deployment.util.PhasesInfo;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.transport.http.AbstractAgent;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AdminAgent extends AbstractAgent {
	private static final Log log = LogFactory.getLog(AbstractAgent.class);
	private static final String LIST_SERVICE_GROUP_JSP = "ListServiceGroup.jsp";
	private static final String LIST_SERVICES_JSP_NAME = "listService.jsp";
	private static final String LIST_SINGLE_SERVICES_JSP_NAME = "listSingleService.jsp";
	private static final String SELECT_SERVICE_JSP_NAME = "SelectService.jsp";
	private static final String IN_ACTIVATE_SERVICE_JSP_NAME = "InActivateService.jsp";
	private static final String ACTIVATE_SERVICE_JSP_NAME = "ActivateService.jsp";
	private static final String LIST_PHASES_JSP_NAME = "viewphases.jsp";
	private static final String LIST_GLOABLLY_ENGAGED_MODULES_JSP_NAME = "globalModules.jsp";
	private static final String LIST_AVAILABLE_MODULES_JSP_NAME = "listModules.jsp";
	private static final String ENGAGING_MODULE_TO_SERVICE_JSP_NAME = "engagingtoaservice.jsp";
	private static final String ENGAGING_MODULE_TO_SERVICE_GROUP_JSP_NAME = "EngageToServiceGroup.jsp";
	private static final String ENGAGING_MODULE_GLOBALLY_JSP_NAME = "engagingglobally.jsp";
	public static final String ADMIN_JSP_NAME = "admin.jsp";
	private static final String VIEW_GLOBAL_HANDLERS_JSP_NAME = "ViewGlobalHandlers.jsp";
	private static final String VIEW_SERVICE_HANDLERS_JSP_NAME = "ViewServiceHandlers.jsp";
	private static final String SERVICE_PARA_EDIT_JSP_NAME = "ServiceParaEdit.jsp";
	private static final String ENGAGE_TO_OPERATION_JSP_NAME = "engagingtoanoperation.jsp";
	private static final String LOGIN_JSP_NAME = "Login.jsp";
	private File serviceDir;

	public AdminAgent(ConfigurationContext aConfigContext) {
		super(aConfigContext);
		try {
			if (this.configContext.getAxisConfiguration().getRepository() != null) {
				File repoDir = new File(this.configContext
						.getAxisConfiguration().getRepository().getFile());

				this.serviceDir = new File(repoDir, "services");
				if (!this.serviceDir.exists())
					this.serviceDir.mkdirs();
			}
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	public void handle(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException,
			ServletException {
		if ((axisSecurityEnabled())
				&& (authorizationRequired(httpServletRequest)))
			renderView("Login.jsp", httpServletRequest, httpServletResponse);
		else
			super.handle(httpServletRequest, httpServletResponse);
	}

	public void processIndex(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		renderView("admin.jsp", req, res);
	}

	public void processUpload(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String hasHotDeployment = (String) this.configContext
				.getAxisConfiguration().getParameterValue("hotdeployment");

		String hasHotUpdate = (String) this.configContext
				.getAxisConfiguration().getParameterValue("hotupdate");

		req.setAttribute("hotDeployment",
				hasHotDeployment.equals("true") ? "enabled" : "disabled");

		req.setAttribute("hotUpdate", hasHotUpdate.equals("true") ? "enabled"
				: "disabled");
		RequestContext reqContext = new ServletRequestContext(req);

		boolean isMultipart = ServletFileUpload.isMultipartContent(reqContext);
		if (isMultipart) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();

				ServletFileUpload upload = new ServletFileUpload(factory);
				List items = upload.parseRequest(req);

				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (!item.isFormField()) {
						String fileName = item.getName();
						String fileExtesion = fileName;
						fileExtesion = fileExtesion.toLowerCase();
						if ((!fileExtesion.endsWith(".jar"))
								&& (!fileExtesion.endsWith(".aar"))) {
							req.setAttribute("status", "failure");
							req.setAttribute("cause", "Unsupported file type "
									+ fileExtesion);
						} else {
							String fileNameOnly;
							if (fileName.indexOf("\\") < 0) {
								fileNameOnly = fileName.substring(fileName
										.lastIndexOf("/") + 1, fileName
										.length());
							} else {
								fileNameOnly = fileName.substring(fileName
										.lastIndexOf("\\") + 1, fileName
										.length());
							}

							File uploadedFile = new File(this.serviceDir,
									fileNameOnly);
							item.write(uploadedFile);
							req.setAttribute("status", "success");
							req.setAttribute("filename", fileNameOnly);
						}
					}
				}
			} catch (Exception e) {
				req.setAttribute("status", "failure");
				req.setAttribute("cause", e.getMessage());
			}
		}

		renderView("upload.jsp", req, res);
	}

	public void processLogin(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String username = req.getParameter("userName");
		String password = req.getParameter("password");

		if ((username == null) || (password == null)
				|| (username.trim().length() == 0)
				|| (password.trim().length() == 0)) {
			req.setAttribute("errorMessage", "Invalid auth credentials!");
			renderView("Login.jsp", req, res);
			return;
		}

		String adminUserName = (String) this.configContext
				.getAxisConfiguration().getParameter("userName").getValue();

		String adminPassword = (String) this.configContext
				.getAxisConfiguration().getParameter("password").getValue();

		if ((username.equals(adminUserName))
				&& (password.equals(adminPassword))) {
			req.getSession().setAttribute("Logged", "Yes");
			renderView("admin.jsp", req, res);
		} else {
			req.setAttribute("errorMessage", "Invalid auth credentials!");
			renderView("Login.jsp", req, res);
		}
	}

	public void processEditServicePara(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String serviceName = req.getParameter("axisService");
		if (req.getParameter("changePara") != null) {
			AxisService service = this.configContext.getAxisConfiguration()
					.getService(serviceName);
			Iterator iterator;
			if (service != null) {
				for (Parameter parameter : service.getParameters()) {
					String para = req.getParameter(serviceName + "_"
							+ parameter.getName());
					service.addParameter(new Parameter(parameter.getName(),
							para));
				}

				for (iterator = service.getOperations(); iterator.hasNext();) {
					AxisOperation axisOperation = (AxisOperation) iterator.next();
					String op_name = axisOperation.getName().getLocalPart();

					for (Parameter parameter : axisOperation.getParameters()) {
						String para = req.getParameter(op_name + "_"
								+ parameter.getName());

						axisOperation.addParameter(new Parameter(parameter
								.getName(), para));
					}
				}
			}
			AxisOperation axisOperation;
			String op_name;
			res.setContentType("text/html");
			req.setAttribute("status", "Parameters Changed Successfully.");
			req.getSession().removeAttribute("service");
		} else {
			AxisService serviceTemp = this.configContext.getAxisConfiguration()
					.getServiceForActivation(serviceName);

			if (serviceTemp.isActive()) {
				if (serviceName != null) {
					req.getSession().setAttribute(
							"service",
							this.configContext.getAxisConfiguration()
									.getService(serviceName));
				}
			} else {
				req
						.setAttribute(
								"status",
								"Service "
										+ serviceName
										+ " is not an active service"
										+ ". \n Only parameters of active services can be edited.");
			}
		}

		renderView("ServiceParaEdit.jsp", req, res);
	}

	public void processEngagingGlobally(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		Map modules = this.configContext.getAxisConfiguration().getModules();

		req.getSession().setAttribute("modulemap", modules);

		String moduleName = req.getParameter("modules");

		req.getSession().setAttribute("engagestatus", null);

		if (moduleName != null) {
			try {
				this.configContext.getAxisConfiguration().engageModule(
						moduleName);
				req.getSession().setAttribute("engagestatus",
						moduleName + " module engaged globally successfully");
			} catch (AxisFault axisFault) {
				req.getSession().setAttribute("engagestatus",
						axisFault.getMessage());
			}
		}

		req.getSession().setAttribute("modules", null);
		renderView("engagingglobally.jsp", req, res);
	}

	public void processListOperations(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		Map modules = this.configContext.getAxisConfiguration().getModules();

		req.getSession().setAttribute("modulemap", modules);

		String moduleName = req.getParameter("modules");

		req.getSession().setAttribute("engagestatus", null);
		req.getSession().setAttribute("modules", null);

		String serviceName = req.getParameter("axisService");

		if (serviceName != null)
			req.getSession().setAttribute("service", serviceName);
		else {
			serviceName = (String) req.getSession().getAttribute("service");
		}

		req.getSession().setAttribute(
				"operationmap",
				this.configContext.getAxisConfiguration().getService(
						serviceName).getOperations());

		req.getSession().setAttribute("engagestatus", null);

		String operationName = req.getParameter("axisOperation");

		if ((serviceName != null) && (moduleName != null)
				&& (operationName != null)) {
			try {
				AxisOperation od = this.configContext.getAxisConfiguration()
						.getService(serviceName).getOperation(
								new QName(operationName));

				od.engageModule(this.configContext.getAxisConfiguration()
						.getModule(moduleName));

				req
						.getSession()
						.setAttribute(
								"engagestatus",
								moduleName
										+ " module engaged to the operation successfully");
			} catch (AxisFault axisFault) {
				req.getSession().setAttribute("engagestatus",
						axisFault.getMessage());
			}
		}

		req.getSession().setAttribute("operation", null);
		renderView("engagingtoanoperation.jsp", req, res);
	}

	public void processEngageToService(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		Map modules = this.configContext.getAxisConfiguration().getModules();

		req.getSession().setAttribute("modulemap", modules);
		populateSessionInformation(req);

		String moduleName = req.getParameter("modules");

		req.getSession().setAttribute("engagestatus", null);
		req.getSession().setAttribute("modules", null);

		String serviceName = req.getParameter("axisService");

		req.getSession().setAttribute("engagestatus", null);

		if ((serviceName != null) && (moduleName != null)) {
			try {
				this.configContext.getAxisConfiguration().getService(
						serviceName).engageModule(
						this.configContext.getAxisConfiguration().getModule(
								moduleName));

				req
						.getSession()
						.setAttribute(
								"engagestatus",
								moduleName
										+ " module engaged to the service successfully");
			} catch (AxisFault axisFault) {
				req.getSession().setAttribute("engagestatus",
						axisFault.getMessage());
			}
		}

		req.getSession().setAttribute("axisService", null);
		renderView("engagingtoaservice.jsp", req, res);
	}

	public void processEngageToServiceGroup(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		Map modules = this.configContext.getAxisConfiguration().getModules();

		req.getSession().setAttribute("modulemap", modules);

		Iterator services = this.configContext.getAxisConfiguration()
				.getServiceGroups();

		req.getSession().setAttribute("serviceGroupmap", services);

		String moduleName = req.getParameter("modules");

		req.getSession().setAttribute("engagestatus", null);
		req.getSession().setAttribute("modules", null);

		String serviceName = req.getParameter("axisService");

		req.getSession().setAttribute("engagestatus", null);

		if ((serviceName != null) && (moduleName != null)) {
			this.configContext.getAxisConfiguration().getServiceGroup(
					serviceName).engageModule(
					this.configContext.getAxisConfiguration().getModule(
							moduleName));

			req
					.getSession()
					.setAttribute(
							"engagestatus",
							moduleName
									+ " module engaged to the service group successfully");
		}

		req.getSession().setAttribute("axisService", null);
		renderView("EngageToServiceGroup.jsp", req, res);
	}

	public void processLogout(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		req.getSession().invalidate();
		renderView("index.jsp", req, res);
	}

	public void processviewServiceGroupConetxt(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String type = req.getParameter("TYPE");
		String sgID = req.getParameter("ID");
		ServiceGroupContext sgContext = this.configContext
				.getServiceGroupContext(sgID);
		req.getSession().setAttribute("ServiceGroupContext", sgContext);
		req.getSession().setAttribute("TYPE", type);
		req.getSession().setAttribute("ConfigurationContext",
				this.configContext);
		renderView("viewServiceGroupContext.jsp", req, res);
	}

	public void processviewServiceContext(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String type = req.getParameter("TYPE");
		String sgID = req.getParameter("PID");
		String ID = req.getParameter("ID");
		ServiceGroupContext sgContext = this.configContext
				.getServiceGroupContext(sgID);
		if (sgContext != null) {
			AxisService service = sgContext.getDescription().getService(ID);
			ServiceContext serviceContext = sgContext
					.getServiceContext(service);
			req.setAttribute("ServiceContext", serviceContext);
			req.setAttribute("TYPE", type);
		} else {
			req.setAttribute("ServiceContext", null);
			req.setAttribute("TYPE", type);
		}
		renderView("viewServiceContext.jsp", req, res);
	}

	public void processSelectServiceParaEdit(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		populateSessionInformation(req);
		req.getSession().setAttribute("SELECT_SERVICE_TYPE",
				"SERVICE_PARAMETER");
		renderView("SelectService.jsp", req, res);
	}

	public void processListOperation(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		populateSessionInformation(req);
		req.getSession().setAttribute("SELECT_SERVICE_TYPE", "MODULE");

		renderView("SelectService.jsp", req, res);
	}

	public void processActivateService(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		if (req.getParameter("submit") != null) {
			String serviceName = req.getParameter("axisService");
			String turnon = req.getParameter("turnon");
			if ((serviceName != null) && (turnon != null)) {
				this.configContext.getAxisConfiguration().startService(
						serviceName);
			}
		}

		populateSessionInformation(req);
		renderView("ActivateService.jsp", req, res);
	}

	public void processDeactivateService(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		if (req.getParameter("submit") != null) {
			String serviceName = req.getParameter("axisService");
			String turnoff = req.getParameter("turnoff");
			if (serviceName != null) {
				if (turnoff != null) {
					this.configContext.getAxisConfiguration().stopService(
							serviceName);
				}
				populateSessionInformation(req);
			}
		} else {
			populateSessionInformation(req);
		}

		renderView("InActivateService.jsp", req, res);
	}

	public void processViewGlobalHandlers(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		req.getSession().setAttribute("axisconfig",
				this.configContext.getAxisConfiguration());

		renderView("ViewGlobalHandlers.jsp", req, res);
	}

	public void processViewServiceHandlers(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String service = req.getParameter("axisService");

		if (service != null) {
			req.getSession().setAttribute(
					"serviceHandlers",
					this.configContext.getAxisConfiguration().getService(
							service));
		}

		renderView("ViewServiceHandlers.jsp", req, res);
	}

	public void processListPhases(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		PhasesInfo info = this.configContext.getAxisConfiguration()
				.getPhasesInfo();
		req.getSession().setAttribute("phaseList", info);
		renderView("viewphases.jsp", req, res);
	}

	public void processListServiceGroups(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		Iterator serviceGroups = this.configContext.getAxisConfiguration()
				.getServiceGroups();
		populateSessionInformation(req);
		req.getSession().setAttribute("serviceGroupmap", serviceGroups);

		renderView("ListServiceGroup.jsp", req, res);
	}

	public void processListService(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		populateSessionInformation(req);
		req.getSession().setAttribute("errprservicemap",
				this.configContext.getAxisConfiguration().getFaultyServices());

		renderView("listService.jsp", req, res);
	}

	public void processListSingleService(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		req.getSession().setAttribute("Fault", "");
		String serviceName = req.getParameter("serviceName");
		if (serviceName != null) {
			AxisService service = this.configContext.getAxisConfiguration()
					.getService(serviceName);
			req.getSession().setAttribute("singleservice", service);
		}
		renderView("listSingleService.jsp", req, res);
	}

	public void processListContexts(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		req.getSession().setAttribute("config_context", this.configContext);
		renderView("ViewContexts.jsp", req, res);
	}

	public void processglobalModules(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		Collection modules = this.configContext.getAxisConfiguration()
				.getEngagedModules();

		req.getSession().setAttribute("modulemap", modules);

		renderView("globalModules.jsp", req, res);
	}

	public void processListModules(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		Map modules = this.configContext.getAxisConfiguration().getModules();

		req.getSession().setAttribute("modulemap", modules);
		req.getSession().setAttribute("errormodulesmap",
				this.configContext.getAxisConfiguration().getFaultyModules());

		renderView("listModules.jsp", req, res);
	}

	public void processdisengageModule(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String type = req.getParameter("type");
		String serviceName = req.getParameter("serviceName");
		String moduleName = req.getParameter("module");
		AxisConfiguration axisConfiguration = this.configContext
				.getAxisConfiguration();
		AxisService service = axisConfiguration.getService(serviceName);
		AxisModule module = axisConfiguration.getModule(moduleName);
		if (type.equals("operation")) {
			if ((service.isEngaged(module.getName()))
					|| (axisConfiguration.isEngaged(module.getName()))) {
				req
						.getSession()
						.setAttribute(
								"status",
								"Can not disengage module "
										+ moduleName
										+ ". This module is engaged at a higher level.");
			} else {
				String opName = req.getParameter("operation");
				AxisOperation op = service.getOperation(new QName(opName));
				op.disengageModule(module);
				req.getSession().setAttribute(
						"status",
						"Module " + moduleName + " was disengaged from "
								+ "operation " + opName + " in service "
								+ serviceName + ".");
			}

		} else if (axisConfiguration.isEngaged(module.getName())) {
			req.getSession().setAttribute(
					"status",
					"Can not disengage module " + moduleName + ". "
							+ "This module is engaged at a higher level.");
		} else {
			service.disengageModule(axisConfiguration.getModule(moduleName));
			req.getSession().setAttribute(
					"status",
					"Module " + moduleName + " was disengaged from"
							+ " service " + serviceName + ".");
		}

		renderView("disengage.jsp", req, res);
	}

	public void processdeleteService(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		String serviceName = req.getParameter("serviceName");
		AxisConfiguration axisConfiguration = this.configContext
				.getAxisConfiguration();
		if (axisConfiguration.getService(serviceName) != null) {
			axisConfiguration.removeService(serviceName);
			req.getSession().setAttribute(
					"status",
					"Service '" + serviceName
							+ "' has been successfully removed.");
		} else {
			req.getSession().setAttribute(
					"status",
					"Failed to delete service '" + serviceName
							+ "'. Service doesn't exist.");
		}

		renderView("deleteService.jsp", req, res);
	}

	public void processSelectService(HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		populateSessionInformation(req);
		req.getSession().setAttribute("SELECT_SERVICE_TYPE", "VIEW");

		renderView("SelectService.jsp", req, res);
	}

	private boolean authorizationRequired(HttpServletRequest httpServletRequest) {
		return (httpServletRequest.getSession().getAttribute("Logged") == null)
				&& (!httpServletRequest.getRequestURI().endsWith("login"));
	}

	private boolean axisSecurityEnabled() {
		Parameter parameter = this.configContext.getAxisConfiguration()
				.getParameter("disableAdminSecurity");

		return (parameter == null) || (!"true".equals(parameter.getValue()));
	}
}