var update=true;
var projectUpdate=true;
var process_project_row=function(rowData){
	var status=rowData[3].textContent;
	$('#project_completion_form').hide();
	$('#assign_project_form').hide();
	$('#project_message').empty();
	if(status=="UNASSIGNED"){
		$('#project_info_body').html(
			'<b>Id:</b>'+rowData[0].textContent+'<br/>'+
			'<b>Name:</b>'+rowData[1].textContent+'<br/>'+
			'<b>Description:</b>'+rowData[2].textContent+'<br/>');
		$('#project_form_id').val(rowData[0].textContent);
		$('#project_form_name').val(rowData[1].textContent);
		$('#project_form_description').val(rowData[2].textContent);
		$('#assign_employee').show();
		$('#mark_completed').hide();
		$('#project_info').show();
		$('#project_form').hide();
	}else if (status=="ASSIGNED") {
		$('#project_info_body').html(
			'<b>Id:</b>'+rowData[0].textContent+'<br/>'+
			'<b>Name:</b>'+rowData[1].textContent+'<br/>'+
			'<b>Description:</b>'+rowData[2].textContent+'<br/>'+
			'<b>Employee Name:</b>'+rowData[5].textContent+'<br/>'+
			'<b>Start Date:</b>'+rowData[6].textContent+'<br/>');
		$('#project_completion_form_date').attr('min', rowData[6].textContent);
		$('#project_form_id').val(rowData[0].textContent);
		$('#project_form_name').val(rowData[1].textContent);
		$('#project_form_description').val(rowData[2].textContent);
		$('#assign_employee').hide();
		$('#mark_completed').show();
		$('#project_info').show();
		$('#project_form').hide();
	}else{
		$('#project_info_body').html(
			'<b>Id:</b>'+rowData[0].textContent+'<br/>'+
			'<b>Name:</b>'+rowData[1].textContent+'<br/>'+
			'<b>Description:</b>'+rowData[2].textContent+'<br/>'+
			'<b>Employee Name:</b>'+rowData[5].textContent+'<br/>'+
			'<b>Start Date:</b>'+rowData[6].textContent+'<br/>'+
			'<b>End Date:</b>'+rowData[7].textContent+'<br/>'+
			'<b>Performance Score:</b>'+rowData[8].textContent+'<br/>');
		$('#project_form_id').val(rowData[0].textContent);
		$('#project_form_name').val(rowData[1].textContent);
		$('#project_form_description').val(rowData[2].textContent);
		$('#assign_employee').hide();
		$('#mark_completed').hide();
		$('#project_info').show();
		$('#project_form').hide();
	}
}
var refresh_employee_list=function () {
	$('#employee_projects_table').hide();
	$('#employee_message').empty();
	$('#employee_form').hide();
	$.ajax({
		url: 'http://localhost:8080/EmployeeExample/employee',
		type: 'GET'
	})
	.done(function(data) {
		if(data==null){
			$('#employee_table').empty();

		}else {
			$("#employee_table").html("<thead><tr><th>Id</th><th>Name</th><th>Email</th><th>Department</th><th>Performance Score</th></tr></thead><tbody>");
			for(i=0;i<data.length;i++){
				$("#employee_table").append("<tr>"+
					"<td>"+data[i].id+"</td>"+
					"<td>"+data[i].name+"</td>"+
					"<td>"+data[i].email+"</td>"+
					"<td>"+data[i].department.name+"</td>"+
					"<td>"+data[i].score+"</td>"+
					"<td style='display: none;'>"+data[i].department.id+"</td>"+
					"</tr>");
			}
			$("#employee_table").append('</tbody>');
			$('#employee_table tbody tr').click(function(event) {
				$('#employee_message').empty();
				var rowData=$(this).children('td');
				$('#employee_form').hide();
				$('#employee_info').show();
				$('#employee_info_body').empty();
				$('#employee_info_body').append("<b>Id:</b>"+rowData[0].textContent+"<br/>"+
					"<b>Name:</b>"+rowData[1].textContent+"<br/>"+
					"<b>Emal:</b>"+rowData[2].textContent+"<br/>"+
					"<b>Department:</b>"+rowData[3].textContent+"<br/>"+
					"<b>Performance Score:</b>"+rowData[4].textContent+"<br/>"
					);
				$('#employee_form_id').val(rowData[0].textContent);
				$('#employee_form_name').val(rowData[1].textContent);
				$('#employee_form_email').val(rowData[2].textContent);
				$('#employee_form_department').val(rowData[5].textContent);
			});
		}
	})
	.fail(function() {
		console.log("error");
	})
	.always(function() {
		console.log("complete");
	});
}

