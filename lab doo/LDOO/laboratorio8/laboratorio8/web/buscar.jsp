<%-- 
    Document   : buscar
    Created on : 01/10/2017, 09:00:27 PM
    Author     : hifum
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="modelos.ComentariosPOJO"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Buscar comentarios</title>
    </head>
    <body>
    <center>
        <h1 align="center">Buscar</h1>
        <form action = "ComentariosControlador" method = "post">
           <br><label>Nombre:</label><input type = "text" name = "name"><br>
           <br><label>Comentario:</label><textarea rows="4" cols="30" name="comentario"></textarea><br>
           <br><input type ="submit" value ="buscar"> 
           <br><input type="hidden" name="action" value="buscar"><br>
        </form>
        <%
            if(session != null){
                ArrayList  comentarios = (ArrayList)session.getAttribute("comentarios");
                if(comentarios != null){
        %>
        <table border="1"> 
            <tr>
                <th>Nombre</th>
                <th>Comentario</th>
            </tr> 
            <%
                for(Object o: comentarios){
                    ComentariosPOJO coments = (ComentariosPOJO) o;
            %>
             <tr>
                 <td><%=coments.getNombre()%></td>
                 <td><%=coments.getComentario()%></td>
             </tr>  
             <%}%>
        </table> 
        <% }
        }
        %>
        </center>
    </body>
</html>
