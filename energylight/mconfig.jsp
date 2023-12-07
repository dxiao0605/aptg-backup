<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
    <head>
        <meta name="generator" content="HTML Tidy for HTML5 for Windows version 5.6.0" />
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>亞太電信-能源平台</title>
        <link rel="stylesheet" href="css/pub/bootstrap-datepicker.css" />
        <link rel="stylesheet" href="css/pub/bootstrap.min.css" />
        <!-- <link rel="stylesheet" href="font-awesome.min.css" /> -->
        
        <link rel="stylesheet" href="css/pub/all.css" />
        <link href="css/pub/bootstrap-slider.css" rel="stylesheet" />
        <link rel="stylesheet" href="css/pub/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="css/pub/pretty-checkbox.min.css"  />
        
        
        <link rel="stylesheet" href="css/def/main.css" />
        <link rel="stylesheet" href="css/def/common.css" />
        
        <script src="js/pub/jquery.min.js"></script>
        <script src="js/pub/bootstrap.min.js"></script>
        <script src="js/pub/bootstrap-slider.js"></script>
        <script src="js/pub/jquery.dataTables.min.js"></script>
       
        <script src="js/pub/bootstrap-datepicker.min.js"></script>   
        <script src="js/pub/bootstrap-datepicker.zh-TW.min.js"></script>
        <script src="js/pub/jsonpath-0.8.0.js"></script>
        
        
        <script src="js/def/common.js"></script>
        <script src="js/def/mconfig.js"></script>
        
    </head>
    <body>
        <%@ include file="include/header.jsp"%>
        <div class="container-fluid">
            <div class="row">
         
         <%@ include file="include/sidemenu.jsp"%>
                         <div class="col" id="main">
                    <div class="container-fluid">
                        <div class="row mt-4">
                            <div class="col-sm-12 ">
                                
                                <select class="custom-select col-sm-2 font-weight-bold" id="select_location_L1" onchange="onShowSelect(this.id)"></select> 
                               
                                <select class="custom-select col-sm-2 font-weight-bold ml-2" id="select_location_L2" onchange="onShowSelect(this.id)"></select> 
                               
                                <select class="custom-select col-sm-2 font-weight-bold ml-2" id="select_location_L3" onchange="onShowSelect(this.id)"></select>
                               
                             
                            </div>
                        </div>
                        
						
						
                            <div class="row mt-4">
                            
                          
								<div class="row " id="cards"></div>
							
                            
                            
                                    </div>
                    </div>
                </div>
            </div>
        </div>
        
        <%@ include file="include/setdev.jsp"%>
    </body>
</html>