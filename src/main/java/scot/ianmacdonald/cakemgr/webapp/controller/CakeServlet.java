package scot.ianmacdonald.cakemgr.webapp.controller;

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
		List<CakeEntity> list = cakeDAO.readAll();

		if (request.getRequestURI().equals("/cakes")) {

			response.getWriter().println("[");

			for (CakeEntity entity : list) {
				response.getWriter().println("\t{");

				response.getWriter().println("\t\t\"title\" : " + entity.getTitle() + ", ");
				response.getWriter().println("\t\t\"desc\" : " + entity.getDescription() + ",");
				response.getWriter().println("\t\t\"image\" : " + entity.getImage());

				response.getWriter().println("\t}");
			}

			response.getWriter().println("]");
		} else if (request.getRequestURI().equals("/")) {
			request.setAttribute("cakeList", list);
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

		} else if (request.getRequestURI().equals("/")) {
			
			// marshall the parameters for the new CakeEntity from the request
			final String title = request.getParameter("title");
			final String description = request.getParameter("description");
			final String image = request.getParameter("image");
			
			// create the new CakeEntity object
			final CakeEntity cakeEntity = new CakeEntity();
			cakeEntity.setTitle(title);
			cakeEntity.setDescription(description);
			cakeEntity.setImage(image);
			
			// save the new CakeEntity object to the DB
			cakeDAO.create(cakeEntity);
			
			// update the list of cakes in the request for rendering by the JSP
			final List<CakeEntity> list = cakeDAO.readAll();
			request.setAttribute("cakeList", list);

			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		}
	}

}
