#!/bin/bash

DIR_PROMETHEUS=/etc/prometheus
ALERT_TEMPLATE=$DIR_PROMETHEUS/alert-template.yml
TEMP_ALERT_FILE=$DIR_PROMETHEUS/UnknownAlert.yml
ALERT_TARGET_DIR=$DIR_PROMETHEUS/alertrules

ALERTNAME=$1
EXPR=$2
DURATION=$3
SEVERITY=$4
SUMMARY=$5
DESCRIPTION=$6

if [ "x" == "x$ALERTNAME" ]
then
	echo "Missing Alet Name"
	exit 1
else
	TEMP_ALERT_FILE=$DIR_PROMETHEUS/$ALERTNAME.yml
fi

if [ "x" == "x$EXPR" ]
then
	echo "Missing Expression"
	exit 2
fi

if [ "x" == "x$DURATION" ]
then
        DURATION=1m
fi

if [ "x" == "x$SEVERITY" ]
then
        SEVERITY=critical
fi

cp $ALERT_TEMPLATE $TEMP_ALERT_FILE
sed -i s#__ALERTNAME__#$ALERTNAME#g $TEMP_ALERT_FILE
sed -i s#__EXPRESSION__#"$EXPR"#g $TEMP_ALERT_FILE
sed -i s#__DURATION__#$DURATION#g $TEMP_ALERT_FILE
sed -i s#__SEVERITY__#$SEVERITY#g $TEMP_ALERT_FILE
sed -i s#__SUMMARY__#$SUMMARY#g $TEMP_ALERT_FILE
sed -i s#__DESCRIPTION__#$DESCRIPTION#g $TEMP_ALERT_FILE

mv $TEMP_ALERT_FILE $ALERT_TARGET_DIR

curl -k -X POST -H "Content-Type: application/json" -d "" http://$HOST_MON:$PM_PORT_MON/-/reload