var refresh_project_list=function () {
	$('#project_table_body').empty();
	$('#project_form').hide();
	$.ajax({
		url: 'http://localhost:8080/EmployeeExample/project/',
		type: 'GET'
	})
	.done(function(data) {
		//console.log(data);
		if(data==null)
			return;
		for(i=0;i<data.length;i++){
			var employeeId="undefined";
			var employeeName="undefined";
			if(data[i].status=="ASSIGNED" || data[i].status=="COMPLETED"){
				employeeId=data[i].employee.id;
				employeeName=data[i].employee.name;
			}
				$('#project_table_body').append('<tr>'+
					'<td>'+data[i].id+'</td>'+
					'<td>'+data[i].name+'</td>'+
					'<td>'+data[i].description+'</td>'+
					'<td>'+data[i].status+'</td>'+
					'<td style="display : none;">'+employeeId+'</td>'+
					'<td style="display : none;">'+employeeName+'</td>'+
					'<td style="display : none;">'+data[i].startDate+'</td>'+
					'<td style="display : none;">'+data[i].endDate+'</td>'+
					'<td style="display : none;">'+data[i].performanceScore+'</td>'+
					'</tr>');
		}
		$('#project_table_body tr').click(function(event) {
			var rowData=$(this).children('td');
			$('project_search_table').empty();
			process_project_row(rowData);

		});
		
		
	})
	.fail(function() {
		console.log("error");
	})
	.always(function() {
		console.log("complete");
	});
	
}


