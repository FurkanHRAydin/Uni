<%-- 
    Document   : BmiRechner.jsp
    Created on : 09.05.2022, 14:04:47
    Author     : fugy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        Sie sind <%= request.getParameter("age")%> Jahre alt <br>
        
        Sie sind <%= request.getParameter("height")%> cm groß <br>
        
        Sie sind <%= request.getParameter("weight")%> schwer <br>
        
        <br>
        
        <%
         int age = Integer.parseInt(request.getParameter("age"));
         int height = Integer.parseInt(request.getParameter("height"));
         int weight = Integer.parseInt(request.getParameter("weight"));
         
         double bmi = (double)weight/( ((double)height/100)*((double)height/100) );
         %>
         
         Ihr BMI ist: <%=bmi%>
         
         <%
         
         if(age <= 24) {
         
           if(bmi<19) {
            out.println("Sie sind untergewichtig");
           }
           
           else if (bmi>19 && bmi<=25) {
            out.println("Sie sind normalgewichtigt");
           }
           else {
            out.println("Sie sind fett");
             }
         }
         
         
         if(age >24 && age <34) {
         
           if(bmi<20) {
            out.println("Sie sind untergewichtig");
           }
           
           else if (bmi>19 && bmi<=26) {
            out.println("Sie sind normalgewichtigt");
           }
           else {
            out.println("Sie sind fett");
             }
         }
         

         if(age >34 &&age <= 45) {
         
           if(bmi<21) {
            out.println("Sie sind untergewichtig");
           }
           
           else if (bmi>21 && bmi<=28) {
            out.println("Sie sind normalgewichtigt");
           }
           else {
            out.println("Sie sind fett");
             }
         }
        %>
         
    
    </body>
</html>
