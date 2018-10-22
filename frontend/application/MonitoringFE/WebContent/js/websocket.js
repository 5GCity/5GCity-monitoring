var ws = null;

try {
	ws = new WebSocket("wss://" + document.location.host + "/Monitoring-WebGui/EndPoint");
	ws.onmessage = function(event) {

		var currCountAlarm = event.data;

		if (currCountAlarm > 0) {
			$("#bell-label").removeClass("label-success").addClass("label-danger");
		} else {
			$("#bell-label").removeClass("label-danger").addClass("label-success");
		}

		$("#bell-label").html(currCountAlarm);

		if ($("#active-alarm-count").length > 0) {
			Fault.WS.getActiveAlarms({
				matchFilter : {
					severity : [],
					type : [],
					resourceType : []
				},
				pageFilter : {
					numQueryResults : 1000
				}
			}, function(data, targetDom) {
				Fault.widget.dashboard(data.data.alarms, '#active-alarm-count');
			});
		}
	};

	ws.onerror = function(event) {
		// console.log("Error ", event);
	};
} catch(e) {
}
