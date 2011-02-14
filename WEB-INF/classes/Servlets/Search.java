/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Servlets;

import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Carlos Flores
 * @author Rodrigo Sepulveda
 * @author Isaac Larraguibel
 * @author Gabriel Chavez
 *
 */
public class Search extends HttpServlet {
      private Statement statement = null;
      private Connection connection = null;
      private String URL = "jdbc:mysql://localhost/tekno"; /*Insert database adress after //*/
		private ArrayList queryResultArray = new ArrayList();
   

       public void init( ServletConfig config ) throws ServletException{
         super.init( config ); 
         try {
            Class.forName( "com.mysql.jdbc.Driver" );
            connection = DriverManager.getConnection( URL, "root", "" );
         }
             catch ( Exception e ) { 
               e.printStackTrace(); 
               connection = null;
            }
      
      }   


    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
		  try{
			statement = connection.createStatement(); /*Initiates connection statement*/
		
			String query = "SELECT login FROM usuario where login = '" + request.getParameter("search") + "'";
		        ResultSet rs = statement.executeQuery(query);
			
			String queryResult = "";
			while(rs.next()){
				queryResult += rs.getString("login");		/*Search parameter 1*/
// 				queryResult += rs.getString("") + "^";		/*Search parameter 2*/
// 				queryResult += rs.getString("") + "^";		/*Search parameter 3*/
// 				queryResult += rs.getString("") + "^";		/*Search parameter 4*/
				/*...*/
				/*queryResult += rs.getString("") + "^";*/	/*Search parameter n*/
				queryResult += "\n"; /*Separates each result in a different line*/
				}
			out.println(queryResult);
			}catch(Exception e){}

	// 			out.println("Hola!!");
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
