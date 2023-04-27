import ftrobopy
from time import sleep

txtcon = ftrobopy.ftrobopy('192.168.0.101', 65000)

M1 = txtcon.motor(3)

M1.setSpeed(-512)
sleep(6)
M1.stop()
sleep(6)
M1.setSpeed(512)
sleep(5)
M1.stop()
