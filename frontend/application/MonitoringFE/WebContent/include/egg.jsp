<script src="jslib/egg.min.js"></script>
<script>
var egg = new Egg("up,up,down,down,left,right,left,right,b,a", function() {
	$('<img id="egggif" src="img/giphy.gif" style="position:absolute;top:33%;left:25%;display:none;">').appendTo(document.body);
	  jQuery('#egggif').fadeIn(500, function() {
	    window.setTimeout(function() { jQuery('#egggif').hide(); jQuery('#egggif').remove(); }, 5000);
	  });
	}).listen();
</script>