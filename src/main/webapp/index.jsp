<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<body>
	<h2>List of cakes that are stored in the DataBase:</h2>

	<table style="width: 100%">
		<tr>
			<th align="left">Title</th>
			<th align="left">Description</th>
			<th align="left">Image</th>
		</tr>
		<c:forEach items="${cakeList}" var="cake">
			<tr>
				<td>${cake.title}</td>
				<td>${cake.description}</td>
				<td><a href="${cake.image}">Image of ${cake.title}</a></td>
			</tr>
		</c:forEach>
	</table>

	<h2>Add a cake to the Database:</h2>

	<form name="newCakeForm" method="post" action="">
		Title: <input type="text" name="title" /> <br />
		Description: <input type="text" name="description" /> <br />
		Image: <input type="text" name="image" /> <br />
		<input type="submit" value="Create Cake" />
	</form>

</body>
</html>
