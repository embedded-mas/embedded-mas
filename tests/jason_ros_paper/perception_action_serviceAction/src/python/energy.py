import subprocess
import random
import threading
import time

import rospy
from std_msgs.msg import Int32

PROB = 99
DECREMENT = 5
SLEEP_TIME = 2

energy_turtle1 = 100
energy_turtle2 = 100

def callback1(data):
    rospy.loginfo("Received: %d", data.data)
    global energy_turtle1  # Declare a variável como global
    energy_turtle1 = data.data


def callback2(data):
    rospy.loginfo("Received: %d", data.data)
    global energy_turtle2  # Declare a variável como global 
    energy_turtle2 = data.data

def publish_random_energy():
     
    global energy_turtle1  # Declare a variável como global
    global energy_turtle2  # Declare a variável como global 
    
    #rospy.init_node('listener', anonymous=True) 
    #rospy.Subscriber("/turtle1/energy", Int32, callback);
    #rospy.spin()

    while True:
        
        
        probability = random.randint(0, 100)    
        if(probability<PROB):
           # Gerar um número inteiro aleatório entre 0 e 100
           random_number = random.randint(0, DECREMENT)
           energy_turtle1 = energy_turtle1 - random_number
        
           # Montar o comando a ser executado
           command = f'rostopic pub /turtle1/energy std_msgs/Int32 {energy_turtle1}'

           # Executar o comando de forma não bloqueante
           subprocess.Popen(command, shell=True)
        
        probability = random.randint(0, 100)
        if(probability<PROB):
           # Gerar um número inteiro aleatório entre 0 e 100
           random_number = random.randint(0, DECREMENT)
           energy_turtle2 = energy_turtle2 - random_number
        
           # Montar o comando a ser executado
           command = f'rostopic pub /turtle2/energy std_msgs/Int32 {energy_turtle2}'

           # Executar o comando de forma não bloqueante
           subprocess.Popen(command, shell=True)   

        # Aguardar 5 segundos
        time.sleep(SLEEP_TIME)
'''
# Iniciar o loop em um thread separado
publish_thread = threading.Thread(target=publish_random_energy)
publish_thread.daemon = True  # Isso faz com que a thread seja finalizada quando o programa principal terminar
publish_thread.start()

energy_turtle1 = 100
energy_turtle2 = 100
command = f'rostopic pub /turtle1/energy std_msgs/Int32 100'
command = f'rostopic pub /turtle2/energy std_msgs/Int32 100'

# O programa principal pode continuar executando outras tarefas
while True:
    pass

'''
if __name__ == '__main__':
   # Iniciar o loop em um thread separado
   publish_thread = threading.Thread(target=publish_random_energy)
   publish_thread.daemon = True  # Isso faz com que a thread seja finalizada quando o programa principal terminar
   publish_thread.start()

   energy_turtle1 = 100
   energy_turtle2 = 100
   command = f'rostopic pub /turtle1/energy std_msgs/Int32 100'
   command = f'rostopic pub /turtle2/energy std_msgs/Int32 100'

   rospy.init_node('listener', anonymous=True) 
   rospy.Subscriber("/turtle1/energy", Int32, callback1);
   rospy.Subscriber("/turtle2/energy", Int32, callback2);
   rospy.spin()

   # O programa principal pode continuar executando outras tarefas
   while True:
       pass