<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String basePath = request.getContextPath();
	String  path= request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+basePath;
%>
<html>
   <head>
      <link rel = "stylesheet" type ="text/css" href= "<%=path%>/extjs/thems/ext-theme-crisp/resources/ext-theme-crisp-all.css" />
      <script type ="text/javascript" src = "<%=path%>/extjs/js/ext-all.js" > </script>
   </head>
  
   <body>
      <div id="helloWorldPanel"></div>
   </body>
   
    <script type="text/javascript">
         Ext.onReady(function() {
        	 var btn1 = Ext.create('Ext.Button', {
        		    text: '按钮1',
        		    renderTo: Ext.getBody(),
        		    handler: function() {
        		        alert('You clicked the button!');
        		    }
        		});
        	 
        	 var btn2 = Ext.create('Ext.Button', {
	     		    text: '按钮2',
	     		    renderTo: Ext.getBody(),
	     		    handler: function() {
	     		        alert('You clicked the button!');
	     		    }
     			});
        	 
        	 // 北
        	 var northPanel = Ext.create('Ext.Panel', {
 	            renderTo: 'helloWorldPanel',
	            height:250,
	            html: 'First Ext JS Hello World Program',
	            icon: "<%=path%>/img/pic2.jpg",
	            closable:true,
	            disabled:false,
	            title: 'north',
	            region:'north',
	            margin:'1 1 1 1',
	            lbar: [
	            	  { xtype: 'button', text: 'Button 1'},{ xtype: 'button', text: 'Button 2'}
	            	],
	            items:[btn1],
	            bbar: [
	            	  { xtype: 'button', text: 'Button 3' },{ xtype: 'button', text: 'Button 4' }
	            	],
	            buttons:[btn1,btn2]  //底部按钮
	           });
        	 
        	 // 南
        	 var southPanel = Ext.create('Ext.Panel', {
 	            height:150,
 	            title: 'south',
 	            region:'south',
 	            closable:true,
 	            buttons:[btn1,btn2]
         	 });
        	 
        	 // 西
        	 var westPanel = Ext.create('Ext.Panel', {
        		 width:350,
  	            title: 'west',
  	            region:'west',
  	        	  closable:true,
  	          	buttons:[btn1,btn2]
          	 });
        	 
        	 // 东
        	 var eastPanel = Ext.create('Ext.Panel', {
   	            width: 350,
   	            title: 'east',
   	            region:'east',
   	        	 closable:true,
   	      	   buttons:[btn1,btn2]
           	 });
        	 
        	 var centerPanel = Ext.create('Ext.Panel', {
    	            title: 'center',
    	            region:'center',
    	            closable:true,
    	      	   buttons:[btn1,btn2]
            	 });
        	 
        	 Ext.create('Ext.container.Viewport', {
        		    layout: 'border',
        		    items: [northPanel,southPanel,westPanel,eastPanel,centerPanel]
        	 });

         });
      </script>
</html>
