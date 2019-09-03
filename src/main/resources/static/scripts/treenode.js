function convertToJSObj(id, name, contentId, parentId) {
	var e = new Object();
	e.id = id;
	e.text = name; 
	e.data = contentId;
	e.parent = parentId || '#';
	return e;
}

function initGUI(data) {
	$("#jstree").jstree({
		"core" : {
			"check_callback" : true,
			"data": data
		},
		"plugins" : [ "dnd", "search"],
		"search": {
			"case_insensitive": true,
			"show_only_matches": true
		},
	}).on("select_node.jstree", function(evt, treeNode) {
		getSelectedNodeInfo(treeNode.node);
    }).bind("move_node.jstree", function (evt, treeNode) {
		var data = {
			id: treeNode.node.id,
			parentId: treeNode.node.parent
		};
		reorganizeByParentId(data);
    }).on('search.jstree', function (nodes, data) {
		if (data.nodes.length === 0) {
			$('#jstree').jstree(true).hide_all();
		} 
		
		$('.myjstree-grayish', $("#jstree")).removeClass("myjstree-grayish");
		// selector for node whitch open but not matched
		$('li a', $("#jstree"))
			.not(".jstree-search")
			.parent(".jstree-open")
			.addClass('myjstree-grayish');
	});
}

$(function () {
    var typingTimer;
    $('#findFieldId').on('keyup', function () {
        var value = $(this).val();
        clearTimeout(typingTimer);
        typingTimer = setTimeout(function() {
            $('#jstree').jstree(true).show_all();
            $('#jstree').jstree('search', value);
        }, 1500);
    });

    $('#findFieldId').on('keydown', function () {
        clearTimeout(typingTimer);
    });

    $('#add-treenode-dialog').modal("hide");
    $("#edit-treenode-dialog").modal("hide");

    $("#addFormId").on("submit", function(e){
        e.preventDefault();

        var data = {
            name: $("#addNameId").val(),
            parentId: $("#selectedTreeNodeId").val(),
            mainContent: $("#addMainContentId").val()
        };
        if (!data.name || !data.mainContent) {
            return false;
        }
        $.ajax({
            url : "/treenode/create",
            type: 'POST',
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function(result){
                location.reload();
            },
            error: function(e) {
                handleErrorMessage(true, "Sorry, but some error occurred during the operation!");
            }
        });
    });

    $("#editFormId").on("submit", function(e){
        e.preventDefault();

        var data = {
            treeNode: {
                id: $("#selectedTreeNodeId").val(),
                name: $("#editNameId").val(),
                contentId: $("#selectedContentId").val(),
                parentId: $("#selectedParentId").val(),
            },
            content: {
                id: $("#selectedContentId").val(),
                mainContent: $("#editMainContentId").val()
            }
        };
        if (!data.treeNode.name || ! data.content.mainContent) {
            return false;
        }
        $.ajax({
            url : "/treenode/update",
            type: 'PUT',
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function(result){
                location.reload();
            },
            error : function(e) {
                handleErrorMessage(true, "Sorry, but some error occurred during the operation!");
            }
        });
    });
});

function deleteById() {
	var treeNodeId = $("#selectedTreeNodeId").val();
	if (!treeNodeId) {
		handleErrorMessage(true, "Please select a node!");
		return;
	} else if ($("#selectedParentId").val() == "#") {
		handleErrorMessage(true, "Couldn't delete root node!");
		return;
	}
	$.ajax({
		url: "/treenode/deleteById?" + $.param({
			"id": treeNodeId
		}),
	    type: 'DELETE',
		success: function(result){
			location.reload();
		},
		error : function(e) {
			handleErrorMessage(true, "Sorry, but some error occurred during the operation!");
		}
    }); 
}

function reorganizeByParentId(data) {
	$.ajax({
		url : "/treenode/reorganizeByParentId",
		type: 'PUT',
		data: JSON.stringify(data),
        contentType: 'application/json',
		success: function(result){
			location.reload();
		},
		error : function(e) {
			handleErrorMessage(true, "Sorry, but some error occurred during the operation!");
		}
    }); 
}

function openAddNewChildPage() {
	if (!$("#selectedTreeNodeId").val()) {
		handleErrorMessage(true, "Please select a node!");
		return;
	} else {
		handleErrorMessage(false);
	}
		
	$("#addNameId").val("");
	$("#addMainContentId").val("");
	$('#add-treenode-dialog').modal('show');
}

function openEditTreeNodePage() {
	if (!$("#selectedTreeNodeId").val()) {
		handleErrorMessage(true, "Please select a node!");
		return;
	} else {
		handleErrorMessage(false);
	}
	$("#editNameId").val($("#selectedNameId").val());
	$("#editMainContentId").val($("#guiMainContentId").text());
	$("#edit-treenode-dialog").modal('show');
}

function getSelectedNodeInfo(treeNode) {
	$("#selectedTreeNodeId").val(treeNode.id);
	$("#selectedNameId").val(treeNode.text);
	$("#selectedContentId").val(treeNode.data);
	$("#selectedParentId").val(treeNode.parent);
	$.get({
		url : "/content/getById?" + $.param({"id": treeNode.data}),
		success: function(result){
			$("#guiMainContentId").text(result.mainContent);
		},
		error : function(e) {
			handleErrorMessage(true, "Sorry, but some error occurred during the operation!");
		}
    });
}

function handleErrorMessage(shouldShow, message) {
	if (shouldShow && $('#form-error').hasClass('d-none')) {
		$('#form-error').html("<p>" + message +"</p>").removeClass("d-none");
	} else if (shouldShow && !$('#form-error').hasClass('d-none')) {
		$('#form-error').html("<p>" + message +"</p>");
	} else if (!shouldShow && !$('#form-error').hasClass('d-none')) {
		$('#form-error').addClass("d-none");
	}
}
