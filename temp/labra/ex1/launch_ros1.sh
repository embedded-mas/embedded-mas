#!/bin/bash
set -e

# stop running containers
(docker ps -q --filter "name=embedded-mas-example" | grep -q . && docker stop embedded-mas-example || true)


( docker network inspect ros >/dev/null 2>&1 ||  docker network create ros)
( docker volume inspect x11socket >/dev/null 2>&1 ||  docker volume create x11socket)


 docker run --rm -d --net=ros      --env="ROS_MASTER_URI=http://embedded-mas-example:11311"     --name embedded-mas-example     -p 9090:9090     maiquelb/embedded-mas-ros:latest 

echo -e "\e[1;33m**** Launching ROS container. Wait 20 seconds ****\e[0m"	

 sleep 20


docker exec -d embedded-mas-example /bin/bash -c " source /opt/ros/noetic/setup.bash &&  rostopic pub  --latch /value1 std_msgs/Int32 0"

docker exec -d embedded-mas-example /bin/bash -c " source /opt/ros/noetic/setup.bash &&  rostopic pub  --latch  /current_time std_msgs/String 'unknown' "


echo -e "\e[1;33m**** ROS container is ready. Start the JaCaMo application ****\e[0m"	