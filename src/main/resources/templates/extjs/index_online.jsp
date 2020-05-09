<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
   <head>
      <link rel = "stylesheet" type ="text/css" href= "https://cdnjs.cloudflare.com/ajax/libs/extjs/6.0.0/classic/theme-crisp/resources/theme-crisp-all.css" />
      <script type ="text/javascript" src = "https://cdnjs.cloudflare.com/ajax/libs/extjs/6.0.0/ext-all.js" > </script>
   </head>
  
   <body>
      <div id="helloWorldPanel"></div>
   </body>
   
    <script type="text/javascript">
         Ext.onReady(function() {
         Ext.create('Ext.Panel', {
            renderTo: 'helloWorldPanel',
            height: 100,
            width: 200,
            title: 'Hello world',
            html: 'First Ext JS Hello World Program'
            });
         });
      </script>
</html>
