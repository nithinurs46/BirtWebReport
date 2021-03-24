<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Selection Criteria</title>
</head>
<style>
div.ex {
	text-align: right width:300px;
	padding: 10px;
	border: 2px solid black;
	margin: 0px
}
</style>
<body>
	<h3>Please enter data for fields below</h3>
	<h4>Note: Please leave the fields blank if not required, select
		report format and click on generate</h4>
	<div class="ex">
		<form action="ReportController" method="post">
			<table style="with: 50%">
				<tr>
					<td>First Name</td>
					<td><input type="text" name="firstName" /></td>
				</tr>
				<tr>
					<td>Last Name:</td>
					<td><input type="text" name="lastName" /></td>
				</tr>
				<tr>
					<td>Department Name:</td>
					<td><input type="text" name="deptName" /></td>
				</tr>
				<tr>
					<td>Location:</td>
					<td><input type="text" name="location" /></td>

				</tr>
				<tr>
					<td>Report Format:</td>
					<td><select name="reportFormat">
							<option value="pdf">PDF</option>
							<option value="xls">Excel</option>
							<option value="doc">Word</option>
					</select></td>
				</tr>
			</table>
			<input type="submit" value="Generate" />
		</form>
	</div>
</body>
</html>