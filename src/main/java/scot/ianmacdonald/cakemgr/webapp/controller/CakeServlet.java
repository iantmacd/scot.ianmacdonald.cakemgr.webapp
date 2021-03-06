package scot.ianmacdonald.cakemgr.webapp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
import scot.ianmacdonald.cakemgr.webapp.model.PojoJsonConverterException;

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
		// Get a reference to a CakeDao for accessing the DB
		CakeDao cakeDao = CakeDaoFactory.getCakeDao();

		// read all the cakes in the DB
		List<CakeEntity> list = cakeDao.readAll();

		if (request.getRequestURI().equals("/cakes")) {

			// RESTful service functions
			// set the Content-Type response header
			response.setHeader("Content-Type", "application/json");
			
			try {

				response.getWriter().print(PojoJsonConverter.pojoListToJson(list));

			} catch (PojoJsonConverterException pjce) {

				// could only happen if the objects retrieved from the DB could not be parsed
				// to JSON, which would be an internal server error condition so
				// set the status code for the response to 500 (INTERNAL SERVER ERROR)
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				// no further processing is possible here, so return the response
				return;
			}

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
	 * 
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Functions common to RESTful service and webapp
		// Get a reference to a CakeDao for accessing the DB
		CakeDao cakeDao = CakeDaoFactory.getCakeDao();

		if (request.getRequestURI().equals("/cakes")) {

			// RESTful service functions
			// set the Content-Type response header
			response.setHeader("Content-Type", "application/json");
			
			// read the JSON for the new cake object from the request body
			String putCakeJsonRequest = null;
			try {
				
				putCakeJsonRequest = new BufferedReader(request.getReader()).lines().collect(Collectors.joining("\n"));
				
			} catch (Exception e) {

				// fatal error while trying to read the request content
				// set the status code for the response to 500 (INTERNAL SERVER ERROR)
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				// send the JSON error message via the http response
				CakeExceptionMessage cakeExceptionMessage = generateCakeExceptionMessage(e);
				response.getWriter().print(PojoJsonConverter.pojoToJson(cakeExceptionMessage));
				return;
			}

			// create a CakeEntity from the JSON
			CakeEntity cakeEntity = null;
			try {

				cakeEntity = PojoJsonConverter.jsonToPojo(putCakeJsonRequest, CakeEntity.class);

			} catch (PojoJsonConverterException pjce) {

				// failure to convert JSON for a cake object into a CakeEntity object probably
				// due to a malformed request from the client
				// Set the status code for the response to 400 (BAD REQUEST)
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				// send the JSON error message via the http response
				CakeExceptionMessage cakeExceptionMessage = generateCakeExceptionMessage(pjce);
				response.getWriter().print(PojoJsonConverter.pojoToJson(cakeExceptionMessage));
				return;
			}

			// create the cake in the DB
			CakeEntity savedCakeEntity = null;
			try {

				savedCakeEntity = cakeDao.create(cakeEntity);

			} catch (CakeDaoConstraintViolationException ex) {

				// generate a message saying the cake already exists
				final CakeExceptionMessage cakeExceptionMessage = generateCakeExceptionMessage(ex);
				// send the message via the http response
				response.getWriter().print(PojoJsonConverter.pojoToJson(cakeExceptionMessage));
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
				final CakeExceptionMessage cakeServletMessage = generateCakeExceptionMessage(ex);
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

	/*
	 * Generate a CakeExceptionMessage object using a message and the caught
	 * throwable
	 */
	private CakeExceptionMessage generateCakeExceptionMessage(final Throwable throwable) {

		final CakeExceptionMessage cakeServletMessage = new CakeExceptionMessage(throwable.getMessage(),
				throwable.getClass().getName(), throwable.getCause().getClass().getName(),
				throwable.getCause().getMessage());
		return cakeServletMessage;
	}

}
