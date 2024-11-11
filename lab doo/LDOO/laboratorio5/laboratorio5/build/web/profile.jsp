<%-- 
    Document   : profile
    Created on : 1/10/2017, 04:04:00 PM
    Author     : Fernando Martinez
--%>
<%
    if(session.getAttribute("usuario")== null)
        response.sendRedirect("login.jsp");
    %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio de sesion valida</title>
    </head>
    <body>
        <h1>hola <%= session.getAttribute("fullnombre")%></h1>
    </body>
</html>