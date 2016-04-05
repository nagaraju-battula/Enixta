<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script
	src="<%=request.getContextPath()%>/resources/plugins/datatables/jquery.dataTables.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/resources/plugins/datatables/dataTables.bootstrap.min.js"
	type="text/javascript"></script>
<div class="panel panel-default">
	<div class="panel-heading">search here</div>
	<div class="panel-body">
		<fieldset>
			<form class="form-horizontal" method="POST" name="formEntry"
				id="reviewForm">
				<div class="box">
					<div class="input-group input-group-sm">
						<input type="text" class="form-control" name="query"> <span
							class="input-group-btn">
							<a class="btn btn-info btn-flat" href="#"
									onclick="submitForm('/buysmaart/lucenesearch.action', 'reviewForm', 'resultsDiv');">Go</a>
						</span>
					</div>
				</div>
			</form>
		</fieldset>
	</div>
	<div class="box-footer">
		<label class="control-label col-sm-2"></label>
	</div>
</div>
<div id="resultsDiv"></div>

