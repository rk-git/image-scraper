// Set false for production
var mockup = false;

var urls = {
	getJobs: mockup ? "mockup/jobs.json" : "/scraper/jobs",
	getJob: mockup ?  "mockup/job.json" : "/scraper/job",
	postJob: "/scraper/job",
	deleteJob: "/scraper/job"
};

$(document).ready(function() {
	_refreshList();
});

_refreshList = function() {
    $.ajax({
        url: urls.getJobs,
        method: "GET"
    }).done(function(data) {
        let content = "<table>";
        content += _renderHeader();
    	data.forEach(row => content += _renderRow(row));
        content += "</table>";
        $("#list").html(content);
    }).fail(function(data) {
        $("#list").empty();
        _showMsg(data.status + " " + data.error);
    });
};

_renderHeader = function() {
	let str = "<table>";
    str += "<tr>";
	str += "<th>ID</th>";
	str += "<th>URL</th>";
	str += "<th>Start</th>";
	str += "<th>End</th>";
	str += "<th>Status</th>";
	str += "<th>View</th>";
	str += "<th>Delete</th>";
	str += "</tr>";
	return str;
};

_renderRow = function(row) {
	let str = "<tr>";
	str += "<td>" + row.id + "</td>";
	str += "<td>" + row.url + "</td>";
	str += "<td>" + row.startTime + "</td>";
	str += "<td>" + row.endTime + "</td>";
	str += "<td>" + row.status + "</td>";
	str += "<td><a href='javascript:_getDetails(\"" + row.id + "\")'>View</a></td>";
	str += "<td><a href='javascript:_deleteJob(\"" + row.id + "\")'>Delete</span></td>";
	str += "</tr>";
	return str;
};

_getDetails = function(id) {
    let jobUrl = urls.getJob;
    if (!mockup)
        jobUrl += "/" + id;
    $.ajax({
        url: jobUrl
    }).done(function(data) {
        _showImage(data.image, data.mime);
    }).fail(function(data) {
        _showMsg(data.status + " " + data.error);
    });
};

_showImage = function(imageData, mime) {
    if (!mime)
        mime = "image/png";
    $("#mainRight").removeClass("hidden");
    $("#jobImage").attr("src", "data:" + mime + ";base64," + imageData);
};

_hideImage = function() {
    $("#mainRight").addClass("hidden");
};

_deleteJob = function(id) {
    if (mockup) {
        // Cannot create new in mockup
        alert("Cannot delete in mockup mode.")
        return;
    }
    $.ajax({
        url: urls.deleteJob + "/" + id,
        method: "DELETE"
    }).done(function(data) {
        _showMsg("Job deleted!");        
    }).fail(function(data) {
        _showMsg(data.status + " " + data.error);
    });
    _refreshList();
};

_createJob = function() {
    let jobUrl = $("#newUrl").val();
    if (jobUrl == "") {
        alert("Cannot create job with blank URL.")
        return;
    }
    if (mockup) {
        // Cannot create new in mockup
        alert("Cannot create job " + jobUrl + " in mockup mode.")
        return;
    }
    $.ajaxSetup({
        contentType: "application/json; charset=utf-8"
    });
    $.ajax({
        url: urls.postJob,
        method: "POST",
        data: {
        	url: jobUrl
        }
    }).done(function(data) {
        #_showMsg("Job created!");        
        _refreshList();
    }).fail(function(data) {
        _showMsg(data.status + " " + data.error);
    });
    _refreshList();
};

_showMsg = function(msg) {
	alert(msg);
};
