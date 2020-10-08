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

import scot.ianmacdonald.cakemgr.webapp.model.CakeDao;
import scot.ianmacdonald.cakemgr.webapp.model.CakeDaoConstraintViolationException;
import scot.ianmacdonald.cakemgr.webapp.model.CakeDaoFactory;
import scot.ianmacdonald.cakemgr.webapp.model.CakeEntity;
import scot.ianmacdonald.cakemgr.webapp.model.PojoJsonConverter;

/**
 * Servlet class implementing controller functions for RESTful json-based
 * microservice at /cakes context and Java web application at / context.
 * 
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
@WebServlet(urlPatterns = { "/cakes", "/" })
public class CakeServlet extends HttpServlet {

	/**
	 * Generated serialVersionUID for identification of class
	 */
	private static final long serialVersionUID = -8600450627971383964L;

	/**
	 * Handle the read operations of the RESTful service and webapp using the http
	 * GET method, as per REST principles
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Functions common to RESTful service and webapp
		// Get a reference to a CakeDap for accessing the DB
		CakeDao cakeDao = CakeDaoFactory.getCakeDao();

		// read all the cakes in the DB
		List<CakeEntity> list = cakeDao.readAll();

		if (request.getRequestURI().equals("/cakes")) {

			// RESTful service functions
			response.getWriter().print(PojoJsonConverter.pojoListToJson(list));

		}

		else if (request.getRequestURI().equals("/")) {

			// webapp functions
			// update the list of cakes in the request for rendering by the JSP
			request.setAttribute("cakeList", list);

			// forward the request/response to the display
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		}
	}

	/**
	 * Handle the create operations of the RESTful service and webapp using the http
	 * POST method, as per REST principles
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Functions common to RESTful service and webapp
		// Get a reference to a CakeDao for accessing the DB
		CakeDao cakeDao = CakeDaoFactory.getCakeDao();

		if (request.getRequestURI().equals("/cakes")) {

			// RESTful service functions
			// read the JSON for the new cake object from the request body
			final StringBuffer cakeJsonBuffer = new StringBuffer();
			String line = null;
			try {

				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					cakeJsonBuffer.append(line);

			} catch (Exception e) {

				// set the status code for the response to 500 (INTERNAL SERVER ERROR)
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				// no further processing is possible, so return the response
				return;
			}

			// create a CakeEntity from the JSON
			final CakeEntity cakeEntity = PojoJsonConverter.jsonToPojo(cakeJsonBuffer.toString(), CakeEntity.class);

			// create the cake in the DB
			CakeEntity savedCakeEntity = null;
			try {

				savedCakeEntity = cakeDao.create(cakeEntity);

			} catch (CakeDaoConstraintViolationException ex) {

				// generate a message saying the cake already exists
				final CakeServletMessage cakeServletMessage = new CakeServletMessage(
						"A Cake with the title [" + cakeEntity.getTitle() + "] already exists in the DB.");
				// send the message via the http response
				response.getWriter().print(PojoJsonConverter.pojoToJson(cakeServletMessage));
				// set the status code for the response to 403 (FORBIDDEN)
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				// no further processing is possible, so return the response
				return;
			}

			// set the status code for the response to 201 (CREATED)
			response.setStatus(HttpServletResponse.SC_CREATED);
			// output the DB content back to the client in the http response
			response.getWriter().print(PojoJsonConverter.pojoToJson(savedCakeEntity));

		} else if (request.getRequestURI().equals("/")) {

			// webapp functions
			// marshall the parameters for the new CakeEntity from the request
			final String title = request.getParameter("title");
			final String desc = request.getParameter("desc");
			final String image = request.getParameter("image");

			// create the new CakeEntity object
			final CakeEntity cakeEntity = new CakeEntity(null, title, desc, image);

			// create the cake in the DB
			try {

				cakeDao.create(cakeEntity);

			} catch (CakeDaoConstraintViolationException ex) {
				
				// generate a message saying the cake already exists
				final CakeServletMessage cakeServletMessage = new CakeServletMessage(
						"A Cake with the title [" + cakeEntity.getTitle() + "] already exists in the DB.");
				// associate the message with the http request
				request.setAttribute("errorMessage", cakeServletMessage);
				
			}

			// update the list of cakes in the request for rendering by the JSP
			final List<CakeEntity> list = cakeDao.readAll();
			request.setAttribute("cakeList", list);

			// forward the request/response to the display
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		}
	}

}
