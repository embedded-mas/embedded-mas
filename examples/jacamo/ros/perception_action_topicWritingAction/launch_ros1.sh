#!/bin/bash
set -e

# stop running containers
(docker ps -q --filter "name=embedded-mas-example" | grep -q . && docker stop embedded-mas-example || true)


( docker network inspect ros >/dev/null 2>&1 ||  docker network create ros)
( docker volume inspect x11socket >/dev/null 2>&1 ||  docker volume create x11socket)

 docker run -d --rm --net=ros     --env="DISPLAY_WIDTH=3000"     --env="DISPLAY_HEIGHT=1800"     --env="RUN_XTERM=no"     --name=novnc -p=8080:8080 theasp/novnc:latest

 docker run --rm -d --net=ros     --env="DISPLAY=novnc:0.0"     --env="ROS_MASTER_URI=http://embedded-mas-example:11311"     --name embedded-mas-example     -p 9090:9090     maiquelb/embedded-mas-ros:latest 

 sleep 20


docker exec -d embedded-mas-example /bin/bash -c " source /opt/ros/noetic/setup.bash &&  rostopic pub  --latch /value1 std_msgs/Int32 0"

docker exec -d embedded-mas-example /bin/bash -c " source /opt/ros/noetic/setup.bash &&  rostopic pub  --latch  /current_time std_msgs/String 'unknown' "


echo -e "\e[1;33m**** Docker container is ready. Start the JaCaMo application ****\e[0m"	