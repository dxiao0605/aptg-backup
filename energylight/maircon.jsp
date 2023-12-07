<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
    <head>
        <meta name="generator" content="HTML Tidy for HTML5 for Windows version 5.6.0" />
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>亞太電信-能源平台</title>
       <link rel="stylesheet" href="css/pub/bootstrap.min.css" />
       <link rel="stylesheet" href="css/pub/all.css" />
       <link rel="stylesheet" href="css/pub/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="css/def/common.css" />
          
          
         <script src="js/pub/jquery.min.js"></script>
         <script src="js/pub/bootstrap.min.js"></script>

         <script src="js/pub/jquery.dataTables.min.js"></script> 
      
        <script src="js/def/common.js"></script>
        <script src="js/def/maircon.js"></script>
       
    </head>
    <body>
       <%@ include file="include/header.jsp"%>
        <div class="container-fluid">
            <div class="row">
               
               <%@ include file="include/sidemenu.jsp"%>
                          <div class="col" id="main">
                    <div class="container-fluid">
                        
                        <div class="row mt-5">
                            <div class="table-group col-md-9" id="tables">
                                <table id="airconTbl" class="cell-border" style="width:100%"></table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>