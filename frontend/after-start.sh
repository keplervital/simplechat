#/bin/bash

retries=15
count=1
found=0

while [ $count -le $retries ] && [ $found != 2 ]
do
    found=0
    root=$(docker logs simplechat-api | grep -o -P '(?<=apiKey: ).*(?=)' | head -1 | tail -1)
    admin=$(docker logs simplechat-api | grep -o -P '(?<=apiKey: ).*(?=)' | head -2 | tail -1)

    if [ ! -z "$root" ]; then
        found=$(( $found + 1 ))
        sed -i "s/auth=.*\"/auth=\"$root\"/g" /app/dist/user1.html
        sed -i "s/auth=.*\"/auth=\"$root\"/g" /usr/share/nginx/html/user1.html
    fi

    if [ ! -z "$admin" ]; then
        found=$(( $found + 1 ))
        sed -i "s/auth=.*\"/auth=\"$admin\"/g" /app/dist/user2.html
        sed -i "s/auth=.*\"/auth=\"$admin\"/g" /usr/share/nginx/html/user2.html
    fi

    count=$(( $count + 1 ))
    sleep 1
done