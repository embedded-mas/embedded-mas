import subprocess
import random
import threading
import time

PROB = 40
DECREMENT = 10


def publish_random_energy():
    energy_turtle1 = 100
    energy_turtle2 = 100
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
        else:
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
        time.sleep(5)

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
