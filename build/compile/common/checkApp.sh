#!/bin/bash

app_path="vendor/sunmi/app"

for i in $(ls $app_path/*.apk)
do
    zipalign -c -v 4 $i > $i.log
    while read line
    do
        num=`sed 's/^/num/g'|awk '{gsub(/num/,sprintf("",++c));print}'|sed -n '1,20p'|sed 's/^ *//g'| awk 'NR==1{print $1}'`
        #echo num=$num
        if [ $num -gt 100 ]
        then
            echo -e "\nerror: \"$i\" is failed, plz check this is apk.\n"
            status=1
        fi
    done < $i.log
done
