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

@WebServlet(urlPatterns = {"/cakes", "/"})
public class CakeServlet extends HttpServlet {

	/**
	 * Generated serialVersionUID for identification of class
	 */
	private static final long serialVersionUID = -8600450627971383964L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		/*
		 * Obtain a reference to a CakeDAO Interface
		 * TODO: improve Exception handling (generally)
		 */
		CakeDAO cakeDAO = CakeDAOFactory.getCakeDAO();
		List<CakeEntity> list = cakeDAO.readAll();
		
		if (req.getRequestURI().equals("/cakes")) {

			resp.getWriter().println("[");

			for (CakeEntity entity : list) {
				resp.getWriter().println("\t{");

				resp.getWriter().println("\t\t\"title\" : " + entity.getTitle() + ", ");
				resp.getWriter().println("\t\t\"desc\" : " + entity.getDescription() + ",");
				resp.getWriter().println("\t\t\"image\" : " + entity.getImage());

				resp.getWriter().println("\t}");
			}

			resp.getWriter().println("]");
		}
		else if (req.getRequestURI().equals("/")) {
			req.setAttribute("cakeList", list);
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(req, resp);
		}
	}

}
