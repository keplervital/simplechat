#!/bin/bash

java -jar /app/api/simplechat.jar > /dev/null &
npm --prefix /app/web run start > /dev/null &
/usr/sbin/apache2ctl -D FOREGROUND