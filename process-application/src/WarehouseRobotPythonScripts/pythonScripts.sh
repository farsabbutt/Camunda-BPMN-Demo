#!/bin/bash

exec python3 ExstoreCheck.py &
exec python3 Remote_control_WarehouseRobot.py &
exec python3 Remote_taskhandler_WarehouseRobot.py &
exec python3 StoreCheck.py
exec python3 TXT_Remote_Steuertest_M1.py
exec python3 TXT_Remote_Steuertest_M3.py
exec python3 TXT_Remote_ExTaskHandler_USB_BPMN_without_dbcon.py
exec python3 TXT_Remote_Control_USB_without_dbcon.py