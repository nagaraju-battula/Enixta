<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script
	src="<%=request.getContextPath()%>/resources/plugins/datatables/jquery.dataTables.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/resources/plugins/datatables/dataTables.bootstrap.min.js"
	type="text/javascript"></script>
<div class="panel panel-default">
	<div class="panel-heading">Select phones for which you want to see
		the review.</div>
	<div class="panel-body">
		<c:choose>
			<c:when test="${smartPhoneList != null}">
				<fieldset>
					<form class="form-horizontal" method="POST" name="formEntry"
						id="reviewForm">

						<div class="box">
							<div class="box-body">
								<table class="table table-bordered">
									<tr>
										<th style="width: 10px">#</th>
										<th>Select</th>
										<th>Name</th>
									</tr>

									<c:forEach var="smartPhone" items="${smartPhoneList}"
										varStatus="status">
										<tr>
											<td><span class="">${status.count}</span></td>
											<td><span class=""><input type="radio"
													name="displayName" value="${smartPhone.displayName}"></span></td>
											<td><span class="">${smartPhone.displayName}</span></td>		
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
					</form>
				</fieldset>
			</c:when>
			<c:otherwise>
				<fieldset>Ooops, No results found for your search.</fieldset>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="box-footer">
		<label class="control-label col-sm-2"></label>

		<div class=" col-sm-3">
			<a class="btn btn-primary" href="#"
				onclick="submitForm('/buysmaart/phonereviews.action', 'reviewForm', 'resultsDiv');">Get review</a>
		</div>
	</div>
</div>
<div id="resultsDiv">
</div>