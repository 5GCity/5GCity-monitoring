#!/bin/bash

USER=node_exporter

cd /tmp
curl -LO https://github.com/prometheus/node_exporter/releases/download/v0.16.0/node_exporter-0.16.0.linux-amd64.tar.gz

tar -xvf node_exporter-0.16.0.linux-amd64.tar.gz

mv node_exporter-0.16.0.linux-amd64/node_exporter /usr/local/bin/

if id $USER >/dev/null 2>&1; then
        echo "user exists"
else
	useradd -rs /bin/false $USER
fi

mkdir -p /opt/exporter/collector

SERVICE_UNIT=/etc/systemd/system/node_exporter.service

cat > $SERVICE_UNIT <<- EOM
[Unit]
Description=Node Exporter
After=network.target
 
[Service]
User=$USER
Group=node_exporter
Type=simple
ExecStart=/usr/local/bin/node_exporter --collector.textfile.directory /opt/exporter/collector
 
[Install]
WantedBy=multi-user.target
EOM

systemctl daemon-reload
systemctl start node_exporter

systemctl enable node_exporter
