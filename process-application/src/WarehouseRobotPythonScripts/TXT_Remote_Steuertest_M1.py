import ftrobopy
from time import sleep

txtcon = ftrobopy.ftrobopy('192.168.7.2', 65000)

M1 = txtcon.motor(1)

M1.setSpeed(-512)
sleep(6)
M1.stop()
sleep(6)
M1.setSpeed(512)
sleep(5)
M1.stop()
