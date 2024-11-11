/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Fernando Martinez Bautista 1702824
 */
public class registro extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");        
        String titulo = request.getParameter("titulo");
        String nombre = request.getParameter("nombre");
        String psw = request.getParameter("psw");
        String email = request.getParameter("email");
        String dia = request.getParameter("dia");
        String mes = request.getParameter("mes");
        String sexo = request.getParameter("sexo");
        String adulto = request.getParameter("adulto");
        String mytextarea = request.getParameter("mytextarea");
        String nuevotitulo = limpiar(titulo);
        String nuevonombre = limpiar(nombre);
        String nuevopsw = limpiar(psw);
        String nuevoemail = limpiar(email);
        String nuevodia = limpiar(dia);
        String nuevomes = limpiar(mes);
        String nuevosexo = limpiar(sexo);
        String nuevoadulto = limpiar(adulto);
        String nuevomytextarea = limpiar(mytextarea);
        
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet registro</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("Titulo: " + titulo );
            out.println("Nombre: " + nombre );
            out.println("Email: " + email );
            out.println("Cumpleanos: " + dia + "/" + mes  );
            out.println("Sexo: " + sexo );
            out.println("mayor de edad: " + adulto );
            out.println("Info adicional: " + mytextarea );
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

   


    String limpiar (String texto){
        try{
            return
                URLEncoder.encode(texto,"UTF-8");
        }catch(UnsupportedEncodingException e){
            return "";
        }
    }

}