$(document).ready(function($) {
	$('#employee_form').hide();
	$('#employee_info').hide();
	$('#project_form').hide();
	$('#project_info').hide();
	$('#project_completion_form').hide();
	$('#assign_project_form').hide();
	refresh_employee_list();
	refresh_project_list();
	$('#add_new_employee').click(function(event) {
		/* Act on the event */
		$("#employee_search_table").empty();
		$('#employee_form_id').val("");
		$('#employee_form_name').val("");
		$('#employee_form_email').val("");
		$('#employee_form_department').val(0);
		$('#employee_info').hide();
		$('#employee_form').show();
		$('#employee_message').empty();
		update=false;
	});

	$('#employee_form').submit(function(event) {
		$('#employee_projects_table').hide();
		$("#employee_search_table").empty();
		$('#employee_message').empty();
		name=$("#employee_form_name").val();
		email=$("#employee_form_email").val();
		department=$("#employee_form_department").val();
		if(update==true){
			type="PUT";
			url='http://localhost:8080/EmployeeExample/employee/'+$('#employee_form_id').val();
		}else {
			type="POST";
			url='http://localhost:8080/EmployeeExample/employee';
		}
		var data = {"name":name,"email":email,"departmentId":department};
		$.ajax({
			url: url,
			type: type,
			contentType: "application/json",
			data: JSON.stringify(data)
		})
		.done(function(data,status) {
			if(status=='notmodified'){
				$('#employee_message').html('<div class="alert alert-warning">Email already exists</div>');
			}else{

			$("#employee_message").html('<div class="alert alert-success">Insertion successful</div>');
			refresh_employee_list();
		}
		})
		.fail(function() {
			$('#employee_message').html('<div class="alert alert-warning">Email already exists</div>');
		})
		.always(function() {
			console.log("complete");
		});
		return false;
	});

	$('#employee_update_button').click(function(event) {
		/* Act on the event */
		$('#employee_message').empty();
		$('#employee_projects_table').hide();
		$("#employee_search_table").empty();
		update=true;
		$('#employee_info').hide();
		$('#employee_form').show();
	});

	$('#employee_delete_button').click(function(event) {
		/* Act on the event */
		$("#employee_search_table").empty();
		$('#employee_projects_table').hide();
		$.ajax({
			url: 'http://localhost:8080/EmployeeExample/employee/'+$('#employee_form_id').val(),
			type: 'DELETE'
		})
		.done(function() {
			console.log("success");
			refresh_employee_list();
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
		});
		
	});
	$('#show_employee_projects').click(function(event) {
		/* Act on the event */
		$('#employee_message').empty();
		$("#employee_search_table").empty();
		$.ajax({
			url: 'http://localhost:8080/EmployeeExample/project/employee/'+$('#employee_form_id').val(),
			type: 'GET'
		})
		.done(function(data) {
			if(data==null){
				$('#employee_message').html(('<div class="alert alert-warning">No projects allocated.</div>'));
				return;
			}
			
			$("#employee_projects_table").html("<thead><tr><th>Id</th><th>Name</th><th>Description</th><th>Table</th></tr></thead><tbody>");
			for(i=0;i<data.length;i++){
				var employeeId="undefined";
				var employeeName="undefined";
				if(data[i].status=="ASSIGNED" || data[i].status=="COMPLETED"){
					employeeId=data[i].employee.id;
					employeeName=data[i].employee.name;
				}
			$('#employee_projects_table').append('<tr>'+
				'<td>'+data[i].id+'</td>'+
				'<td>'+data[i].name+'</td>'+
				'<td>'+data[i].description+'</td>'+
				'<td>'+data[i].status+'</td>'+
				'<td style="display : none;">'+employeeId+'</td>'+
				'<td style="display : none;">'+employeeName+'</td>'+
				'<td style="display : none;">'+data[i].startDate+'</td>'+
				'<td style="display : none;">'+data[i].endDate+'</td>'+
				'<td style="display : none;">'+data[i].performanceScore+'</td>'+
				'</tr>');

			}
			$('#employee_projects_table').show();
			$('#employee_projects_table tbody tr').click(function(event) {
				/* Act on the event */
				event.preventDefault();
				var rowData=$(this).children('td');
				$('#project_search_table').hide();
				process_project_row(rowData);
				$('#myTabs a[href="#project"]').tab('show');
			});
			
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
		});
		
	});

	$('#employee_search_by_name').submit(function(event) {
		/* Act on the event */
		$('#employee_message').empty();
		$('#employee_projects_table').hide();
		$('#employee_info').hide();
		$.ajax({
			url: 'http://localhost:8080/EmployeeExample/employee/name/'+$("#employee_search_name").val(),
			type: 'GET'
		})
		.done(function(data,status) {
			if(status=='nocontent'){
				$('#employee_message').html('<div class="alert alert-warning">No employee with such name exists</div>');
			}else{
				$("#employee_search_table").html("<thead><tr><th>Id</th><th>Name</th><th>Email</th><th>Department</th></tr></thead><tbody>");
				for(i=0;i<data.length;i++){
					$("#employee_search_table").append("<tr>"+
						"<td>"+data[i].id+"</td>"+
						"<td>"+data[i].name+"</td>"+
						"<td>"+data[i].email+"</td>"+
						"<td>"+data[i].department.name+"</td>"+
						"<td style='display: none;'>"+data[i].department.id+"</td>"+
						"</tr>");
				}
				$("#employee_table").append('</tbody>');
				$('#employee_search_table tr').click(function(event) {
					var rowData=$(this).children('td');
					$('#employee_form').hide();
					$('#employee_info').show();
					$('#employee_info_body').empty();
					$('#employee_info_body').append("<b>Id:</b>"+rowData[0].textContent+"<br/>"+
						"<b>Name:</b>"+rowData[1].textContent+"<br/>"+
						"<b>Emal:</b>"+rowData[2].textContent+"<br/>"+
						"<b>Department:</b>"+rowData[3].textContent+"<br/>"
						);
					$('#employee_form_id').val(rowData[0].textContent);
					$('#employee_form_name').val(rowData[1].textContent);
					$('#employee_form_email').val(rowData[2].textContent);
					$('#employee_form_department').val(rowData[4].textContent);
				});
			}
			return false;
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
			return false;
		});
		return false;
	});

	$('#employee_search_by_id').submit(function(event) {
		/* Act on the event */
		$('#employee_message').empty();
		$('#employee_projects_table').hide();
		$('#employee_info').hide();
		$.ajax({
			url: 'http://localhost:8080/EmployeeExample/employee/id/'+$('#employee_search_id').val(),
			type: 'GET'
		})
		.done(function(data,status) {
			if(status=='nocontent'){
				$('#employee_message').html('<div class="alert alert-warning">No employee with such id exists</div>');
			}else {
				$('#employee_form').hide();
				$('#employee_info').show();
				$('#employee_info_body').empty();
				$('#employee_info_body').append("<b>Id:</b>"+data.id+"<br/>"+
					"<b>Name:</b>"+data.name+"<br/>"+
					"<b>Emal:</b>"+data.email+"<br/>"+
					"<b>Department:</b>"+data.department.name+"<br/>"
					);
				$('#employee_form_id').val(data.id);
				$('#employee_form_name').val(data.name);
				$('#employee_form_email').val(data.email);
				$('#employee_form_department').val(data.department.id);
			}
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
		});
		
		return false;
	});
	$('#add_new_project').click(function(event) {
		/* Act on the event */
		//console.log(event);
		$('#project_message').hide();
		$('#project_completion_form').hide();
		$('#assign_project_form').hide();
		$('#project_info').hide();
		projectUpdate=false;
		$('#project_form_name').val("");
		$('#project_form_description').val("");
		$('#project_form').show();
		$('#project_search_table').hide();
	});
	$('#project_update_button').click(function(event) {
		/* Act on the event */
		$('#project_message').hide();
		$('#project_info').hide();
		$('#project_completion_form').hide();
		$('#assign_project_form').hide();
		projectUpdate=true;
		$('#project_form').show();


	});
	$('#project_form').submit(function(event) {
		/* Act on the event */
		$('#project_search_table').hide();
		if(projectUpdate){
			url='http://localhost:8080/EmployeeExample/project/'+$('#project_form_id').val();
			type="PUT";		
			//console.log($('#project_form_id').val());
		}else{
			url='http://localhost:8080/EmployeeExample/project/';
			type="POST";	
		}
		var data={"name":$('#project_form_name').val(),"description":$('#project_form_description').val()};
		$.ajax({
			url: url,
			type: type,
			contentType: "application/json",
			data: JSON.stringify(data)
		})
		.done(function() {
			if(projectUpdate){
				$('#project_message').html('<div class="alert alert-success">Project details updated successfully.</div>');
			}else
				$('#project_message').html('<div class="alert alert-success">Project added successfully.</div>');
			$('#project_message').show();
			refresh_project_list();
			return false;
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
			return false;
		});
		return false;
	});
	$('#project_delete_button').click(function(event) {
		/* Act on the event */
		$('#project_message').empty();
		$('#project_search_table').hide();
		$('#project_info').hide();
		$.ajax({
			url: 'http://localhost:8080/EmployeeExample/project/'+$('#project_form_id').val(),
			type: 'DELETE'
		})
		.done(function(data,status) {
			if(status=="notmodified"){
				$('#project_message').html('<div class="alert alert-success">Project not deleted</div>');
			}else{
			console.log("success");
			$('#project_message').html('<div class="alert alert-success">Project deleted</div>');
			refresh_project_list();
		}
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
		});
		
	});
	$('#assign_employee').click(function(event) {
		/* Act on the event */
		$('#project_message').hide();
		$('#assign_project_form').show();
		$('#project_info').hide();
		$('#project_form').hide();
		$('#project_completion_form').hide();
		$('#assign_project_form').submit(function(event) {
			/* Act on the event */
			$('#project_search_table').hide();
			var url='http://localhost:8080/EmployeeExample/project/'+$('#project_form_id').val()+'/'+$('#assign_project_form_eid').val();
			if($('#assign_project_form_date').val()!="")
				url+='?date='+$('#assign_project_form_date').val();
			//console.log($('#project_completion_form_date').val());
			$.ajax({
				url: url,
				type: 'PUT'
			})
			.done(function(data,status) {
				console.log(status);
				if(status=="nocontent"){
					alert("No employee with given id");
					return false;
				}
				console.log("success");
				refresh_project_list();
			})
			.fail(function() {
				console.log("error");
			})
			.always(function() {
				console.log("complete");
			});
			
			refresh_project_list();
			return false;
		});
	});
	$('#mark_completed').click(function(event) {
		/* Act on the event */
		$('#project_message').hide();
		$('#assign_project_form').hide();
		$('#project_info').hide();
		$('#project_form').hide();
		$('#project_completion_form').show();
		$('#project_completion_form').submit(function(event) {
			/* Act on the event */
			var url='http://localhost:8080/EmployeeExample/project/'+$('#project_form_id').val()+'/score/'+$('#project_completion_form_score').val();
			if($('#project_completion_form_date').val()!="")
				url+='?date='+$('#project_completion_form_date').val();
			console.log($('#project_completion_form_date').val());
			$.ajax({
				url: url,
				type: 'PUT'
			})
			.done(function() {
				console.log("success");
				refresh_project_list();
			})
			.fail(function() {
				console.log("error");
			})
			.always(function() {
				console.log("complete");
			});
			
			refresh_project_list();
			return false;
		});
	});
	$('#project_search_by_id').submit(function(event) {
		/* Act on the event */
		event.preventDefault();
		$('#project_message').empty();
		$('#project_search_table').hide();
		var id=$('#project_search_id').val();
		if(id==""){
			$('#project_message').html('<div class="alert alert-warning">Enter ID please.</div>');
		}else{
			$.ajax({
				url: 'http://localhost:8080/EmployeeExample/project/'+id,
				type: 'GET'
			})
			.done(function(data,status) {
				//console.log("success");
				if(status=="nocontent")
					$('#project_message').html('<div class="alert alert-warning">No project with given id.</div>');
				else{
					if(status=="UNASSIGNED"){
						$('#project_info_body').html(
							'<b>Id:</b>'+data.id+'<br/>'+
							'<b>Name:</b>'+data.name+'<br/>'+
							'<b>Description:</b>'+data.description+'<br/>');
						$('#project_form_id').val(data.id);
						$('#project_form_name').val(data.name);
						$('#project_form_description').val(data.description);
						$('#assign_employee').show();
						$('#mark_completed').hide();
						$('#project_info').show();
						$('#project_form').hide();
					}else if (status=="ASSIGNED") {
						$('#project_info_body').html(
							'<b>Id:</b>'+data.id+'<br/>'+
							'<b>Name:</b>'+data.name+'<br/>'+
							'<b>Description:</b>'+data.description+'<br/>'+
							'<b>Employee Name:</b>'+data.employee.name+'<br/>'+
							'<b>Start Date:</b>'+data.startDate+'<br/>');
						$('#project_completion_form_date').attr('min', data.startDate);
						$('#project_form_id').val(data.id);
						$('#project_form_name').val(data.name);
						$('#project_form_description').val(data.description);
						$('#assign_employee').hide();
						$('#mark_completed').show();
						$('#project_info').show();
						$('#project_form').hide();
					}else{
						$('#project_info_body').html(
							'<b>Id:</b>'+data.id+'<br/>'+
							'<b>Name:</b>'+data.name+'<br/>'+
							'<b>Description:</b>'+data.description+'<br/>'+
							'<b>Employee Name:</b>'+data.employee.name+'<br/>'+
							'<b>Start Date:</b>'+data.startDate+'<br/>'+
							'<b>End Date:</b>'+data.endDate+'<br/>'+
							'<b>Performance Score:</b>'+data.performanceScore+'<br/>');
						$('#project_form_id').val(data.id);
						$('#project_form_name').val(data.name);
						$('#project_form_description').val(data.description);
						$('#assign_employee').hide();
						$('#mark_completed').hide();
						$('#project_info').show();
						$('#project_form').hide();
					}
				}
					
			})
			.fail(function() {
				console.log("error");
			})
			.always(function() {
				console.log("complete");
				return false;
			});
			
		}
		return false;
	});
	$('#project_search_by_name').submit(function(event) {
		/* Act on the event */
		$('#project_message').empty();
		$('#project_search_table').empty();
		var name=$("#project_search_name").val();
		if(name==""){
			$('#project_message').html('<div class="alert alert-warning">Enter ID please.</div>');
		}else{
			$.ajax({
				url: 'http://localhost:8080/EmployeeExample/project/name/'+name,
				type: "GET"
			})
			.done(function(data,status) {
				if(status=="nocontent"){
					$('#project_message').html('<div class="alert alert-warning">No projects to show</div>');
					return false;
				}
				$('#project_search_table').html('<thead><tr><th>Id</th><th>Name</th><th>Description</th><th>Status</th></tr></thead><tbody>');
				for(i=0;i<data.length;i++){
					var employeeId="undefined";
					var employeeName="undefined";
					if(data[i].status=="ASSIGNED" || data[i].status=="COMPLETED"){
						employeeId=data[i].employee.id;
						employeeName=data[i].employee.name;
					}
						$('#project_search_table').append('<tr>'+
							'<td>'+data[i].id+'</td>'+
							'<td>'+data[i].name+'</td>'+
							'<td>'+data[i].description+'</td>'+
							'<td>'+data[i].status+'</td>'+
							'<td style="display : none;">'+employeeId+'</td>'+
							'<td style="display : none;">'+employeeName+'</td>'+
							'<td style="display : none;">'+data[i].startDate+'</td>'+
							'<td style="display : none;">'+data[i].endDate+'</td>'+
							'<td style="display : none;">'+data[i].performanceScore+'</td>'+
							'</tr>');
				}
				$('#project_search_table').show();
				$('#project_search_table tr').click(function(event) {
					var rowData=$(this).children('td');
					process_project_row(rowData);

				});
			})
			.fail(function() {
				console.log("error");
			})
			.always(function() {
				console.log("complete");
			});
			
		}
		return false;
	});

});