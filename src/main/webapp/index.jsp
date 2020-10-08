<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<body>
	<h2>List of Cakes that are Stored in the Database:</h2>

	<table style="width: 70%">
		<tr>
			<th align="left">Id</th>
			<th align="left">Title</th>
			<th align="left">Description</th>
			<th align="left">Image</th>
		</tr>
		<c:forEach items="${cakeList}" var="cake">
			<tr>
				<td>${cake.id}</td>
				<td>${cake.title}</td>
				<td>${cake.desc}</td>
				<td><a href="${cake.image}">${cake.title}</a></td>
			</tr>
		</c:forEach>
	</table>

	<h2>Add a Cake to the Database:</h2>

	<form name="newCakeForm" method="post" action="">
		<table style="width: 35%">
			<tr>
				<td align="right">Title:</td>
				<td><input type="text" name="title" maxlength="100" /></td>
			</tr>
			<tr>
				<td align="right">Description:</td>
				<td><input type="text" name="desc" maxlength="100" /></td>
			</tr>
			<tr>
				<td align="right">Image:</td>
				<td><input type="text" name="image" maxlength="300" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Create Cake" /></td>
			</tr>
		</table>
		<span class="error">${errorMessage.message}</span>
	</form>

</body>
</html>
