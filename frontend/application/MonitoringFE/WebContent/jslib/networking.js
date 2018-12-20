/**
 * (Javascript)<br/>
 * @param integer maskSize <p>
 * <br>
 * <b>maskSize</b> : size of the Netmask (CIDR notation)<br>
 * </p>
 * @return Netmask (bitmask) <p>
 * <b> -</b> The Netmask (bitmask) corresponding with the mask size <br>
 * <b> -</b> "false" if the Netmask size is not valid <br>
 */
function itlNetmaskSizeToBitmask(maskSize, ipType) {
	ipType = typeof ipType !== 'undefined' ?  ipType : 4;
	//Gli operatori bitwise di Javascript operano a 32 bit
	var mask = false;
	var maskArray = [];
	if (ipType == 4) {
		if (( maskSize >= 0 ) && ( maskSize <= 32 )) {
			if (maskSize == 0) {
				mask = 0;
			} else {
				mask = -1 << (32 - maskSize);
			}
			maskArray.push(mask);
			mask = maskArray;
		}
	} else if (ipType == 6) {
		if (( maskSize >= 0 ) && ( maskSize <= 128 )) {
			var remainingSize = maskSize;
			for (var i = 0; i < 4; i++) {
				if (remainingSize == 0) {
					mask = 0;
				} else if (remainingSize < 32) {
					mask = -1 << (32 - remainingSize);
					remainingSize = 0;
				} else {
					mask = -1 << 0;
					remainingSize -= 32;
				}
				maskArray.push(mask);
				mask = maskArray;
			}
		}
	}
	return mask;
}

/**
 * (Javascript)<br/>
 * @param integer bitmask <p>
 * <br>
 * <b>bitmask</b> : bitmask representing the IP address<br>
 * </p>
 * @return IP Address <p>
 * <b> -</b> The IP Address corresponding with the bitmask <br>
 * <b> -</b> "false" if the bitmask is not valid <br>
 */
function itlBitmaskToIpAddress(bitmask) {
    var address = false;
	var bitmaskLength = bitmask.length;
	var bitmaskBlock32 = 0;
	if ($.isArray(bitmask) && ((bitmaskLength == 1) || (bitmaskLength == 4))) {
		var outString = "";
		if (bitmaskLength == 1) {
			//La bitmask e' di tipo IPv4
			bitmaskBlock32 = bitmask[0];
			for (var i = 24; i >= 0; i-=8) {
				var octect = (bitmaskBlock32 >> i) & 255;
				var octectString = octect.toString();
				outString += octectString;
				if (i >= 8) {
					outString += ".";
				}
			}
			address = outString;
		} else {
			//La bitmask e' di tipo IPv6
			for (var i = 0; i < bitmaskLength; i++) {
				bitmaskBlock32 = bitmask[i];
				for (var j = 16; j >= 0; j-=16) {
					var doubleOctect = (bitmaskBlock32 >> j) & 65535;
					var doubleOctectString = doubleOctect.toString(16);
					outString += doubleOctectString;
					if ((j != 0) || (i != bitmaskLength -1)) {
						outString += ":";
					}
				}
			}
			address = outString;
		}
	}
	return address;
}

/**
 * (Javascript)<br/>
 * @param integer netmaskSize <p>
 * <br>
 * <b>bitmask</b> : size of the netmask ("0-32" for IPv4, "0-128" for IPv6)<br>
 * </p>
  * @param integer ipType (optional) <p>
 * <br>
 * <b>bitmask</b> : type of IP address ("4" for IPv4 -default-, "6" for IPv6)<br>
 * </p>
 * @return IP Address <p>
 * <b> -</b> The IP Address representing the netmask<br>
 * <b> -</b> "false" if the bitmask is not valid<br>
 */
function itlNetmaskSizeToIPAddress(netmaskSize, ipType) {
	address = false;
	if ((netmaskSize >= 0) && (netmaskSize <= 128) && ((ipType == 4) || (ipType == 6))) {
		address = itlBitmaskToIpAddress(itlNetmaskSizeToBitmask(netmaskSize, ipType));
	}
	return address;
}