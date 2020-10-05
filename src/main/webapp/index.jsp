<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<body>
	<h2>List of cakes that are stored in the DB:</h2>

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

</body>
</html>
