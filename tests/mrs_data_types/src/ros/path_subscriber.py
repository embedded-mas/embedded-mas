#!/usr/bin/env python
import rospy
from mrs_msgs.msg import Path

def callback(path_msg):
    # Callback function to process the Path message
    rospy.loginfo("Received Path: %s", path_msg)

def path_subscriber():
    # Initialize the node
    rospy.init_node('path_subscriber', anonymous=True)
    
    # Create a subscriber for the topic 'path_topic' with message type Path
    rospy.Subscriber('path_topic', Path, callback)
    
    # Keep the node running
    rospy.spin()

if __name__ == '__main__':
    path_subscriber()
