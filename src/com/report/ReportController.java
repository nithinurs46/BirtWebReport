package com.report;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;

public class ReportController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger(ReportController.class.getName());

	private static Properties configProps = null;
	private final static String configFile = "BirtConfig.properties";
	private final static String className = "ReportController";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReportController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 *      protected void doGet(HttpServletRequest request, HttpServletResponse
	 *      response) throws ServletException, IOException { // TODO Auto-generated
	 *      method stub }
	 * 
	 *      /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.setLevel(Level.ALL);
		logger.addHandler(new ConsoleHandler());
		// TODO Auto-generated method stub
		logger.entering(className, "doPost");
		loadConfigProps();
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String deptName = request.getParameter("deptName");
		String location = request.getParameter("location");
		String reportFormat = request.getParameter("reportFormat");
		logger.info("firstName: " + firstName);
		logger.info("lastName: " + lastName);
		logger.info("deptName: " + deptName);
		logger.info("location: " + location);
		logger.info("reportFormat: " + reportFormat);

		firstName = initArgs("firstName", firstName);
		lastName = initArgs("lastName", lastName);
		deptName = initArgs("deptName", deptName);
		location = initArgs("location", location);

		// get report name and launch the engine
		setContentType(response, reportFormat);

		ServletContext sc = request.getSession().getServletContext();
		this.birtReportEngine = BirtEngine.getBirtEngine(sc);

		// setup image directory
		RenderOption renderOption = new RenderOption();
		Map<Object, Object> contextMap = new HashMap<Object, Object>();
		contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderOption);

		IReportRunnable design;
		ServletOutputStream out = null;
		try {
			// Open report design
			design = birtReportEngine.openReportDesign(sc.getRealPath("/Reports") + "/" + "sample.rptdesign");
			// create task to run and render report
			IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);

			/*
			 * If multiple db's are required, add all the reqd db properties to map, as
			 * shown below
			 */
			contextMap.put("user.conn1", configProps.getProperty("DB_Username1"));
			contextMap.put("pwd.conn1", configProps.getProperty("DB_Password1"));
			contextMap.put("url.conn1", configProps.getProperty("DB_URL1"));
			contextMap.put("driver.conn1", "oracle.jdbc.OracleDriver");

			contextMap.put("user.conn2", configProps.getProperty("DB_Username2"));
			contextMap.put("pwd.conn2", configProps.getProperty("DB_Password2"));
			contextMap.put("url.conn2", configProps.getProperty("DB_URL2"));
			contextMap.put("driver.conn2", "oracle.jdbc.OracleDriver");

			/*
			 * If only one db connection is reqd, then comment above lines where we are
			 * setting db props, uncomment below line and make necessary changes in Birt
			 * template to remove connection binding
			 */
			// contextMap.put("OdaJDBCDriverPassInConnection",
			// getJDBCConnection(url,user,pwd));
			contextMap.put("OdaJDBCDriverPassInConnectionCloseAfterUse", true);
			task.setAppContext(contextMap);
			// set output options
			IGetParameterDefinitionTask parameterDefinitionTask = birtReportEngine
					.createGetParameterDefinitionTask(design);
			parameterDefinitionTask.evaluateDefaults();
			HashMap<String, String> params = parameterDefinitionTask.getDefaultValues();
			params.put("first_name", firstName);
			params.put("last_name", lastName);
			params.put("dept_name", deptName);
			params.put("location", location);
			task.setParameterValues(params);
			// options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
			renderOption.setOutputFormat(reportFormat);
			ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
			renderOption.setOutputStream(baoStream);
			task.setRenderOption(renderOption);
			// run report
			task.run();
			task.close();
			out = response.getOutputStream();
			byte[] reportData = baoStream.toByteArray();
			out.write(reportData, 0, reportData.length);
			// baoStream.writeTo(out);
			// out.flush();
			logger.exiting(className, "doPost");
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
			throw new ServletException(e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}

	}

	private static void loadConfigProps() {
		final String methodName = "loadConfigProps";
		logger.entering(className, methodName);
		configProps = new Properties();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try (InputStream in = cl.getResourceAsStream(configFile)) {
			configProps.load(in);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}

		logger.exiting(className, methodName);
	}

	private static final String initArgs(String column, String value) {
		logger.entering(className, "initArgs");
		if (value == null || value == "") {
			value = "%";
		}
		logger.exiting(className, "initArgs", column + " :: value :: " + value);
		return value;
	}

	private void setContentType(HttpServletResponse response, String reportFormat) throws UnsupportedEncodingException {
		String methodName = "setContentType";
		logger.entering(className, methodName);
		String fileName = "Birt_Sample";
		if (reportFormat.equalsIgnoreCase("pdf")) {
			fileName = fileName + ".pdf";
			response.setContentType("application/pdf;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		} else if (reportFormat.equalsIgnoreCase("doc")) {
			fileName = fileName + ".doc";
			response.setContentType("application/msword;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		} else if (reportFormat.equalsIgnoreCase("xls")) {
			fileName = fileName + ".xls";
			response.setContentType("application/msexcel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		} else {
			response.setContentType("text/plain;charset=UTF-8");
		}
		logger.exiting(className, methodName);
	}

	private Connection getJDBCConnection(String url, String user, String pwd) {
		logger.entering(className, "getJDBCConnection");
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			// Class.forName("org.eclipse.birt.report.data.oda.jdbc.JDBCDriverManager");
			logger.info("url: " + url);
			logger.info("user: " + user);
			logger.info("pwd: " + pwd);
			con = DriverManager.getConnection(url, user, pwd);

		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
		logger.exiting(className, "getJDBCConnection");
		return con;

	}

}
