import ftrobopy
import datetime

# Verbindung zum TXT Controller aufbauen und ein Objekt txtcon erstellen
# Das Script wird auf dem TXT Controller ausgeführt weshalb der Host = localhost und der Standardport = 6500 ist

txtcon = ftrobopy.ftrobopy('192.168.7.2', 65000)
txtdevicename = txtcon.getDevicename()
print("Verbindung mit " + txtcon.getDevicename() + " erfolgreich hergestellt!")

def wait():
    pass

# Klassendefiniton für den Lagerplatz inkl. Koordinaten
class StoragePlace:
    def __init__(self, x, y):
        self._x = x
        self._y = y


# Variablendefinition für Motoren und Sensoren der Hochregalroboters
M1 = txtcon.motor(1)  # Motor X Achse
M2 = txtcon.motor(2)  # Motor Z Achse
M3 = txtcon.motor(3)  # Motor Y Achse
I1 = txtcon.input(1)  # Taster gibt an, ob der Roboter in der Initialen X Position steht 1 = gedrückt 0 nicht gedrückt
I2 = txtcon.input(2)  # Taster EndSchalter Gabel ist vorne 1 = gedrückt 0 nicht gedrückt
I3 = txtcon.input(3)  # Taster gibt an, ob der Roboter in der Initialen Y Position steht 1 = gedrückt 0 = nicht gedrückt
I4 = txtcon.input(4)  # Taster EndSchalter Gabel ist hinten 1 = gedrückt 0 = nicht gedrückt

# Variablendefintion der Lagerplätze
place1 = StoragePlace(3100, 1930)
place2 = StoragePlace(5040, 1930)
place3 = StoragePlace(3100, 1000)
place4 = StoragePlace(5040, 1000)
place5 = StoragePlace(3100, 80)
place6 = StoragePlace(5040, 80)

# Globale Variablen der aktuellen Position
X_Position = 0
Y_Position = 0
Z_Position = 0

# Lokale Speicherung der JSON eventdaten
# def writeEventLog(path,fileName, data):
#      filePathNameExt = './' + path + '/' + fileName + '.json'
#      with open(filePathNameExt, 'w') as fp:
#          json.dump(data,fp)

def createEvent(activity):
    eventDic = {
        "process_id": process_id,
        "activity": activity,
        "timestamp": datetime.datetime.now().strftime("%Y/%m/%d, %H:%M:%S"),
        "topic": topic,
        "item": item,
        "worker": txtdevicename
    }
    # writeEventLog('events', str(process_id) + '_' + activity, eventDic)


# Initalisierung des Hochregalrobotors in der X Achse.
def arm_back_init():
    M1.setSpeed(512)
    while I1.state() == 0:
         continue
    else:
        M1.stop()
    global X_Position
    X_Position = 0
    createEvent(arm_back_init.__name__)


# Initalisierung des Hochregalrobotors in der X Achse.
def arm_down_init():
    M3.setSpeed(512)
    while I3.state() == 0:
         continue
    else:
        M3.stop()
    global Y_Position
    Y_Position = 0
    createEvent(arm_down_init.__name__)



# Hochregalroboter positive X Achsen Bewegung bis Entfernung x
def arm_forth_till(x):
    M1.setDistance(x)
    M1.setSpeed(-512)
    while M1.finished() == False:
        continue
    else:
        M1.stop()
    global X_Position
    X_Position = M1.getCurrentDistance()
    createEvent(arm_forth_till.__name__)


'''
Hochregalroboter negative X Achsen Bewegung wird nicht benötigt
'''

# Hochregalroboter positive Y Achsen Bewegung bis Entfernung y
def arm_up_till(y):
    M3.setDistance(y)
    M3.setSpeed(-512)
    while M3.finished() == False:
        continue
    else:
        M3.stop()
    global Y_Position
    Y_Position = M3.getCurrentDistance()
    createEvent(arm_up_till.__name__)


# Hochregalroboter negative Y Achsen Bewegung bis Entfernung y
def arm_down_till(y):
    M3.setDistance(y)
    M3.setSpeed(512)
    while M3.finished() == False:
        continue
    else:
        M3.stop()
    global Y_Position
    Y_Position = M3.getCurrentDistance()
    createEvent(arm_down_till.__name__)



# Gabel negative Z Achsen Bewegung
def fork_back():
    M2.setSpeed(512)
    while I2.state() == 0:
        continue
    else:
        M2.stop()
    global Z_Position
    Z_Position = 0
    createEvent(fork_back.__name__)

# Gabel positive Z Achsen Bewegung
def fork_forth():
    M2.setSpeed(-512)
    while I4.state() == 0:
        continue
    else:
        M2.stop()
    global Z_Position
    Z_Position = 100
    createEvent(fork_forth.__name__)

'''
Funktionsdefinition Ablauf Arbeitsschritte
'''

# Intialisierung des Hochregalroboters - Alle Koordinaten auf 0
def initialize():
    fork_back()
    arm_down_init()
    arm_back_init()
    createEvent(initialize.__name__)

# Teilschritt Ware aufnehmen
def pick_up_item():
    fork_forth()
    arm_up_till(80)
    fork_back()
    arm_down_init()
    createEvent(pick_up_item.__name__)

# Teilschritt Ware ablegen
def put_down_item():
    fork_forth()
    arm_down_till(80)
    fork_back()
    createEvent(put_down_item.__name__)

# Teilschritt Platzfahrt
def spaceride(x,y):  #switchcase
    arm_forth_till(x)
    arm_up_till(y)
    createEvent(spaceride.__name__)


# Einlagerung Ware an bestimmten Lagerplatz
def store_item(place, camunda_process_instance_id, camunda_topic, task_item):
    global process_id
    process_id = camunda_process_instance_id
    global topic
    topic = camunda_topic
    global item
    item = task_item

    switcher = {
        1: place1,
        2: place2,
        3: place3,
        4: place4,
        5: place5,
        6: place6
    }
    initialize()
    arm_up_till(50)
    pick_up_item()
    spaceride(switcher.get(place)._x,switcher.get(place)._y)
    arm_up_till(50)
    put_down_item()
    initialize()
    createEvent(store_item.__name__)


# Auslagerung Ware von einem bestimmten Lagerplatz 
def exstore_item(place, camunda_process_instance_id, camunda_topic, task_item):
    global process_id
    process_id = camunda_process_instance_id
    global topic
    topic = camunda_topic
    global item
    item = task_item

    switcher = {
        1: place1,
        2: place2,
        3: place3,
        4: place4,
        5: place5,
        6: place6
    }
    initialize()
    spaceride(switcher.get(place)._x,switcher.get(place)._y)
    pick_up_item()
    initialize()
    arm_up_till(100)
    put_down_item()
    initialize()
    createEvent(exstore_item.__name__)