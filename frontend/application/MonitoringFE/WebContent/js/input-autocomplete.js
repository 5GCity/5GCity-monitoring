$.autocomplete = {
		
		prepare : function(targetContainer, cachedList, updateCachedList, onSearchCompleted){
				var inputList = $(targetContainer)
							.find('input[name="ip"], input[name="ipAddress"], input[data-inputmask="\'alias\':\'ip\'"]')
							.not('[skip-autocomplete]');
//				if (!isNull(inputList[0])) {
//					initIp = inputList[0].defaultValue;
//				}	
				
				function onSearchCompletedCallback(input){
					if(!isNull(onSearchCompleted))
						onSearchCompleted(input);
				};
				
				$(inputList).each(function(){
					var input = $(this);
					var selectElement = $(targetContainer)
										.find('select[name="subnetName"]');
					var selectObj = '';
					if(selectElement.length != 0)
						selectObj = selectElement.first();
					else
						selectObj = $(input).parents('.data-input-l1')
											.find('div[subnetName] select').first();
					$(input).click(function(){
						var selectedSubnet = $(selectObj).find('option:selected').val();
						var gateway = $(targetContainer).find('input[name="gatewayAddress"]');
						var gatewayOK = selectedSubnet != NULL_TOKEN ? true : false;
						gatewayOK = gatewayOK && (gateway.length == 0 ? true : (gateway).val() != '' ? true : false);
						if(gatewayOK && $(input).val() == ''){
							$(input).autocomplete("search"); 
						}
					});
					$(input).autocomplete({
						minLength : 0,
						source : function(request, response) {
							
							if($(input).parent().hasClass('has-warning') && $(input).hasClass('ip-out-of-range')
									&& !$(input).hasClass('dont-add-autocomplete-warning')){
								unbindWarningInput(input);
								$(input).removeClass('ip-out-of-range');
							}
							
							if(!isNull(updateCachedList)){
								cachedList = updateCachedList(input);
							}
							
							if(cachedList == null || cachedList.length == 0)
							{
								wMsg = 'There is no valid subnet IP addresses';
								
								$(input).addClass('ip-out-of-range');
								bindWarningInput(input, wMsg);
								
								onSearchCompletedCallback(input);
								return [];
							}
							
							var ip = '';
							var netmaskString = '';
							var netmaskBit = 0;
							var networkPlanType = 'IPV4';
							var optionSelected = $(selectObj).find('option:selected');
							var selectedSubnet = $(optionSelected).val();
							var usedIpVector = [];
							cachedList.each(
								function() {
									if(this.name == selectedSubnet)
									{
										ip = this.gatewayAddress;
										netmaskString = this.netmask;
										networkPlanType = this.networkPlanType;
										
										if($.inArray(ip, usedIpVector) == -1)
											usedIpVector.push(ip);
										return false;
									};
							});
							if(networkPlanType == "IPV6"){
								onSearchCompletedCallback(input);
								return [];
							}
								
							if(ip.split('.').length != 4 || $.inArray('',ip.split('.')) != -1
									|| !isNull(ip.split('.').join().replace(/,/g,'').match(/[^0-9]/g)))
							{
								wMsg = 'The subnet gateway address is not valid.';
								
								$(input).addClass('ip-out-of-range');
								bindWarningInput(input, wMsg);
								
								onSearchCompletedCallback(input);
								return [];
							}
							if(netmaskString != '')
							{
								wMsg = 'IP address not valid';
								var vect = netmaskString.split('.');
								
								for(var i=0; i < vect.length; i++)
								{
									if(vect[i] == '255')
									{
										netmaskBit += 8;
									}
									else
									{
										var val = parseInt(vect[i]);
										var only1Bit = val.toString(2).split('0')[0]; // 224 -> 11100000
										netmaskBit += only1Bit.length; // 111 -> 3
										break; // esco dal for
									}
								}
								
								var src = $.autocomplete.getAutocompletes(input,
										ip, netmaskBit,request.term, usedIpVector, wMsg);
								
								onSearchCompletedCallback(request.term);
								
								response(src);
							}
							else{ 
								var wMsg = 'There is no subnet ip address';
								
								$(input).addClass('ip-out-of-range');
								bindWarningInput(input, wMsg);
								
								onSearchCompletedCallback(null);
								
								response([]);
							}
							
						},//fine source
				    });//fine autocomplete
				});
		},
		
		getPartialIpBlockNumbers : function(termValueToMatch, startValue,endValue, ipRootVect,
				usedIPvector, isLastBlock, strictSearch){	
			
			//vero, se il valore suggerito contiene il valore termValueToMatch in stringa 
			//e che non eccediamo il valore massimo.
			function continueSearch(suggVal,termValueToMatch)
			{
				
				if(suggVal > 255 || suggVal > endValue || suggVal < startValue)
					return false;
				if (termValueToMatch == -1)
					return true; // significa tutti i valori sono accettabili.
				if(strictSearch && suggVal != termValueToMatch)
					return false;
				if((suggVal).toString().slice(0,(termValueToMatch).toString().length ) != (termValueToMatch).toString())
					return false;
				
				return true;
			};
			
			function getNewIpAddresses(newBlockNumbers, maxCount)
			{
				var count = 0;
				var ipAvailableVect = [];
				for(var irl = 0; irl< Math.max(1,ipRootVect.length); irl++)
				{
					var ipRootVal = !isNull(ipRootVect[irl]) ? ipRootVect[irl] + '.' : '';
					for(var i=0; i < newBlockNumbers.length; i++)
					{
						var suggVal = newBlockNumbers[i];
						var newIp = ipRootVal + suggVal;
						if ( !isLastBlock )
						{
							ipAvailableVect.push(newIp);
							count += 1;
						}
						else
						{
							// è l'ultimo blocco quindi devo verificare che si tratta di un Ip
							//anche controllare che negli indirizzi usati non ci sia questo nuovo indirizzo
							var ipNotUsed = $.inArray(newIp, usedIPvector) == -1; 
							var notBroadcast = suggVal !=  endValue;
							var notNetAddress = suggVal != startValue ;
							if (notBroadcast && notNetAddress && ipNotUsed)
							{
								ipAvailableVect.push(newIp);
								count += 1;
							}
						}
						if(count == maxCount)
							break;
					}
					if(count == maxCount)
						break;
				}
				return ipAvailableVect;
			}
			
			var newBlockNumbers = [];
			var maxCount = 50;
			var tv = termValueToMatch;
			if(termValueToMatch == -1 || continueSearch(startValue , termValueToMatch))
				tv = startValue;
			var add = 1;
			var suggVal = tv; // valore suggerito
			while(add <= 100)
			{
				// controllo ad esempio 222, rischio di saltare 220.
				if(continueSearch(parseInt(suggVal/10)* 10 , termValueToMatch))
					suggVal = parseInt(suggVal/10) * 10 ;
				var foundAtLeastOne = false;
				while(continueSearch(suggVal, termValueToMatch) )
				{
					foundAtLeastOne = true;
					if(suggVal != -1 && $.inArray(suggVal, newBlockNumbers) == -1)
						newBlockNumbers.push(suggVal);
					suggVal += 1;
					if(strictSearch)
						break;
				}
				
				if (tv == 0 || suggVal > endValue || (strictSearch && foundAtLeastOne))
					break;
				
				add *=10;
				suggVal = tv*add;
			}
			return getNewIpAddresses(newBlockNumbers, maxCount);
		},// fine get matched number,
		
		getAutocompletes: function(input, ip, netMask, term, usedIPvector, wMsg){
			
			var termVect = term.split('.');
			var warning = false;
			var rootBlockLength = parseInt(netMask/8);
			if(term.length != 0 && term[0] != ip[0] && rootBlockLength > 0)
			{
				warning = true; 
				wMsg = 'The ip address must start with ' + ip[0];
			}
			var ipRootVect = [];
			var ipRoot = '';
			var ipVect = ip.split('.');
			var source = [];
			var wMsg2 = wMsg;
			//faccio un loop per i 4 blocchi di 8 bit
			for (var i=0; i < Math.max(4,termVect.length) && !warning; i++)
			{
				if(i < rootBlockLength )
				{
					//creo il prefisso degli indirizzi
					ipRoot += ipVect[i];
					ipRootVect[0] = ipRoot;
					if ( i < 3) // mancano altri blocchi
						ipRoot += '.';
					
					if( (termVect.length > i && ('' + ipVect[i]).indexOf(''+termVect[i]) == -1) // se digito qualcosa di diverso.
							|| ( termVect[i] == '' && !isNull(termVect[i+1]) )) // ho messo due punti
					{
						wMsg2 = 'The valid IP addresses prefix is "' + ipRoot +'"';
						warning = true; source=[]; break;
					}
				}	
				else
				{
					wMsg2 = wMsg;
					if(typeof termVect[i] != 'undefined' &&
						(isNaN(termVect[i]) // se digiti un carattere non numerico
						   || ( termVect[i][0] == 0 && (termVect[i].length > 1 || i==3 ))
						   ||( termVect[i] == '' && typeof termVect[i+1] != 'undefined') // se digiti due punti consecutivi
						   || termVect.length > 4)) // se digiti un ip con piu di 4 blocchi
					{
						wMsg2 = 'Invalid IP address format';
						warning = true; source=[]; break;
					}
					
					if(term != '' &&  ipRoot.length > term.length) // 
					{
						//immetto automaticamente il prefisso degli indirizzi.
						input.val(ipRoot);
						term = ipRoot;
						termVect = ipRoot.split('.');
					};
					// la versione con il placeholder
//					if(term == ''){
//						$(input).attr('placeholder', ipRoot);
//						$(input).parent().find('.glyphicon-ok-sign').remove();
//						if(!$(input).parent().hasClass('"has-feedback"')){
//							var okSign = '<span class="glyphicon glyphicon-ok-sign form-control-feedback"'
//								+ ' aria-hidden="true" style="cursor:pointer; pointer-events : all;"></span>';
//							$(input).parent().addClass("has-feedback").append(okSign);
//							$(input).parent().find('.glyphicon-ok-sign')
//											 .click(function(){
//												 input.val(ipRoot);
//													term = ipRoot;
//													termVect = ipRoot.split('.');
//													$(input).parent().find('.glyphicon-ok-sign').remove();
//													$(input).parent().removeClass('has-feedback');
//													$(input).autocomplete('search');
//											 });
//						}
//					}
//					if(term != ''){
//						$(input).parent().find('.glyphicon-ok-sign').remove();
//						$(input).attr('placeholder', '');
//					}
					
					var blockPos = i+1;
					var next8bit = blockPos*8;
					var freebit = next8bit - netMask;
						freebit = Math.min(freebit,8);
					var totValues = Math.pow(2, freebit);
					var currentIpBlockValue = parseInt(ipVect[i]);
					
					var isLastBlock = false; 
					if(blockPos == 4)
					{
						isLastBlock = true; // se true, vuol dire che è l'ultimo blocco che cerchiamo.
					}
					var strictSearch = false;
					if( termVect.length > 3 && !isLastBlock)
						strictSearch = true;
					
					//cerco l'intervallo che contiene gli IP nella stessa maschera
					var nthInterval = parseInt(currentIpBlockValue/totValues);
					var	startValue = (nthInterval)*totValues;
					var endValue = startValue + totValues - 1;
					
					var termValueToMatch = -1;
					if(typeof termVect[i] != 'undefined'&& termVect[i] != '' )
						termValueToMatch = parseInt(termVect[i]);
					wMsg2 += ' or already used';
					if(termValueToMatch != -1 && (termValueToMatch <= startValue || termValueToMatch >= endValue))
					{
						var start = startValue;
						var end = endValue;
						var root = 'root';
						var orAlreadyUsed = '';
						var numberPos = ['first', 'second', 'third', 'last'];
						
						if(isLastBlock)
						{ 
							start = startValue + 1;
							end = endValue - 1;
							root = '';
							orAlreadyUsed = ' or already used';	
						}
//						if(termValueToMatch < start || termValueToMatch > end)
//							warning = true;
						wMsg2 = 'IP address '+ root +' not valid' + orAlreadyUsed + '. The ' + numberPos[i] + ' value must be within ['+ start + ', ' + end +']';
					}
					source = $.autocomplete.getPartialIpBlockNumbers(termValueToMatch,
							startValue, endValue, ipRootVect, usedIPvector, isLastBlock, strictSearch);
					ipRootVect = source;
					if(source.length == 0){
						warning = true; break;
					}
				} // fine else
			}//fine for;
			
			if((warning || source.length == 0)){
				$(input).addClass('ip-out-of-range');
				bindWarningInput(input, wMsg2);
			}
			
			return source;
		},
};