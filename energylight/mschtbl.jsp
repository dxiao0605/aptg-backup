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
        <script src="js/def/mschtbl.js"></script>
       
    </head>
    <body>
       <%@ include file="include/header.jsp"%>
        <div class="container-fluid">
            <div class="row">
               
               <%@ include file="include/sidemenu.jsp"%>
                          <div class="col" id="main">
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-sm-9">
                                <div class="row mt-2 mb-2">
                                <select class="custom-select device-select col-sm-2 font-weight-bold" id="select_location_L1" onchange="onSelectFloor()"></select> 
                                <select class="custom-select device-select col-sm-1 font-weight-bold ml-2" id="select_location_L2" onchange="onSelectOffice()"></select> 
                                <select class="custom-select device-select col-sm-3 font-weight-bold ml-2" id="select_location_L3" onchange="onShowLight()"></select></div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="table-group col-md-9" id="tables">
                                <table id="example" class="display table" style="width:100%"></table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>