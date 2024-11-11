<%-- 
    Document   : login
    Created on : 19/09/2017, 02:04:14 AM
    Author     : fernando martinez bautista 1702824
--%>
<%
    if(session.getAttribute("usuario")!= null)
        response.sendRedirect("profile.jsp");
    %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio de sesión</title>
    </head>
    <body>
    <center>
        <h1>Bienvenido</h1>
        <form action="LoginController" method="post" name="login">
            <label>Usuario:  </label><input type="text" name="usuario"><br><br>
            <label>Password:</label><input type="password" name="password"><br><br>
            <br><input type="submit" value="Iniciar sesion">
        </form>
    </center>
    </body>
</html>