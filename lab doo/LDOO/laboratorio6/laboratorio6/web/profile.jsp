<%-- 
    Document   : profile
    Created on : 1/10/2017, 04:04:00 PM
    Author     : Fernando Martinez
--%>
<%
    if(session.getAttribute("usuario")== null)
        response.sendRedirect("login.jsp");
    %>
    
    <%
        String color = "";
        Cookie[] cookies=request.getCookies();
        for (Cookie c : cookies){
            if(c.getName().equals("color")){
                color=c.getValue();
                }
            }
    %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio de sesion valida</title>
    </head>
    <body style="background-color: <%= color %>">
    <center>
        <h1>Hola <%= session.getAttribute("fullnombre")%></h1><br>
        <br><label>Color de perfil</label>
        <br>
        <form action="ProfileController" method="post">
        <select name="color">
                <option selected value="choice">Elije tu color</option>
                <option value="white">Blanco</option>
                <option value="grey">Gris</option>
                <option value="green">Verde</option>
        </select><br>
        <br><input type="submit" value="Guardar">
        </form>
        <br><a href="LogoutController">Cerrar sesion </a>
        
    </center>
    </body>
</html>