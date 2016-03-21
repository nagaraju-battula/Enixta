<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script
	src="<%=request.getContextPath()%>/resources/plugins/datatables/jquery.dataTables.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/resources/plugins/datatables/dataTables.bootstrap.min.js"
	type="text/javascript"></script>
<div class="panel panel-default">
	<div class="panel-heading">Review results</div>
	<div class="panel-body">
		<c:choose>
			<c:when test="${reviews != null}">
				<fieldset>
					<form class="form-horizontal" method="POST" name="formEntry"
						id="formEntry">
						<div class="box">
							<div class="box-body">
								<table class="table table-bordered">
									<tr>
										<th style="width: 10px">#</th>
										<th> Video ID </th>
										<th> Review Title </th>
										<th> Review Description </th>
										<th> Video Publisher (ChannelTitle) </th>
										<th> Date of publishing </th>
										<th> Thumbnails </th>
										<th> Device name </th>
										<th> Action </th>
									</tr>

									<c:forEach var="review" items="${reviews}"
										varStatus="status">
										<tr>
											<td><span class="">${status.count}</span></td>
											<td><span class="">${review.videoId}</span></td>
											<td><span class="">${review.reviewTitle}</span></td>
											<td><span class="">${review.reviewDescription}</span></td>
											
											<td><span class="">${review.channelTitle}</span></td>
											<td><span class="">${review.publishedAt}</span></td>
											<td><span class="">${review.thumbnailUrl}</span></td>
											<td><span class="">${review.deviceName}</span></td>
											<td><span class="">
											<a href="#" onclick="submitAsPost('/buysmaart/deletereview.action', '${review.videoId}', '${review.deviceName}', 'resultsDiv');">
											delete</a>
											</span></td>
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
</div>