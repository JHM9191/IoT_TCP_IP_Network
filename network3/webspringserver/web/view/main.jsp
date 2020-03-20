<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
@import url('https://fonts.googleapis.com/css?family=Roboto+Condensed');

.preserve-3d {
	transform-style: preserve-3d;
	-webkit-transform-style: preserve-3d;
}

body {
	background-color: #EAEAEA;
	align-content: center;
	/* vertical-align: middle; */
	padding: 0;
	padding-top: 100px;
	padding-bottom: 100px;
	margin: 0;
	border: 0;
	overflow-x: none;
	font-family: Roboto Condensed, sans-serif;
	font-size: 40px;
	font-smooth: always;
	-webkit-font-smoothing: antialiased;
	position: relative;
	font-family: Roboto Condensed, sans-serif;
	margin: 0;
}

/* table */
th {
	width: 180px;
	color: gray;
	border-right: solid;
	border-right-width: 1px;
	border-right-color: gray;
}

td {
	padding-left: 20px;
}

table {
	border: solid;
	border-width: 1px;
	border-color: black;
	background: black;
	padding: 20px;
	background-color: white;
}

select, option {
	font-family: Roboto Condensed, sans-serif;
	font-size: 20px;
}

/* button customization */
.button_base {
	margin: 0;
	border: 0;
	font-size: 18px;
	position: relative;
	top: 50%;
	left: 50%;
	margin-top: -25px;
	margin-left: -100px;
	width: 200px;
	height: 50px;
	text-align: center;
	cursor: default;
}

.button_base:hover {
	cursor: pointer;
}

.btn_3d_pushback {
	perspective: 500px;
	-webkit-perspective: 500px;
	-moz-perspective: 500px;
	transform-style: preserve-3d;
	-webkit-transform-style: preserve-3d;
	-moz-perspective: 500px;
	transform-style: preserve-3d;
	transform-style: preserve-3d;
}

.btn_3d_pushback div {
	position: absolute;
	text-align: center;
	width: 100%;
	height: 50px;
	box-sizing: border-box;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	pointer-events: none;
	padding: 10px;
	border: #000000 solid 1px;
}

.btn_3d_pushback div:nth-child(1) {
	color: #000000;
	background-color: #ffffff;
	transform: rotateX(0deg) translateZ(0px);
	-webkit-transform: rotateX(0deg) translateZ(0px);
	-moz-transform: rotateX(0deg) translateZ(0px);
	transform-origin: 50% 50%;
	-webkit-transform-origin: 50% 50%;
	-moz-transform-origin: 50% 50%;
	transition: all 0.2s ease;
	-webkit-transition: all 0.2s ease;
	-moz-transition: all 0.2s ease;
}

.btn_3d_pushback div:nth-child(2) {
	color: #ffffff;
	background-color: #000000;
	transform: rotateX(-179.5deg) translateZ(1px);
	-webkit-transform: rotateX(-179.5deg) translateZ(1px);
	-moz-transform: rotateX(-179.5deg) translateZ(1px);
	transform-origin: 50% 50%;
	-webkit-transform-origin: 50% 50%;
	-moz-transform-origin: 50% 50%;
	transition: all 0.2s ease;
	-webkit-transition: all 0.2s ease;
	-moz-transition: all 0.2s ease;
}

.btn_3d_pushback:hover div:nth-child(1) {
	transition: all 0.2s ease;
	-webkit-transition: all 0.2s ease;
	-moz-transition: all 0.2s ease;
	transform: rotateX(179.5deg);
	-webkit-transform: rotateX(179.5deg);
	-moz-transform: rotateX(179.5deg);
}

.btn_3d_pushback:hover div:nth-child(2) {
	transition: all 0.2s ease;
	-webkit-transition: all 0.2s ease;
	-moz-transition: all 0.2s ease;
	transform: rotateX(0deg) rotateY(0deg) translateZ(1px);
	-webkit-transform: rotateX(0deg) rotateY(0deg) translateZ(1px);
	-moz-transform: rotateX(0deg) rotateY(0deg) translateZ(1px);
}
</style>
<script>
	function submitForm() {
		document.getElementById("webapp_form").submit();
	}
	function display(result) {

	};
	function getData() {
		console.log('hello~');
		$.ajax({
			url : 'getIoTClientLoginData.top?',
			success : function(result) {
				console.log(result);
				/* $.each(result, function(idx, data) {
					console.log(data[idx].id);
				}); */
				/* for (var i = 0; i < result.size(); i++) {
					console.log(result.get(i).toString());
				}
				; */
				console.log(result.getIp());
				console.log(result.id);
				display(result);
			},
			error : function() {
			}

		});
	};

	setInterval(function() {
		getData();
	}, 500);

	/* 	$(document).ready(function() {
	 getData();
	 }); */
</script>
</head>
<body topmargin="250dp">
	<form id="webapp_form" action="webclient.top" method="get">
		<div align="center">
			<h1>Admin Page</h1>
			<table>
				<tr>
					<th>IP 주소</th>
					<td colspan="2"><select name="carId" required="required"
						autofocus="autofocus"
						style="width: 450px; height: 40px; font-size: 20px;">
							<option value="">ALL</option>
							<option value="car1">car1</option>
							<!-- <option value="/70.12.113.199">hyunchu(/70.12.113.199)</option>
							<option value="/70.12.227.235">jmj(/70.12.227.235)</option>
							<option value="/70.12.113.191">hennie(/70.12.113.191)</option> -->

					</select></td>
				</tr>
				<tr>
					<th>상태</th>
					<td align="center"><input type="radio" name="message"
						value="1" checked="checked" required="required">start</td>
					<td align="center"><input type="radio" name="message"
						value="0">stop</td>

				</tr>
				<!-- <tr>
					<th>메세지</th>
					<td colspan="2"><input type="text"
						style="height: 40px; font-size: 20px;" size="40" name="txt"></td>
				</tr> -->

			</table>
		</div>
		<br> <br>
		<div class="btn_3d_pushback button_base" onClick="submitForm();">
			<div>보내기</div>
			<div>보내기</div>
		</div>
		<input type="hidden" name="control" value="engine" />
	</form>
	<table>
		<c:forEach var="i" items="${loginList }">
			<tr>
				<td>${i.ip}</td>
				<td>${i.id}</td>

			</tr>
		</c:forEach>
	</table>
</body>
</html>