/**
 * @type $.Help
 * @memberOf __global
 */
$.Help = (/** @constructor */
function($) {

	/**
	 * @memberOf $.Help.__private
	 */
	function writeHelp(preloadObj, defaultClass) {
		$(document).ajaxStop(function() {
			$(document).unbind('ajaxStop');
			var box = "";
			$('section.wizard-content div.box, section.content div.box, div.nav-tabs-custom, div.data-help-box').each(

			function() {
				if ($(this).hasClass("data-help-skip") || $(this).parents('div.box').length > 0 || $(this).parents('div.nav-tabs-custom').length > 0) {
					return;
				}
				var checkInTabs = false;
				var boxTitle = '';
				var boxTarget = '';
				if ($(this).hasClass("nav-tabs-custom")) {
					checkInTabs = true;
					boxTitle = $(this).find('li.pull-left.header').html();
					boxTarget = $(this).find('li.pull-left.header').text().trim();
					$(this).find('li.pull-left.header').attr("data-help-target", boxTarget);
				} else if ($(this).hasClass("data-help-box")) {
					boxTitle = '';
					if (!isNull($(this).data("help-title"))) {
						boxTarget = $(this).data("help-title").trim();
						box += '<label class="help-groupname" data-help-anchor="' + boxTarget + '">' + $(this).data("help-title") + '</label>';

					}
				} else {
					// var boxTitle =
					// $(this).find('div.box-header:eq(0)
					// h3.box-title').contents()[0].nodeValue
					// .trim();
					boxTitle = $(this).find('div.box-header:eq(0) h3.box-title').html();
					boxTarget = $(this).find('div.box-header:eq(0) h3.box-title').text().trim();
					$(this).find('div.box-header:eq(0) h3.box-title').attr("data-help-target", boxTarget);
				}

				var ckItem = [];
				if (!isNull(boxTitle)) {
					box += '<label class="help-groupname" data-help-anchor="' + boxTarget + '">' + boxTitle + '</label>';
				}
				var label = "";
				var oldckbox = "";
				var oldckTabBox = "";
				var oldcktab = "";
				var ckTabID = "";
				var oldCkTabID = "";

				$(this).find('input, select, [data-help-label][data-help-key], [data-help-startgroup][data-help-title], [data-help-endgroup]').not("[type='hidden'], .data-help-skip").each(function() {
					if (!isNull($(this).closest('div.tab-pane'))) {
						var cktab = $(this).closest('div.tab-pane').attr("id");
						if (oldcktab != cktab) {
							var tabTitle = $('a[href="#' + cktab + '"]').text();
							if (!isNull(tabTitle)) {
								box += "<i class=\"fa fa-fw fa-list-alt\"></i><span class=\"tabTitle\">" + tabTitle + "</span>";
								oldcktab = cktab;
							}
						}
					}
					if (!isNull($(this).data("help-title"))) {
						box += '<div class="help-group"><dl><dt class="help-grouptitle">' + $(this).data("help-title") + '</dt></dl>';
						return;
					}
					if (typeof $(this).data("help-endgroup") != "undefined") {
						box += '</div>';
						return;
					}

					var fieldName;
					var ckbox = boxTitle;
					if (!checkInTabs) {
						/*
						 * var ckbox = $(this).closest('div.box').find(
						 * 'div.box-header:eq(0)
						 * h3.box-title').contents()[0].nodeValue .trim();
						 */
						ckbox = $(this).closest('div.box').find('div.box-header:eq(0) h3.box-title').html();
					} else {
						ckTabID = $(this).closest('div.tab-pane').attr("id");
						ckTabBox = $(this).closest('div.box').find('div.box-header:eq(0) h3.box-title').html();

						if (!isNull(ckTabID) && !isNull(ckTabBox) && (ckTabID != oldCkTabID || ckTabBox != oldckTabBox)) {
							box += "<div class=\"help-tabbox-title\">" + ckTabBox + "</div>";
							oldckTabBox = ckTabBox;
							oldCkTabID = ckTabID;
						}

					}

					if (!isNull(ckbox) && ckbox != boxTitle && ckbox != oldckbox) {

						box += "<br /><div class=\"help-title\">" + ckbox + "</div>";
						oldckbox = ckbox;
					}

					var dataInput = "";
					if ($(this).parents('[data-inputname]').length > 0) {
						$(this).parents('[data-inputname]').each(function() {
							dataInput = $(this).data('inputname') + "." + dataInput;
						});
						dataInput = dataInput.slice(0, -1);
					}

					if (!isNull($(this).data('help-key'))) {
						fieldName = $(this).data('help-key');
					} else {
						if (dataInput == "") {
							fieldName = $(this).attr('name');
						} else {
							fieldName = dataInput + "." + $(this).attr('name');
						}
					}
					var fieldAnchorName = boxTarget + "." + fieldName;

					if (typeof $(this).data('help-label') != "undefined") {
						label = $(this).data('help-label');
						$(this).parent().find('label:contains(' + label + ')').attr("data-help-target", fieldAnchorName);
					} else {
						if ($(this).prev('label').length > 0) {
							label = $(this).prev('label').text();
							$(this).prev('label').attr("data-help-target", fieldAnchorName);
						} else if ($(this).closest('div.form-group').find('label').length > 0) {
							label = $(this).closest('div.form-group').find('label').text();
							$(this).closest('div.form-group').find('label').attr("data-help-target", fieldAnchorName);
						}

					}

					var className = defaultClass;
					if (!isNull($(this).data('help-class'))) {
						className = $(this).data('help-class');
					}

					var preloadItemObj = $.Help.getObjects(preloadObj, className);

					if (isNull(preloadItemObj) || typeof fieldName == "undefined" || typeof preloadItemObj == "undefined" || typeof preloadItemObj['name'] == "undefined") {
						return;
					}

					var description = '';
					if (ckItem.indexOf(fieldName) == -1) {
						ckItem.push(fieldName);
						if (fieldName == className) {
							description = preloadItemObj['description'];
						} else {
							var fieldObj = preloadItemObj['childs'];
							$(fieldName.split(".")).each(function(index) {
								var hobj;
								var subFieldName = this;
								var pname = fieldName.split(".").slice(index).join(".");
								hobj = $.Help.getObjects(fieldObj, pname);
								if (!isNull(hobj)) {
									description = hobj['description'];
									return false;
								} else {
									hobj = $.Help.getObjects(fieldObj, subFieldName);
									if (hobj == null) {
										description = "??? " + fieldName;
									} else {
										description = hobj['description'];
										if (typeof hobj['childs'] != "undefined") {
											fieldObj = hobj['childs'];
										}
									}
								}
							});
						}
						box += '<dl><dt class="help-fieldname" data-help-anchor="' + fieldAnchorName + '">' + label + '</dt><dd>' + description + '</dd></dl>';
					}
				});
			});
			box = box + '<br />';
			$.Help.slimScroll();
			$('div.help-content').html(box);
			$('div#main-container [data-help-target]').on("click", function() {
				$.Help.scrollTo($(this).attr("data-help-target"));
			});
			return box;
		});
	}

	return {
	/**
	 * @memberOf $.Help
	 */
	init : function() {

		$.Help.setSize();
		$(window).off('resize');
		$(window).on('resize', function() {
			$.Help.setSize();
		});
	},
	setSize : function() {
		var height = $('html').height() - $('div.help-content').offset().top - $('footer.main-footer').innerHeight();
		// var height = $('div.content-wrapper').innerHeight() +
		// $('div.content-wrapper').offset().top -
		// $('div.help-content').offset().top;
		$('div.doc-help div.slimScrollDiv').height(height);
		$('div.help-content').height(height);
	},

	slimScroll : function() {
		$("div.help-content").slimscroll({
		alwaysVisible : false,
		size : "3px",
		color : "#ccc"
		});
	},

	scrollTo : function(dataHelpAnchor) {
		var container = $("div.help-content");
		var scrollTo = $('[data-help-anchor="' + dataHelpAnchor + '"]');
		var navBarHeight = $('.navbar').height() || 0;
		var delta = $(window).scrollTop() > $(container).offset().top ? $(window).scrollTop() + navBarHeight - $(container).offset().top : 0;
		$(container).animate({
			scrollTop : $(scrollTo).offset().top - $(container).offset().top + $(container).scrollTop() - delta
		}, 1000);
	},

	load : function(className) {
		var path = 'docs/' + className + ".html";
		$.get(path).success(function(helpfile) {
			$('div.help-content').html(helpfile);
			$.Help.slimScroll();
		});
		return null;
	},

	write : function(list) {

		if (list.length === 0) {
			return false;
		}

		var resources = [];
		var helpObj = [];

		for ( var k in list) {
			resources.push({
			name : 'help_' + list[k]["name"],
			uri : _BASE_WEB_ROOT + '/om' + '/help/' + list[k]["name"]
			});
			if (list[k]["localHelp"] == true) {
				resources.push({
				name : 'help_local_' + list[k]["name"],
				uri : '/Monitoring-WebGui/docs/' + list[k]["name"] + '.json'
				});
			}
		}

		var defaultClass = list[0]["name"];
		Preloader.loadCustomResources(resources).then(function() {
			for (k in list) {
				var wsHelp = Preloader.getResource('help_' + list[k]["name"]);
				if (!isNull(list[k]["default"]) && list[k]["default"] == "true") {
					defaultClass = list[k]["name"];
				}
				if (isNull(list[k]["localHelp"])) {
					list[k]["localHelp"] = false;
				}
				if (list[k]["localHelp"] == true) {

					var localHelp = Preloader.getResource('help_local_' + list[k]["name"]);

					if (!isNull(localHelp)) {
						if (!isNull(wsHelp)) {

							if (typeof localHelp["childs"] != "undefined") {
								wsHelp["childs"].push.apply(wsHelp["childs"], localHelp["childs"]);
							}

						} else {
							wsHelp = localHelp;
						}

					} else if (isNull(wsHelp) && isNull(localHelp)) {
						continue;
					}

				}

				helpObj.push(wsHelp);
			}

			if (!isNull(helpObj)) {
				writeHelp(helpObj, defaultClass);
			}

			return null;
		});

	},

	read : function(name, successHandler, errorHandler, targetDom) {
		errorHandler = errorHandler || null;
		getValues(_BASE_WEB_ROOT + '/om' + '/help/' + name, successHandler, errorHandler, targetDom);
	},

	getObjects : function(arr, val) {

		for ( var i in arr) {

			if (typeof arr[i] != "undefined" && arr[i].name == val) {
				return arr[i];
			}
		}
		return null;
	}

	};
})(jQuery);
