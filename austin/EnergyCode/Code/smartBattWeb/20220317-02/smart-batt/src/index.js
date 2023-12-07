import React from "react";
// react-router
import ReactDOM from "react-dom";
// BrowserRouter
import { HashRouter, Route, Switch } from "react-router-dom";
// react-redux
import { Provider } from "react-redux";
import { PersistGate } from "redux-persist/integration/react";
import { store, persistor } from "./store";
// for ie
import "react-app-polyfill/ie11";
import "react-app-polyfill/stable";
// i18n
import './utils/i18n';
import { I18nextProvider } from "react-i18next";
// style&webfont
import { createMuiTheme } from "@material-ui/core/styles";
import { ThemeProvider } from "@material-ui/styles";
// import { green } from '@material-ui/core/colors';
import { blue } from '@material-ui/core/colors';
import "@fortawesome/fontawesome-free/css/all.min.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "./index.css";
// component
import { App, Login, PrivacyPolicy } from './Route/lazyLoad';

// 測量性能用
// import reportWebVitals from "./reportWebVitals";




const theme = createMuiTheme({
	typography: {
		fontFamily: [
			"微軟正黑體",
			"Microsoft JhengHei",
			"Open Sans",
			'"Helvetica Neue"',
			"Arial",
			"sans-serif",
		].join(","),
	},
	palette: {
		primary: blue,
	},
});





ReactDOM.render(
	<HashRouter>
 		<Provider store={store}>
 		<PersistGate loading={null} persistor={persistor}>
			<I18nextProvider>
				<ThemeProvider theme={theme}>
					<Switch>
						{/* 登入 */}
						<Route path='/login' component={Login} />
						{/* 隱私權政策及網站安全政策 */}
						<Route path='/PrivacyPolicy' component={PrivacyPolicy} />
						{/* 內容 */}
						<Route path='/' component={App} />
					</Switch>
				</ThemeProvider>
			</I18nextProvider>
 		</PersistGate>
 		</Provider>
 	</HashRouter>,
	document.getElementById("root")
);







// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
// reportWebVitals();

// yarn add react-transition-group --save
// yarn add prop-types --save
// yarn add classnames --save
// yarn add bootstrap --save
// yarn add react-router-dom --save
// yarn add redux --save
// yarn add react-redux
// yarn add redux-saga --save
// yarn add @fortawesome/fontawesome-free
// yarn add sass-loader
// yarn add node-sass@4.14.1
// yarn add autoprefixer
// yarn add leaflet
// yarn add redux-persist --save
// yarn add react-datepicker --save
// yarn add moment --save
// yarn add @material-ui/core
// yarn add @material-ui/lab
// yarn add @material-ui/icons
// yarn add @material-ui/pickers --save
// yarn add redux-logger --save
// yarn add react-spring --save
// yarn add react-js-pagination
// yarn add react-number-format --save
// yarn add d3
// yarn add react-chartjs-2 --save
// yarn add chart.js
// yarn add chartjs-plugin-zoom
// yarn add chartjs-plugin-annotation --save
// yarn add chartjs-plugin-datalabels
// yarn add @react-google-maps/api
// yarn add google-maps-react
// yarn add react-geocode
// yarn add i18next 
// yarn add react-i18next
// yarn add i18next-http-backend
// yarn add i18next-fetch-backend
// yarn add i18next-browser-languagedetector
// yarn add i18next-localstorage-cache
// yarn add react-swipeable-views
// yarn add react-use
// yarn add react-date-range