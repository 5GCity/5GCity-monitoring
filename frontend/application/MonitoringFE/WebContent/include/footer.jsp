
<script>
function showdisclaimer(){
// 	alert('disclaimer');
// 	http://almsaeedstudio.com	
	var title = 'Copyright (c) 2015-2016 Italtel S.p.a. - All rights reserved';
	var disclaimer = '<div style="background-color : #F5F5F5; padding : 10px">Permission is hereby granted, free of charge, to any person \
					obtaining a copy of this software and associated documentation \
					files (the "Software"), to deal in the Software without restriction, \
					including without limitation the rights to use, copy, modify, merge, \
					publish, distribute, sublicense, and/or sell copies of the Software, \
					and to permit persons to whom the Software is furnished to do so, \
					subject to the following conditions.The above copyright notice and \
					this permission notice shall be included in all copies or \
					substantial portions of the Software.</div><br>'+

					'<div style="background-color : #F5F5F5; padding : 10px">THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, \
					EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF \
					MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND \
					NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR \
					COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER \
					LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, \
					ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE \
					USE OR OTHER DEALINGS IN THE SOFTWARE.</div>';
	
	var modalBody = '<div clas="form-modal-body col-md-12">';//A
	
	modalBody += disclaimer; //A
	
	modalBody += '</div>';
	
	var modal = '<div id="modal-disclaimer" class="modal" data-backdrop="static"'
		+'data-keyboard="false" >'
		+'<div class="modal-dialog">'
			+'<div class="modal-content">'
				+'<div class="modal-header">'
				+ '<h4>' + title + '</h4>'
				+ '</div>' // modal header
				+'<div class="row modal-body" style="max-height : calc(100vh - 10px); overflow-y:auto;">'
					+ modalBody
				+'</div>' // modal body
				+'<div class="modal-footer hide-me-first">'
				+ 		'<button class="btn btn-primary btn-confirm-modal btn-sm">OK</button>' // class="hide-me"
				+'</div>' // modal footer
			+'</div>' // modal-content
		+'</div>' // modal-dialog
	+'</div>'; // id
	
	
	$('.wrapper').append(modal);
	
	$('#modal-disclaimer').modal('show');
	$('.modal-body').css({
		'margin' : '0 15px 0 15px'
	});
	
	$('.btn-confirm-modal').css({ 'cursor' : 'pointer' });
	$('.btn-confirm-modal .form-modal-body').css({
					'padding' : '15px' });
	

	$('.btn-confirm-modal').click(function(){
		$('#modal-disclaimer').modal('hide');
		$('#modal-disclaimer').remove();
	});

}

function copyright(){
	var currYear = new Date().getFullYear();
	document.getElementById('copyright').innerHTML = '<strong>Copyright &copy; 2012-'+currYear+' <a\
		href="http://www.italtel.com">Italtel S.p.a.</a>.\
		</strong> All rights reserved.';
}

</script>
<footer class="main-footer">
	
	<div class="pull-right hidden-xs">
		Powered By <b><a href="#" onclick="showdisclaimer()">AdminLTE 2</a></b>
	</div>
	<p id="copyright"></p>
	<script type="text/javascript"> copyright(); </script>
</footer>