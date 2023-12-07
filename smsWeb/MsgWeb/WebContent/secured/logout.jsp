<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

  <%--test the condition you need--%>

  <c:set var="ctx" value="${pageContext.request.requestURL}" />
  
  
  
  <c:choose>
    <c:when test = "${fn:containsIgnoreCase(ctx, 'localhost')}">
     <% request.getSession(false).invalidate(); %>
    <% response.sendRedirect("https://auth.aptg.com.tw/cas/logout?service=http://localhost:8080/MsgWeb/"); %>
    </c:when>
    <c:otherwise>
     <% request.getSession(false).invalidate(); %>
      <% response.sendRedirect("https://auth.aptg.com.tw/cas/logout?service=http://webmsg.aptg.com.tw/MsgWeb/"); %>
    </c:otherwise>
  </c:choose>





 
 