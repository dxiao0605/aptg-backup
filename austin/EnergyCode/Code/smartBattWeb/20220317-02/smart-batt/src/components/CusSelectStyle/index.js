
// import React from "react";
import { withStyles } from "@material-ui/core/styles";
import InputBase from "@material-ui/core/InputBase";

// 
export const CusSelectStyle = withStyles((theme) => ({
	root: {
		"label + &": {
			marginTop: theme.spacing(3),
		},
	},
	input: {
        // borderRadius: 4,
        display: 'inline-block',
		position: "relative",
        // backgroundColor: theme.palette.background.paper,
        backgroundColor: '#ededed',
        // border: "1px solid #ced4da",
        border: 'none',
		fontSize: 16,
        // padding: "10px 26px 10px 12px",
        padding: '6px 8px;',
		transition: theme.transitions.create(["border-color", "box-shadow"]),
		fontFamily: [
			"-apple-system",
			"BlinkMacSystemFont",
			'"Segoe UI"',
			'"Helvetica Neue"',
			"Arial",
			"sans-serif",
			'"Apple Color Emoji"',
			'"Segoe UI Emoji"',
			'"Segoe UI Symbol"',
		].join(","),
		"&:focus": {
			// borderRadius: 4,
			// borderColor: "#80bdff",
			boxShadow: "0 0 0 0.2rem rgba(0,123,255,.25)",
		},
	},
}))(InputBase);


// 選單從下方顯示
export const menuUnderSelectStyle = {
    anchorOrigin: {
      vertical: "bottom",
      horizontal: "left"
    },
    transformOrigin: {
      vertical: "top",
      horizontal: "left"
    },
    getContentAnchorEl: null
}