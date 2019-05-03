package com.italtel.monitoring.fe.webgui.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SessionManager
 */
@WebServlet("/sm")
public class SessionManager extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SessionManager() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}

	void execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();

		String collapsedParam = request.getParameter("collapsed");
		// String pageParam = request.getParameter("page");

		if (collapsedParam != null) {
			switch (collapsedParam) {
			case "true":
				session.setAttribute("collapseClass", "sidebar-collapse");
				session.setAttribute("sidebarCollapsed", Boolean.TRUE);
				break;
			case "false":
				session.setAttribute("collapseClass", "");
				session.setAttribute("sidebarCollapsed", Boolean.FALSE);
				break;
			case "toggle":
				Boolean sidebarCollapsed = (Boolean) session
						.getAttribute("sidebarCollapsed");
				if (sidebarCollapsed) {
					session.setAttribute("collapseClass", "");
					session.setAttribute("sidebarCollapsed", Boolean.FALSE);
				} else {
					session.setAttribute("collapseClass", "sidebar-collapse");
					session.setAttribute("sidebarCollapsed", Boolean.TRUE);
				}
				break;
			default:
				break;
			}
		}
		// if (pageParam != null) {
		// switch (pageParam) {
		// case "dashboard":
		// case "subnet":
		// case "sip":
		// case "domain":
		// session.setAttribute("page", pageParam);
		// }
		// }

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println("{");
		// out.println("  \"page\": \"" + session.getAttribute("page") + "\",");
		out.println("  \"sidebarCollapsed\": \""
				+ session.getAttribute("sidebarCollapsed") + "\"");
		out.println("  \"collapseClass\": \""
				+ session.getAttribute("collapseClass") + "\"");
		out.println("}");
		out.close();
	}
}
