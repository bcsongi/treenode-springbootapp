<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>TreeNode app</title>

        <script src="https://code.jquery.com/jquery-3.4.1.min.js" integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo=" crossorigin="anonymous" />
        <script src="https://code.jquery.com/ui/3.4.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous" />
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css" />

        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>

        <script src="../scripts/treenode.js"></script>
        <link href="../style/treenode.css" rel="stylesheet" />
    </head>
    <body>
		<jsp:include page="addTreeNode.jsp" />
		<jsp:include page="editTreeNode.jsp" />

		<input type="hidden" id="selectedTreeNodeId" />
		<input type="hidden" id="selectedNameId" />
		<input type="hidden" id="selectedContentId" />
		<input type="hidden" id="selectedParentId" />
		
		<nav class="navbar navbar-dark bg-dark ">
			<div class="col-md-6 ">
				<button type="createButtonId" class="btn btn-success mt-1 mb-1" onclick="openAddNewChildPage()">Create</button>
				<button type="editButtonId" class="btn btn-warning mt-1 mb-1" onclick="openEditTreeNodePage()">Edit</button>
				<button type="deleteButtonId" class="btn btn-danger mt-1 mb-1" onclick="deleteById()">Delete</button>	
			</div>
			<div class="col-md-6 mt-1 mb-1">
				<input class="form-control form-control-sm w-75" type="text" placeholder="Search"
					id="findFieldId" aria-label="Search">
			</div>	
		</nav>
		
		<div class="container-fluid">
			<div class="alert alert-danger d-none" id="form-error">
				<button type="button" class="close" data-dismiss="alert" aria-hidden="true">
					&times;
				</button>
			</div>
		
			<div class="row jsTreeRow">
				<div class="col-md-6 mb-4">
					<div id="jstree"></div>
				</div>
				<div class="col-md-6 mb-4">
					<h4>Main content:</h4>
					<div class="card ">
						<div class="card-body " id="guiMainContentId" />
					</div>
				</div>
			</div>
		</div>
		
		<script>
			var treeNodeArray = [];
			<c:forEach items="${treeNodeList}" var="treeNode">
				treeNodeArray.push(convertToJSObj('${treeNode.id}', '${treeNode.name}', '${treeNode.contentId}', '${treeNode.parentId}'));
			</c:forEach> 
			initGUI(treeNodeArray);
		</script>
	</body>
</html>
