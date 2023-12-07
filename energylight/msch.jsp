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
        <link rel="stylesheet" href="css/pub/bootstrap-datepicker.css" />
        <link rel="stylesheet" href="css/pub/bootstrap.min.css" />
        <!-- <link rel="stylesheet" href="font-awesome.min.css" /> -->
        
        <link rel="stylesheet" href="css/pub/all.css" />
        <link rel="stylesheet" href="css/pub/jquery.dataTables.min.css" />
        <link rel="stylesheet" href="css/pub/pretty-checkbox.min.css"  />
        
        
        <link rel="stylesheet" href="css/def/msch.css" />
        <link rel="stylesheet" href="css/def/common.css" />
        
        <script src="js/pub/jquery.min.js"></script>
        <script src="js/pub/bootstrap.min.js"></script>
        <script src="js/pub/jquery.dataTables.min.js"></script>
       
        <script src="js/pub/bootstrap-datepicker.min.js"></script>   
        <script src="js/pub/bootstrap-datepicker.zh-TW.min.js"></script>
        <script src="js/pub/jsonpath-0.8.0.js"></script>
        
        
        <script src="js/def/common.js"></script>
        <script src="js/def/msch.js"></script>
        
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
                               
                                <select class="custom-select col-sm-2 font-weight-bold ml-2" id="select_location_L3" onchange="onShowiTouch(this.id)"></select>
                               
                                <select class="custom-select col-sm-2 font-weight-bold ml-2" id="select_location_L4" onchange="onShowScreen(this.id)"></select>
                           
                                <select class="custom-select col-sm-2 font-weight-bold" id="select_sreen"></select>
                           
                            </div>
                        </div>
                        
						
						
                            <div class="row mt-4">
                                <div class='col-sm-7'>
                                   
                                   
                                   <div class="schset " id="schset">
                                    <div class="pretty p-default p-curve">
                                        <input type="radio" name="schType" value="0"/>
                                        <div class="state p-primary-o">
                                            <label>立即執行</label>
                                        </div>
                                    </div>

								<div class="form-inline mt-3">
									<div class="pretty p-default p-curve">
										<input type="radio" name="schType" value="1"/>
										<div class="state p-primary-o">
											<label>單次執行</label>
										</div>
									</div>
									
										<div class="form-group">
											<div class="input-group date">
												<input type="text" class="form-control dp_input" placeholder="請輸入日期" id="dp_onetime">
												<div class="input-group-prepend">
													<button class="btn btn-primary" type="button">
														<i class='fas fa-calendar-alt'></i>
													</button>
												</div>

											</div>
										</div>
									

									<div class="timeset form-inline ml-3">

										<div class="hourset">
											<select class="custom-select col-sm-12 font-weight-bold" id="onetime_hour"> 	</select>
										</div>
										<div class="h6 mx-2">時</div>
										<div class="minset">
											<select class="custom-select col-sm-12 font-weight-bold" id="onetime_min"></select>
										</div>
										<div class="h6 mx-2">分</div>
									</div>

								</div>

								<div class="form-inline mt-3">
									<div class="pretty p-default p-curve">
										<input type="radio" name="schType" value="9"/>
										<div class="state p-primary-o">
											<label>周期執行</label>
										</div>
									</div>
								</div>
								</div>

                             <div class="routineset " id="routineset">

								<div class="row mt-3 ml-5">
                                    <div class='col-sm-12'>
										<div class="form-inline mt-3">
											<div class="pretty p-default p-curve">
												<input type="radio" name="routine" value="2" />
												<div class="state p-primary-o">
													<label>每天執行</label>
												</div>
											</div>

											<div class="timeset form-inline ml-3">

												<div class="hourset">
													<select class="custom-select col-sm-12 font-weight-bold" id="daily_hour">
													</select>
												</div>
												<div class="h6 mx-2">時</div>
												<div class="minset">
													<select class="custom-select col-sm-12 font-weight-bold" id="daily_min"></select>
												</div>
												<div class="h6 mx-2">分</div>
											</div>



										</div>
										<div class="form-inline mt-3">
											<div class="pretty p-default p-curve">
												<input type="radio" name="routine" value="3"/>
												<div class="state p-primary-o">
													<label>每周執行</label>
												</div>
											</div>

											<div class="timeset form-inline ml-3 mt-2">

												<div class="hourset">
													<select class="custom-select col-sm-12 font-weight-bold" id="weekly_hour">
													</select>
												</div>
												<div class="h6 mx-2">時</div>
												<div class="minset">
													<select class="custom-select col-sm-12 font-weight-bold" id="weekly_min"></select>
												</div>
												<div class="h6 mx-2">分</div>
											</div>


										</div>

										<div class="form-inline ml-5 mt-4">
                                            <div class="pretty p-default">
                                                <input type="checkbox" name="week"  value ="1"/>
                                                <div class="state p-success">
                                                    <label>星期一</label>
                                                </div>
                                            </div>
                                            <div class="pretty p-default">
                                                <input type="checkbox" name="week"  value ="2"/>
                                                <div class="state p-success">
                                                    <label>星期二</label>
                                                </div>
                                            </div>
                                            <div class="pretty p-default">
                                                <input type="checkbox" name="week"  value ="3"/>
                                                <div class="state p-success">
                                                    <label>星期三</label>
                                                </div>
                                            </div>
                                            <div class="pretty p-default">
                                                <input type="checkbox" name="week"  value ="4"/>
                                                <div class="state p-success">
                                                    <label>星期四</label>
                                                </div>
                                            </div>
                                            <div class="pretty p-default">
                                                <input type="checkbox" name="week"  value ="5"/>
                                                <div class="state p-success">
                                                    <label>星期五</label>
                                                </div>
                                            </div>
                                            <div class="pretty p-default">
                                                <input type="checkbox" name="week"  value ="6"/>
                                                <div class="state p-success">
                                                    <label>星期六</label>
                                                </div>
                                            </div>
                                            <div class="pretty p-default">
                                                <input type="checkbox" name="week"  value ="7"/>
                                                <div class="state p-success">
                                                    <label>星期日</label>
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <div class="form-inline mt-5">
                                            <div class="pretty p-default p-curve">
                                                <input type="radio" name="routine" value="4" />
                                                <div class="state p-primary-o">
                                                    <label>每月執行</label>
                                                </div>
                                            </div>
                                            
                                            <div class="timeset form-inline ml-3 mt-2">

												<div class="hourset">
													<select class="custom-select col-sm-12 font-weight-bold" id="monthly_hour">
													</select>
												</div>
												<div class="h6 mx-2">時</div>
												<div class="minset">
													<select class="custom-select col-sm-12 font-weight-bold" id="monthly_min"></select>
												</div>
												<div class="h6 mx-2">分</div>
											</div>
                                              
                                        </div>
                                        <div class="form-inline ml-5 mt-3" id="days">
                                            <div class="pretty p-default day">
                                                <input type="checkbox" />
                                                <div class="state p-success">
                                                    <label>1日</label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-inline mt-5">
                                           


											<div class="form-group">
												<div class="input-group date">
													<input type="text" class="form-control dp_input" placeholder="起始日" id="dp_routine_st">
													<div class="input-group-prepend">
														<button class="btn btn-primary" type="button">
															<i class='fas fa-calendar-alt'></i>
														</button>
													</div>

												</div>
											</div>


											<div class="form-group ml-5">
												<div class="input-group date">
													<input type="text" class="form-control dp_input" placeholder="到期日" id="dp_routine_end">
													<div class="input-group-prepend">
														<button class="btn btn-primary" type="button">
															<i class='fas fa-calendar-alt'></i>
														</button>
													</div>

												</div>
											</div>


											



										</div>
                                        </div>
                                    </div>


								


							</div>
								
								
								
								<div class="form-inline ml-5 mt-5 schset" id="submit-form">
									<button type="button" class="btn btn-success btn-lg btn-block" id="SendSch">送出</button>
									
								</div>
								
								
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>