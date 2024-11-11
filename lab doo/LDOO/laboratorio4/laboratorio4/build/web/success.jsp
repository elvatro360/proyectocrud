<%-- 
    Document   : succes
    Created on : 23/09/2017, 08:49:49 AM
    Author     : hifum
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio de sesion valida</title>
    </head>
    <body>
        <h1>Inicio de sesion valida</h1>
        <h2>hola <%= request.getAttribute("username")%> </h2>
    </body>
</html>
