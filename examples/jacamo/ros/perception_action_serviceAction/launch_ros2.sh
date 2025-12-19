#!/bin/bash
set -e

CONTAINER_NAME="embedded-mas-example"
NOVNC_NAME="novnc"
IMAGE_NAME="maiquelb/embedded-mas-ros2:latest"

# ---------------------------------------------------------
# 1. Stop running containers
# ---------------------------------------------------------
(docker ps -q --filter "name=$NOVNC_NAME" | grep -q . && docker stop "$NOVNC_NAME" || true)
(docker ps -q --filter "name=$CONTAINER_NAME" | grep -q . && docker stop "$CONTAINER_NAME" || true)

# ---------------------------------------------------------
# 2. Create network/volume if needed
# ---------------------------------------------------------
(docker network inspect ros >/dev/null 2>&1 || docker network create ros)
(docker volume inspect x11socket >/dev/null 2>&1 || docker volume create x11socket)

# ---------------------------------------------------------
# 3. Start noVNC container
# ---------------------------------------------------------
docker run -d --rm \
    --net=ros \
    --env="DISPLAY_WIDTH=3000" \
    --env="DISPLAY_HEIGHT=1800" \
    --env="RUN_XTERM=no" \
    --name="$NOVNC_NAME" \
    -p 8080:8080 \
    theasp/novnc:latest

# ---------------------------------------------------------
# 4. Start ROS 2 container
# ---------------------------------------------------------
docker run -d --rm \
    --net=ros \
    --env="DISPLAY=${NOVNC_NAME}:0.0" \
    --name "$CONTAINER_NAME" \
    -p 9090:9090 \
    "$IMAGE_NAME"

echo -e "\e[1;33m**** Launching ROS 2 container. Wait 5 seconds ****\e[0m"
sleep 5

# ---------------------------------------------------------
# 5. Run turtlesim (ROS 2 version)
# ---------------------------------------------------------

docker exec -d "$CONTAINER_NAME" /bin/bash -ic "
    source /opt/ros/humble/setup.bash && \
    ros2 run turtlesim turtlesim_node
"

echo -e "\e[1;33m**** ROS 2 container is ready. Open http://localhost:8080/vnc.html ****\e[0m"

