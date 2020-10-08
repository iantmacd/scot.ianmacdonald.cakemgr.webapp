package scot.ianmacdonald.cakemgr.webapp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import scot.ianmacdonald.cakemgr.webapp.model.CakeDAO;
import scot.ianmacdonald.cakemgr.webapp.model.CakeDAOFactory;
import scot.ianmacdonald.cakemgr.webapp.model.CakeEntity;
import scot.ianmacdonald.cakemgr.webapp.model.PojoJsonConverter;

@WebServlet(urlPatterns = { "/cakes", "/" })
public class CakeServlet extends HttpServlet {

	/**
	 * Generated serialVersionUID for identification of class
	 */
	private static final long serialVersionUID = -8600450627971383964L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * Obtain a reference to a CakeDAO Interface TODO: improve Exception handling
		 * (generally)
		 */
		CakeDAO cakeDAO = CakeDAOFactory.getCakeDAO();

		// read all the cakes in the DB
		List<CakeEntity> list = cakeDAO.readAll();

		if (request.getRequestURI().equals("/cakes")) {

			response.getWriter().print(PojoJsonConverter.pojoListToJson(list));

		} else if (request.getRequestURI().equals("/")) {

			// update the list of cakes in the request for rendering by the JSP
			request.setAttribute("cakeList", list);

			// forward the request/response to the display
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * Obtain a reference to a CakeDAO Interface TODO: improve Exception handling
		 * (generally)
		 */
		CakeDAO cakeDAO = CakeDAOFactory.getCakeDAO();

		if (request.getRequestURI().equals("/cakes")) {

			// read the JSON for the new cake object from the request body
			final StringBuffer cakeJsonBuffer = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					cakeJsonBuffer.append(line);
			} catch (Exception e) {
				// TODO: Implement proper Exception handing accross application
			}

			// create a CakeEntity from the JSON
			final CakeEntity cakeEntity = PojoJsonConverter.jsonToPojo(cakeJsonBuffer.toString(), CakeEntity.class);

			// create the cake in the DB
			final CakeEntity savedCakeEntity = cakeDAO.create(cakeEntity);

			// output the DB content back to the client in the http response
			response.getWriter().print(PojoJsonConverter.pojoToJson(savedCakeEntity));

		} else if (request.getRequestURI().equals("/")) {

			// marshall the parameters for the new CakeEntity from the request
			final String title = request.getParameter("title");
			final String desc = request.getParameter("desc");
			final String image = request.getParameter("image");

			// create the new CakeEntity object
			final CakeEntity cakeEntity = new CakeEntity(null, title, desc, image);

			// create the cake in the DB
			cakeDAO.create(cakeEntity);

			// update the list of cakes in the request for rendering by the JSP
			final List<CakeEntity> list = cakeDAO.readAll();
			request.setAttribute("cakeList", list);

			// forward the request/response to the display
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		}
	}

}
