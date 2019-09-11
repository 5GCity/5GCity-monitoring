#!/bin/bash

DIR_PROMETHEUS=/etc/prometheus
ALERT_TARGET_DIR=$DIR_PROMETHEUS/alertrules

ALERTNAME=$1

if [ "x" == "x$ALERTNAME" ]
then
	echo "Missing Alert Name"
	exit 1
fi

rm -f $ALERT_TARGET_DIR/$ALERTNAME.yml

curl -k -X POST -H "Content-Type: application/json" -d "" http://$HOST_MON:$PM_PORT_MON/-/reload
