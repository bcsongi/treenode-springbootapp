<div id="edit-treenode-dialog" class="modal fade" role="dialog">
    <div class="modal-dialog">
		<div class="modal-content">
			<form id="editFormId" >
				<div class="modal-header">
					<h4 class="modal-title">Edit TreeNode Page</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				<div class="modal-body">
						<tr>
							<td>
								<span>Name:</span>
								<input type="text" class="input-sm form-control" id="editNameId" placeholder="Type a name.." required >
							</td>
						</tr>
						<tr>
							<td>
								<span>Content:</span>				
								<input type="text" class="input-sm form-control" id="editMainContentId" placeholder="Type a content.." required >
							</td>
						</tr>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
					<button type="submit" id="editSubmitButtonId" class="btn btn-primary">Edit</button>
				</div>
			</form>
		</div>
    </div>
</div>
